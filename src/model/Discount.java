package model;

import java.sql.Date;

public class Discount {
    private int id;
    private String name;            // Nom de la promotion
    private String description;     // Description
    private int minQuantity;        // Nombre minimum d'articles à acheter
    private String discountType;    // "gratuit" ou "pourcentage"
    private double discountAmount;  // Pour "gratuit": nombre d'articles offerts; pour "pourcentage": % de réduction
    private String targetCategory;  // "Produit" ou "Session"
    private int targetId;           // ID de l'article ou de la session visé(e)
    private Date startDate;         // Date de début de validité
    private Date endDate;           // Date de fin de validité

    public Discount(int id, String name, String description,
                    int minQuantity, String discountType, double discountAmount,
                    String targetCategory, int targetId, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minQuantity = minQuantity;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.targetCategory = targetCategory;
        this.targetId = targetId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    public String getTargetCategory() { return targetCategory; }
    public void setTargetCategory(String targetCategory) { this.targetCategory = targetCategory; }

    public int getTargetId() { return targetId; }
    public void setTargetId(int targetId) { this.targetId = targetId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
