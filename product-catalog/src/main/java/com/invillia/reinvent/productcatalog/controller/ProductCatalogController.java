package com.invillia.reinvent.productcatalog.controller;

import com.invillia.reinvent.productcatalog.dto.ProductDto;
import com.invillia.reinvent.productcatalog.entity.Product;
import com.invillia.reinvent.productcatalog.service.ProductCatalogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/product")
public class ProductCatalogController {

    @Autowired
    private ProductCatalogService productCatalogService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<ProductDto> listProducts() {
        List<Product> productList = productCatalogService.listProducts();
        return productList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
         Optional<Product> productOptional = productCatalogService.getProductById(id);
         if (productOptional.isPresent()) {
             return ResponseEntity.ok().body(convertToDto(productOptional.get()));
         }
         return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product createdProduct = productCatalogService.createProduct(product);
        return convertToDto(createdProduct);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    private Product convertToEntity(ProductDto productDto)  {
        Product product = modelMapper.map(productDto, Product.class);
        return product;
    }

}
