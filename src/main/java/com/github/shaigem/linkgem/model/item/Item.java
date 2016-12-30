package com.github.shaigem.linkgem.model.item;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created on 2016-12-21.
 */
public abstract class Item {

    private StringProperty name;
    private StringProperty description;

    public Item(final String name, final String description) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }


    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }


    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String toString() {
        return getName();
    }
}

