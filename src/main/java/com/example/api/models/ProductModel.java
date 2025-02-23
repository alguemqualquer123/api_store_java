package com.example.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_PRODUCTS")
public class ProductModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Define o UUID como chave primária
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "product_id", columnDefinition = "VARCHAR(36)", unique = true, nullable = false)
    private String productId;

    @NotNull(message = "Nome do produto não pode ser nulo")
    @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres")
    @Column(name = "name", columnDefinition = "VARCHAR(100)", unique = true, nullable = false)
    private String name;

    @NotNull(message = "Preço não pode ser nulo")
    @Min(value = 0, message = "O preço deve ser maior ou igual a 0")
    @Column(name = "value", precision = 10, scale = 2, nullable = false)
    private BigDecimal value;

    @Enumerated(EnumType.STRING) // Armazena como String no banco de dados
    @NotNull(message = "Categoria não pode ser nula")
    @Column(name = "category", columnDefinition = "VARCHAR(36)", nullable = false)
    private CategoryType category;

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public enum CategoryType {
        ITENS, PACKAGE;
    }
}