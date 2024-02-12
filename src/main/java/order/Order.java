package order;

import lombok.With;
import product.Product;

import java.math.BigDecimal;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        BigDecimal total,
        OrderStatus status
) {
}
