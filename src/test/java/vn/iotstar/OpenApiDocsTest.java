package vn.iotstar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOpenApiJson_Success() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", notNullValue()))
                .andExpect(jsonPath("$.info.title", is("Product Management API")))
                .andExpect(jsonPath("$.paths['/api/categories']", notNullValue()))
                .andExpect(jsonPath("$.paths['/api/products']", notNullValue()));
    }

    @Test
    void testSwaggerUiRedirect_Success() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }
}
