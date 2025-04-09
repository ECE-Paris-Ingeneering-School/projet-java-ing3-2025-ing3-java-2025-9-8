package model;

import java.sql.Timestamp;

public class OrderInfoSimple {
    private int orderId;
    private Timestamp orderDate;
    private double total;

    public OrderInfoSimple(int orderId, Timestamp orderDate, double total) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.total = total;
    }

    public int getOrderId() { return orderId; }
    public Timestamp getOrderDate() { return orderDate; }
    public double getTotal() { return total; }
}

