package auth_service.controllers;

import auth_service.dtos.ProductRequestDTO;
import auth_service.dtos.ProductResponseDTO;
import auth_service.entities.Product;
import auth_service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@EnableMethodSecurity
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestDTO product) {
        return productService.createProduct(product);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<ProductResponseDTO> getAllProducts() {

        var products = productService.getAllProducts();

        return products;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ProductResponseDTO getProductById(@PathVariable Long id) {

        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);

        return ResponseEntity.ok("Produto excluido com sucesso.");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Optional<Product>> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO product) {
        return productService.updateProduct(id, product);
    }

}
