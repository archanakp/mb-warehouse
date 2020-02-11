package com.battmobile.battmobilewarehouse.scrap;

public class ScrapModel {
    String id, product_id,sku, image, brand, size,retail_sale_price,quantity,date;

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getImage() {
        return image;
    }

    public String getBrand() {
        return brand;
    }

    public String getSize() {
        return size;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getRetail_sale_price() {
        return retail_sale_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public ScrapModel(String id, String product_id, String sku, String image, String brand, String size, String retail_sale_price, String quantity, String date) {
        this.id = id;
        this.product_id = product_id;
        this.sku = sku;
        this.image = image;
        this.brand = brand;
        this.size = size;
        this.retail_sale_price = retail_sale_price;
        this.quantity = quantity;
        this.date = date;
    }
}
