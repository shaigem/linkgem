package com.github.shaigem.linkgem.model.item;

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
