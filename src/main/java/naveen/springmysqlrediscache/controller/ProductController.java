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

    @PostMapping("/employees")
    public Product addEmployee(@RequestBody Product product) {

        return productRepository.save(product);
    }


    @GetMapping("/employees")
    public ResponseEntity<List<Product>> getAllEmployees() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("employees/{employeeId}")
    @Cacheable(value = "employees", key = "#employeeId")
    public Product findEmployeeById(@PathVariable(value = "employeeId") Integer employeeId) {
        System.out.println("Employee fetching from database:: " + employeeId);
        return productRepository.findById(employeeId).orElseThrow(
                () -> new ResouceNotFoundException("Employee not found" + employeeId));

    }


    @PutMapping("employees/{employeeId}")
    @CachePut(value = "employees", key = "#employeeId")
    public Product updateEmployee(@PathVariable(value = "employeeId") Integer employeeId,
                                  @RequestBody Product productDetails) {
        Product product = productRepository.findById(employeeId)
                .orElseThrow(() -> new ResouceNotFoundException("Employee not found for this id :: " + employeeId));
        product.setName(productDetails.getName());
        final Product updatedProduct = productRepository.save(product);
        return updatedProduct;

    }


    @DeleteMapping("employees/{id}")
    @CacheEvict(value = "employees", allEntries = true)
    public void deleteEmployee(@PathVariable(value = "id") Integer employeeId) {
        Product product = productRepository.findById(employeeId).orElseThrow(
                () -> new ResouceNotFoundException("Employee not found" + employeeId));
        productRepository.delete(product);
    }
}
