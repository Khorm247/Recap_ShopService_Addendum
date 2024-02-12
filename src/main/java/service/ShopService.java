package service;

import order.Order;
import order.OrderMapRepo;
import order.OrderRepo;
import order.OrderStatus;
import product.Product;
import product.ProductRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopService {

    // Fields
    private final ProductRepo productRepo = new ProductRepo();
    private final OrderRepo orderRepo = new OrderMapRepo();
    private final IdService idService = new IdService();

    // Getters
    public List<Product> getProducts() {
        return productRepo.getProducts();
    }
    public Optional<Product> getProduct(String id) {
        return productRepo.getProductById(id);
    }
    public Order getOrder(String id) {
        return orderRepo.getOrderById(id);
    }
    public List<Order> getAllOrders() {
        return orderRepo.getOrders();
    }


    public List<Order> getAllOrdersByStatus(OrderStatus orderStatus){
        return orderRepo.getOrders().stream()
                .filter(order -> order.status().equals(orderStatus))
                .collect(Collectors.toList());
        // ToDo: Coding: Order Status
    }

    // #############################################################################################
    // Methods
    // #############################################################################################

    // ADD METHODS
    public Product addProduct(Product newProduct) {
        return productRepo.addProduct(newProduct);
    }
    public void addProducts(List<Product> newProducts) {
        productRepo.addProducts(newProducts);
    }
    public Order addOrder(List<String> productIds) {

        // ToDo: Coding: Exceptions
        //  Modify the 'addOrder' method in the ShopService
        //  so that an exception is thrown if the product does not exist.
        try{
            if (verifyAndReduceProductStock(productIds))
                throw new NoSuchElementException("product does not exist");
        } catch (NoSuchElementException e){
            System.out.println("Product does not exist");
            e.printStackTrace();
        }


        List<Product> products = retrieveProducts(productIds);

        final BigDecimal totalPrice = calculateTotalPrice(productIds);
        final String orderId = idService.generateId();
        final Order newOrder = new Order(orderId, products, totalPrice, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }
    public void addProductsFromCsv(String filePath) {
        List<Product> products = CsvService.readProductsFromCsv(filePath);
        productRepo.addProducts(products);
    }

    // REMOVE METHODS
    public void removeProduct(String id) {
        productRepo.removeProduct(id);
    }
    public void removeOrder(String id) {
        orderRepo.removeOrder(id);
    }


    // #############################################################################################
    // PRIVATE METHODS
    // #############################################################################################

    // PRODUCT METHODS
    private boolean isProductAvailable(String productId) {
        // ToDo: Tests needed here
        return getProduct(productId).filter(value -> value.stock() > 0).isPresent();
    }
    private boolean verifyAndReduceProductStock(List<String> productIds) {
        for (String productId : productIds) {
            if (!isProductAvailable(productId)) {
                System.out.println("Product with id " + productId + " is not available");
                return true;
            }
            reduceProductStock(productId);
        }
        return false;
    }
    private List<Product> retrieveProducts(List<String> productIds) {
        final List<Product> products = new ArrayList<>();

        for (String productId : productIds) {
            Optional<Product> product = getProduct(productId);
            if (product.isPresent()) {
                products.add(product.get());
            } else {
                System.out.println("Product with id " + productId + " not found");
            }
        }
        return products;
    }
    private void reduceProductStock(String productId) {
        Optional<Product> product = getProduct(productId);
        if(product.isPresent()){
            Product actualProduct = product.get();
            Product updatedProduct = new Product(actualProduct.id(), actualProduct.name(), actualProduct.price(), actualProduct.stock() - 1);
            productRepo.addProduct(updatedProduct);
        }
    }

    // ORDER METHODS
    private BigDecimal calculateTotalPrice(List<String> productIds) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<Product> products = retrieveProducts(productIds);

        for (Product product : products) {
            totalPrice = totalPrice.add(product.price());
        }
        return totalPrice;
    }
}
