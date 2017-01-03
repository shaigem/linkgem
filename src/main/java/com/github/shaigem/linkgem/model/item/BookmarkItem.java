package com.github.shaigem.linkgem.model.item;

import com.github.shaigem.linkgem.util.LocationUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created on 2016-12-21.
 */
public class BookmarkItem extends Item {

    private StringProperty location;

    public BookmarkItem(String name, String description, String location) {
        super(name, description, ItemType.BOOKMARK);
        this.location = new SimpleStringProperty(location);
    }

    public BookmarkItem(String name) {
        this(name, "", "");
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(LocationUtil.addHttp(location));
    }

    public String getLocation() {
        return location.get();
    }

}
