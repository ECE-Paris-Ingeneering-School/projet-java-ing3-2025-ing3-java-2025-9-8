package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imagePath;
    private int stock;

    public Product(int id, String name, double price, String imagePath, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.stock = stock;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public int getStock() { return stock; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setStock(int stock) { this.stock = stock; }
}
