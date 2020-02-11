package com.battmobile.battmobilewarehouse.supplier;

public class SupplierModel {
    String id, name, email, mobile, company, address;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    public SupplierModel(String id, String name, String email, String mobile, String company, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.company = company;
        this.address = address;
    }
}
