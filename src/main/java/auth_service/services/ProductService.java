package auth_service.services;

import auth_service.dtos.ProductRequestDTO;
import auth_service.dtos.ProductResponseDTO;
import auth_service.entities.Product;
import auth_service.repositories.CategoryRepository;
import auth_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<String> createProduct(ProductRequestDTO dto){

        var product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStatus(dto.status());

        var category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        product.setCategory(category);
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body("Produto criado com sucesso.");

    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getCategory().getName()
                ))
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getCategory().getName()
        );
    }

    public ResponseEntity<Void> deleteProduct(Long id){

        var product = productRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        productRepository.existsById(id);

        return ResponseEntity.noContent().build();

    }

    public ResponseEntity<Optional<Product>> updateProduct(Long id, ProductRequestDTO dto) {

        var product = productRepository.findById((id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
                );

        var category = categoryRepository.getById(dto.categoryId());

        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setStatus(dto.status());
        product.setCategory(category);
        product.setDescription(dto.description());


        Optional<Product> productAtualizado = productRepository.findById((id));

        return ResponseEntity.ok(productAtualizado);
    }
}
