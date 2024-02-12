package product;

import java.util.*;

public class ProductRepo {
    private final Map<String, Product> products;

    public ProductRepo() {
        products = new HashMap<>();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public Product addProduct(Product newProduct) {
        products.put(newProduct.id(), newProduct);
        return newProduct;
    }

    public void addProducts(List<Product> newProducts) {
        for (Product product : newProducts) {
            products.put(product.id(), product);
        }
    }

    public void removeProduct(String id) {
        products.remove(id);
    }
}
