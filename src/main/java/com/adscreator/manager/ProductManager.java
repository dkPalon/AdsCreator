package com.adscreator.manager;

import com.adscreator.data.ProductRepository;
import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class ProductManager {
    private final ProductRepository productRepository;

    @Autowired
    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(ProductManager.class);

    public Product getProduct(String productID) {
        return this.productRepository.findBySerialNumber(productID);
    }

    public Set<String> getIDs() {
        return this.productRepository.getAllIds();
    }

    public List<Product> getProductsInCategory(String category) {
        return this.productRepository.findProductsByCategory(category);
    }

    @PostConstruct
    private void initProducts() {
        for (int i = 0; i < 10; i++) {
            String serialNumber = "A" + i;
            Product genProduct = new Product();
            genProduct.setSerialNumber(serialNumber);
            genProduct.setPrice(generatePrice());
            genProduct.setCategory(generateCategory());
            genProduct.setTitle(generateName());
            this.productRepository.save(genProduct);
        }
        this.productRepository.flush();
        LOGGER.info("Created Products are: ");
        LOGGER.info(String.valueOf(this.productRepository.findAll().toString()));
    }

    private double generatePrice() {
        double start = 1;
        double end = 500;
        double random = new Random().nextDouble();
        return start + (random * (end - start));
    }

    private String generateCategory() {
        String[] categories = {"Electronics", "Furniture", "Food", "Health", "Cars"};
        int random = new Random().nextInt(categories.length);
        return categories[random];
    }

    public String generateName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
