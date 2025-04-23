package auth_service.services;

import auth_service.entities.Category;
import auth_service.entities.Product;
import auth_service.repositories.CategoryRepository;
import auth_service.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CategoryService {

    @Autowired
    public CategoryRepository categoryRepository;
    @Autowired
    public ProductRepository productRepository;


    public Category createCategory(Category catergory) {

        categoryRepository.save(catergory);

        return ResponseEntity.status(HttpStatus.CREATED).body(catergory).getBody();
    }

    public List<Category> getAllCategory() {
        var categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma categoria foi encontrada.");
        }

        return categories;
    }

    public Category getCategoryById(Long id) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma categoria com id correspondente."));

        return category;
    }

    public ResponseEntity<Void> deleteCategory(Long id) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        categoryRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    public Category updateCategory(Long id, Category categoryUpdate){

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        category.setName(categoryUpdate.getName());
        category.setDescription(categoryUpdate.getDescription());
        category.setUpdateIn(LocalDateTime.now());
        category.setProducts(categoryUpdate.getProducts());

        return category;

    }
}
