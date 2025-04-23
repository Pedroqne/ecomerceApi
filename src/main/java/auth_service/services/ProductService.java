package auth_service.services;

import auth_service.dtos.PaginatedResponse;
import auth_service.dtos.ProductRequestDTO;
import auth_service.dtos.ProductResponseDTO;
import auth_service.entities.Product;
import auth_service.enums.StatusProduct;
import auth_service.repositories.CategoryRepository;
import auth_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ProductResponseDTO> getProductsByCategory(String categoryName) {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().getName().equals(categoryName))
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStatus(),
                        p.getCategory().getName()
                        ))
                .collect(Collectors.toList());
    }

    public PaginatedResponse<ProductResponseDTO> getPaginationProducts(int page, int size) {

                Page<Product> pagedResult = productRepository.findAll(PageRequest.of(page, size, Sort.by("name").descending()));

                List<ProductResponseDTO> products = pagedResult.getContent()
                .stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStatus(),
                        p.getCategory().getName()
                )).collect(Collectors.toList());



        return new PaginatedResponse<> (
                products,
                pagedResult.getNumber(),
                pagedResult.getSize(),
                pagedResult.getTotalElements(),
                pagedResult.getTotalPages()
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
