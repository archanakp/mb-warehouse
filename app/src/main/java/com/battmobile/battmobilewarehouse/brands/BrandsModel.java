package com.battmobile.battmobilewarehouse.brands;

public class BrandsModel {
    String id, name;
    boolean status;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public BrandsModel(String id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}
