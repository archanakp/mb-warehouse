package com.battmobile.battmobilewarehouse.stock;

public class StockModel {
    String id, sku, brand, size, cost_price, quantity;

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getBrand() {
        return brand;
    }

    public String getSize() {
        return size;
    }

    public String getCost_price() {
        return cost_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public StockModel(String id, String sku, String brand, String size, String cost_price, String quantity) {
        this.id = id;
        this.sku = sku;
        this.brand = brand;
        this.size = size;
        this.cost_price = cost_price;
        this.quantity = quantity;
    }
}
