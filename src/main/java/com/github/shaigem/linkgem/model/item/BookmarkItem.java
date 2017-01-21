package com.github.shaigem.linkgem.model.item;

import com.github.shaigem.linkgem.favicon.IconManager;
import com.github.shaigem.linkgem.util.LocationUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a bookmark item.
 *
 * @author Ronnie Tran
 */
public class BookmarkItem extends Item {

    private StringProperty location;

    public BookmarkItem(String name, String description, String location) {
        super(IconManager.getInstance().getDefaultBookmarkIcon(), name, description, ItemType.BOOKMARK);
        this.location = new SimpleStringProperty(location);
        if (!location.isEmpty()) {
            // fetch the icon and then set the bookmark's icon when it is successfully fetched.
            IconManager.getInstance().getIconForLocation(location).thenAcceptAsync(this::setIcon);
        }
        this.location.addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                // when the location or URL changes, we fetch the icon again
                IconManager.getInstance().getIconForLocation(newValue).thenAcceptAsync(this::setIcon);
            }
        });
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
