package com.web.leaftracker.model;

public class FactoryModel {
    int id;
    String factory_name;

    public FactoryModel(int id, String factory_name) {
        this.id = id;
        this.factory_name = factory_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public void setFactory_name(String factory_name) {
        this.factory_name = factory_name;
    }
}
