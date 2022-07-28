package com.adscreator.data;

import com.adscreator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findBySerialNumber(String serialNumber);
    List<Product> findProductsByCategory(String category);

    @Query("select table.serialNumber from Product table")
    Set<String> getAllIds();
}
