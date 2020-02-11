package com.battmobile.battmobilewarehouse.product;

public class ProductModel {
    String id, sku, size, brand_id, brand_title, image;

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getSize() {
        return size;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getBrand_title() {
        return brand_title;
    }

    public String getImage() {
        return image;
    }

    public ProductModel(String id, String sku, String size, String brand_id, String brand_title, String image) {
        this.id = id;
        this.sku = sku;
        this.size = size;
        this.brand_id = brand_id;
        this.brand_title = brand_title;
        this.image = image;
    }
}
