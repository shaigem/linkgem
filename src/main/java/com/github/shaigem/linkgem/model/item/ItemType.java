package com.github.shaigem.linkgem.model.item;

/**
 * Represents the item type. It can either be a folder or a bookmark.
 */
public enum ItemType {

    FOLDER("Folder"), BOOKMARK("Bookmark");

    private String name;

    ItemType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
