package vn.iotstar.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.config.StorageProperties;
import vn.iotstar.exception.StorageException;
import vn.iotstar.exception.StorageFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileSystemStorageServiceImpl implements IStorageService {

    private final Path rootLocation;
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "svg", "bmp")
    );

    public FileSystemStorageServiceImpl(StorageProperties properties) {
        if (properties.getLocation() == null || properties.getLocation().trim().isEmpty()) {
            this.rootLocation = Paths.get("uploads");
        } else {
            this.rootLocation = Paths.get(properties.getLocation());
        }
    }

    @Override
    public String getSorageFilename(MultipartFile file, String id) {
        String originalFilename = file.getOriginalFilename();
        String ext = (originalFilename != null) ? FilenameUtils.getExtension(originalFilename).toLowerCase() : "png";
        if (ext.isEmpty() || !ALLOWED_EXTENSIONS.contains(ext)) {
            ext = "png";
        }
        return "file_" + id + "." + ext;
    }

    @Override
    public void store(MultipartFile file, String storeFilename) {
        try {
            if (file == null || file.isEmpty()) {
                throw new StorageException("Cannot store empty file.");
            }

            // Sanitize filename to prevent path traversal
            String cleanFilename = Paths.get(storeFilename).getFileName().toString();
            String ext = FilenameUtils.getExtension(cleanFilename).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                throw new StorageException("Unsupported file extension: " + ext + ". Allowed extensions: " + ALLOWED_EXTENSIONS);
            }

            Path destinationFile = this.rootLocation.resolve(cleanFilename).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Security check failed: Cannot store file outside root directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + storeFilename, e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new StorageFileNotFoundException("Could not read file: " + filename);
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Path load(String filename) {
        String cleanFilename = Paths.get(filename).getFileName().toString();
        return rootLocation.resolve(cleanFilename).normalize();
    }

    @Override
    public void delete(String storeFilename) throws Exception {
        if (storeFilename == null || storeFilename.trim().isEmpty()) {
            return;
        }
        try {
            String cleanFilename = Paths.get(storeFilename).getFileName().toString();
            Path destinationFile = rootLocation.resolve(cleanFilename).normalize().toAbsolutePath();
            if (destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                Files.deleteIfExists(destinationFile);
            }
        } catch (IOException e) {
            // Log or ignore if file already missing
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage directory", e);
        }
    }
}
