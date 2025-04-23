package auth_service.entities;

import auth_service.enums.StatusProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 150)
    @Column(unique = true)
    private String name;

    @Size(max = 225)
    private String description;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", message = "O preço deve ser maior que zero.", inclusive = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusProduct status;

    private LocalDateTime createdIn;
    private LocalDateTime updateIn;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public StatusProduct getStatus() {
        return status;
    }

    public void setStatus(StatusProduct status) {
        this.status = status;
    }

    public LocalDateTime getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(LocalDateTime createdIn) {
        this.createdIn = createdIn;
    }

    public LocalDateTime getUpdateIn() {
        return updateIn;
    }

    public void setUpdateIn(LocalDateTime updateIn) {
        this.updateIn = updateIn;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @PrePersist
    public void prePersit() {
        this.createdIn = LocalDateTime.now();
        this.updateIn = LocalDateTime.now();
    }
}
