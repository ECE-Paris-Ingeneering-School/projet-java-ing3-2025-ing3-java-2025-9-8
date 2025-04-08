package model;

import java.sql.Timestamp;

public class Ticket {
    private int id;
    private int sessionId;
    private double price;
    private int quantity;
    private int userId;
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

    public int getId() { return id; }
    public int getSessionId() { return sessionId; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getUserId() { return userId; }
    public Timestamp getAddedDate() { return addedDate; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
