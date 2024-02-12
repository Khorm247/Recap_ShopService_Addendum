package order;

import lombok.With;
import product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        BigDecimal total,
        OrderStatus status,
        Instant orderDate
) {
}
