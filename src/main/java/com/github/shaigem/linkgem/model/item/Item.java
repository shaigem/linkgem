package com.github.shaigem.linkgem.model.item;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created on 2016-12-21.
 */
public abstract class Item {

    private static final String DEFAULT_NAME = "<No Name>";

    private StringProperty name;
    private StringProperty description;
    private ObjectProperty<ItemType> itemType;

    public Item(final String name, final String description, final ItemType type) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.itemType = new SimpleObjectProperty<>(type);
    }

    public void setName(String name) {
        if(name.isEmpty()) {
            name = DEFAULT_NAME;
        }
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

    public ObjectProperty<ItemType> itemTypeProperty() {
        return itemType;
    }

    @Override
    public String toString() {
        return getName();
    }
}

