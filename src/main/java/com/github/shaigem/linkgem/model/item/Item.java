package com.github.shaigem.linkgem.model.item;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created on 2016-12-21.
 */
public abstract class Item {

    private StringProperty name;

    public Item(final String name) {
        this.name = new SimpleStringProperty(name);
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
