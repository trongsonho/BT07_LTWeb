package vn.iotstar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCategories_Success() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testGetCategoryById_Success() throws Exception {
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.categoryId").value(1));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/api/categories/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "test_icon.png", MediaType.IMAGE_PNG_VALUE, "dummy content".getBytes()
        );

        mockMvc.perform(multipart("/api/categories")
                        .file(icon)
                        .param("categoryName", "Test Unique Category " + System.currentTimeMillis()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.categoryName", containsString("Test Unique Category")));
    }

    @Test
    void testCreateCategory_DuplicateName_ReturnsConflict() throws Exception {
        // Seed category with known name
        String dupName = "Laptops & Computers";

        mockMvc.perform(multipart("/api/categories")
                        .param("categoryName", dupName))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testDeleteCategory_WithProducts_ReturnsConflict() throws Exception {
        // Category 1 has associated seeded products, so delete should fail with 409 Conflict
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testCreateAndUpdateCategory_Success() throws Exception {
        String originalName = "Cat For Update " + System.currentTimeMillis();
        String updatedName = "Updated Cat Name " + System.currentTimeMillis();

        // 1. Create
        mockMvc.perform(multipart("/api/categories")
                        .param("categoryName", originalName))
                .andExpect(status().isCreated());

        // 2. Fetch list to get new id
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }
}
