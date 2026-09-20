package vn.iotstar.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Hidden
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping({"/categories", "/category"})
    public String categoriesPage() {
        return "categories";
    }

    @GetMapping({"/products", "/product"})
    public String productsPage() {
        return "products";
    }
}
