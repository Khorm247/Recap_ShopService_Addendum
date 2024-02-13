package service;

import order.Order;
import order.OrderStatus;
import org.junit.jupiter.api.Test;
import product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void getOldestOrderPerStatus() {
    }

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        shopService.addProductsFromCsv("src/main/resources/products.csv");
        // 1,Milch,3.75,469
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("1", List.of(new Product("1", "Milch", new BigDecimal(3.75), 468)), new BigDecimal(3.75), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }
}