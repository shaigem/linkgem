package com.github.shaigem.linkgem.model.item;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Created on 2016-12-21.
 */
public class FolderItem extends Item {

    private TreeItem<FolderItem> treeItem;

    private BooleanProperty readOnly;
    private ObservableList<Item> children;

    public FolderItem(String name, String description, boolean readOnly) {
        super(name, description, ItemType.FOLDER);
        this.readOnly = new SimpleBooleanProperty(readOnly);
        this.children = FXCollections.observableArrayList();
    }

    public FolderItem(String name) {
        this(name, "", false);
    }

    public boolean addItem(Item item) {
        return children.add(item);
    }

    public ObservableList<Item> getChildren() {
        return children;
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public TreeItem<FolderItem> getAsTreeItem() {
        if (treeItem == null) {
            treeItem = new TreeItem<>(this);
            treeItem.setExpanded(true);
        }
        return treeItem;
    }
}
