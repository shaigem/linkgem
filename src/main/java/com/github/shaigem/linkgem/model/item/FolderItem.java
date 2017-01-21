package com.github.shaigem.linkgem.model.item;

import com.github.shaigem.linkgem.favicon.IconManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Represents a folder item. A folder item acts like a folder. It can hold more folders and bookmarks.
 *
 * @author Ronnie Tran
 */
public class FolderItem extends Item {

    private TreeItem<FolderItem> treeItem;
    /**
     * Property indicating that the folder is readonly.
     * This means that users cannot add items to the folder but they can delete items in the folder.
     * However, the folder itself cannot be deleted.
     */
    private BooleanProperty readOnly;
    /**
     * Observable list that hold the folder's children.
     */
    private ObservableList<Item> children;

    public FolderItem(String name, String description, boolean readOnly) {
        super(IconManager.getInstance().getDefaultFolderIcon(), name, description, ItemType.FOLDER);
        this.readOnly = new SimpleBooleanProperty(readOnly);
        this.children = FXCollections.observableArrayList();
    }

    public FolderItem(String name) {
        this(name, "", false);
    }

    public boolean addItem(Item item) {
        final boolean added = children.add(item);
        if (added) {
            item.setParentFolder(this);
        }
        return added;
    }

    public boolean removeItem(Item item) {
        return children.remove(item);
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

    /**
     * Create a tree item for this folder.
     *
     * @return the tree item
     */
    public TreeItem<FolderItem> getAsTreeItem() {
        if (treeItem == null) {
            treeItem = new TreeItem<>(this);
            treeItem.setExpanded(true);
        }
        return treeItem;
    }
}
