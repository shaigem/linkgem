package com.github.shaigem.linkgem.model.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Created on 2016-12-21.
 */
public class FolderItem extends Item {

    private TreeItem<FolderItem> treeItem;
    private ObservableList<Item> items;

    public FolderItem(String name) {
        super(name);
        this.items = FXCollections.observableArrayList();
    }

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public TreeItem<FolderItem> getAsTreeItem() {
        if(treeItem == null) {
            treeItem = new TreeItem<>(this);
            treeItem.setExpanded(true);
        }
        return treeItem;
    }
}
