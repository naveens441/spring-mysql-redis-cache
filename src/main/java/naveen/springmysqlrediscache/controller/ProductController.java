package naveen.springmysqlrediscache.controller;


import lombok.RequiredArgsConstructor;
import naveen.springmysqlrediscache.ResouceNotFoundException;
import naveen.springmysqlrediscache.model.Product;
import naveen.springmysqlrediscache.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping("/product")
    public Product addProduct(@RequestBody Product product) {

        return productRepository.save(product);
    }


    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("product/{productId}")
    @Cacheable(value = "product", key = "#productId")
    public Product findProductById(@PathVariable(value = "productId") Integer productId) {
        System.out.println("Product fetching from database:: " + productId);
        return productRepository.findById(productId).orElseThrow(
                () -> new ResouceNotFoundException("product not found" + productId));

    }


    @PutMapping("product/{productId}")
    @CachePut(value = "product", key = "#productId")
    public Product updateEmployee(@PathVariable(value = "productId") Integer employeeId,
                                  @RequestBody Product productDetails) {
        Product product = productRepository.findById(employeeId)
                .orElseThrow(() -> new ResouceNotFoundException("Product not found for this id :: " + employeeId));
        product.setName(productDetails.getName());
        final Product updatedProduct = productRepository.save(product);
        return updatedProduct;

    }


    @DeleteMapping("product/{id}")
    @CacheEvict(value = "product", allEntries = true)
    public void deleteProduct(@PathVariable(value = "id") Integer employeeId) {
        Product product = productRepository.findById(employeeId).orElseThrow(
                () -> new ResouceNotFoundException("Product not found" + employeeId));
        productRepository.delete(product);
    }
}
