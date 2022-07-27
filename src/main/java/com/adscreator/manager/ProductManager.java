package com.adscreator.manager;

import com.adscreator.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

@Component
public class ProductManager {
    private final HashMap<String, Product> products;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductManager.class);

    public ProductManager() {
        this.products = new HashMap<>();
    }

    public Product getProduct(String productID) {
        return this.products.get(productID);
    }

    public Set<String> getIDs() {
        return this.products.keySet();
    }

    @PostConstruct
    private void initProducts() {
        for (int i = 0; i < 10; i++) {
            String serialNumber = "A" + i;
            this.products.put(serialNumber,
                    new Product(generateName(), generateCategory(), generatePrice(), serialNumber));
        }
        LOGGER.info("Created Products are: ");
        LOGGER.info(String.valueOf(this.products));
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
