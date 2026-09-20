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
public class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllProducts_Success() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testGetProductById_Success() throws Exception {
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.productId").value(1));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        mockMvc.perform(get("/api/products/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "imageFile", "product_test.png", MediaType.IMAGE_PNG_VALUE, "dummy product image".getBytes()
        );

        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("productName", "Test Unique Product " + System.currentTimeMillis())
                        .param("quantity", "15")
                        .param("unitPrice", "12500000.0")
                        .param("discount", "10.0")
                        .param("description", "High quality test product")
                        .param("status", "1")
                        .param("categoryId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.productName", containsString("Test Unique Product")))
                .andExpect(jsonPath("$.body.finalPrice").value(11250000.0));
    }

    @Test
    void testCreateProduct_InvalidCategory_ReturnsNotFound() throws Exception {
        mockMvc.perform(multipart("/api/products")
                        .param("productName", "Invalid Cat Product " + System.currentTimeMillis())
                        .param("quantity", "5")
                        .param("unitPrice", "500000.0")
                        .param("discount", "0.0")
                        .param("status", "1")
                        .param("categoryId", "99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testCreateProduct_DuplicateName_ReturnsConflict() throws Exception {
        String existingName = "MacBook Pro 14 M3";

        mockMvc.perform(multipart("/api/products")
                        .param("productName", existingName)
                        .param("quantity", "10")
                        .param("unitPrice", "35000000.0")
                        .param("discount", "5.0")
                        .param("status", "1")
                        .param("categoryId", "1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(false));
    }
}
