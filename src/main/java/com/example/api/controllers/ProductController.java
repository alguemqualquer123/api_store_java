package com.example.api.controllers;

import com.example.api.dtos.*;
import com.example.api.hoocks.ErrorResponse;
import com.example.api.hoocks.ResponseMessage;
import com.example.api.models.ProductModel;
import com.example.api.repositories.ProductRepository;

import org.springframework.beans.BeanUtils;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@SpringBootApplication
@RequestMapping("/api")
@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @SuppressWarnings("unused")
    private ProductModel productModel;
    
    
    
    @GetMapping("/get-products")
    public ResponseEntity<List<String>> getRoutes() {
        List<String> routes = List.of("route1", "route2", "route3");
        return ResponseEntity.ok(routes);
    }
    
    
    @GetMapping("/products")
    public  ResponseEntity<?> getAllProducts(){
        return ResponseEntity.status(200).body(productRepository.findAll());
    }

    @PostMapping("/get-product")
    public ResponseEntity<?> getProductById(@RequestBody ProductRequest request) {
        if (request.id == null || request.id.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("ID não pode ser nulo ou vazio."));
        }


        Optional<ProductModel> product = productRepository.findById(request.id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Produto não encontrado."));
        }

        return ResponseEntity.ok(product.get());
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateRequest request) {
        if (request.getId() == null || request.getId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("ID não pode ser nulo ou vazio."));
        }

        Optional<ProductModel> existingProduct = productRepository.findById(request.getId());
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Produto não encontrado."));
        }

        ProductModel product = existingProduct.get();

        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
        }
        if (request.getValue() != null && !request.getValue().isBlank()) {
            product.setValue(new BigDecimal(request.getValue()));
        }
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            try {
                product.setCategory(ProductModel.CategoryType.valueOf(request.getCategory().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Categoria inválida."));
            }
        }

        productRepository.save(product);

        return ResponseEntity.ok(product);
    }

    @PatchMapping("/update-value")
    public ResponseEntity<?> updateProductValue(@RequestBody @Valid ProductValueUpdateRequest request) {
        if (request.getId() == null || request.getId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("ID não pode ser nulo ou vazio."));
        }
        if (request.getValue() == null || request.getValue().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("O preço deve ser maior ou igual a 0."));
        }

        Optional<ProductModel> productOptional = productRepository.findById(request.getId());
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Produto não encontrado."));
        }

        ProductModel product = productOptional.get();
        product.setValue(request.getValue());
        productRepository.save(product);

        return ResponseEntity.ok(product);
    }
    @PatchMapping("/update-name")
    public ResponseEntity<?> updateProductName(@RequestBody @Valid ProductNameUpdateRequest request) {
        if (request.getId() == null || request.getId().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("ID não pode ser nulo ou vazio."));
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("O Nome não pode ser nulo."));
        }

        Optional<ProductModel> productOptional = productRepository.findById(request.getId());
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Produto não encontrado."));
        }

        ProductModel product = productOptional.get();
        product.setName(request.getName()); // Correção aqui: Atualiza o nome, não o valor
        productRepository.save(product);

        return ResponseEntity.ok(product);
    }


    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Erro de validação: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }
        
        if (productRecordDto.name() == null || productRecordDto.name().isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Nome do produto não pode ser nulo ou vazio.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        if (productRepository.existsByName(productRecordDto.name())) {
            ErrorResponse errorResponse = new ErrorResponse("Produto já existe com o mesmo nome.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        if (productRecordDto.category() == null || productRecordDto.category().isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Categoria do produto não definida.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        boolean validCategory = false;
        for (ProductModel.CategoryType type : ProductModel.CategoryType.values()) {
            if (type.name().equalsIgnoreCase(productRecordDto.category())) {
                validCategory = true;
                break;
            }
        }
        System.out.printf("Category:",productRecordDto.category());
        if (!validCategory) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categoria inválida.");
        }
        
        try {
            var productModel = new ProductModel();
            BeanUtils.copyProperties(productRecordDto, productModel);
            productModel.setCategory(ProductModel.CategoryType.valueOf(productRecordDto.category().toUpperCase()));
            return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o produto: " + e.getMessage());
        }
    }
}
