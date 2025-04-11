package model;

import java.sql.Timestamp;

public class Ticket {
    private int id;
    private int sessionId;
    private double price;
    private int quantity;
    private int userId;
    private String race; // 👈 Nouveau champ pour la race du chien

    private Timestamp addedDate;

    public Ticket(int id, int sessionId, double price,
                  int quantity, int userId, Timestamp addedDate) {
        this.id = id;
        this.sessionId = sessionId;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
        this.addedDate = addedDate;
    }

    // Surcharge pour inclure la race
    public Ticket(int id, int sessionId, double price,
                  int quantity, int userId, Timestamp addedDate, String race) {
        this(id, sessionId, price, quantity, userId, addedDate);
        this.race = race;
    }

    public int getId() { return id; }
    public int getSessionId() { return sessionId; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getUserId() { return userId; }
    public Timestamp getAddedDate() { return addedDate; }

    public String getRace() { return race; } // 👈 Getter
    public void setRace(String race) { this.race = race; } // 👈 Setter

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}