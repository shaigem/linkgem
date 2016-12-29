package com.github.shaigem.linkgem.model.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Created on 2016-12-21.
 */
public class FolderItem extends Item {

    private TreeItem<FolderItem> treeItem;
    private ObservableList<Item> children;

    public FolderItem(String name, String description) {
        super(name, description);
        this.children = FXCollections.observableArrayList();
    }

    public FolderItem(String name) {
        this(name, "");
    }

    public boolean addItem(Item item) {
        return children.add(item);
    }

    public ObservableList<Item> getChildren() {
        return children;
    }

    public TreeItem<FolderItem> getAsTreeItem() {
        if (treeItem == null) {
            treeItem = new TreeItem<>(this);
            treeItem.setExpanded(true);
        }
        return treeItem;
    }
}
