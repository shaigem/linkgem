package com.github.shaigem.linkgem.repository;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created on 2016-12-25.
 */
public class FolderRepository {

    private final FolderItem masterFolder = new FolderItem("Bookmarks");

    private final FolderItem searchFolder = new FolderItem("Search", "Search results", true);

    private ObservableList<FolderItem> folderItems = FXCollections.observableArrayList();

    public ObservableList<Item> getAllBookmarkItems(FolderItem folderItem) {
        ObservableList<Item> items = FXCollections.observableArrayList();
        for (Item item : folderItem.getChildren()) {
            if (item instanceof FolderItem) {
                items.add(item);
                items.addAll(getAllBookmarkItems((FolderItem) item));
            } else {
                items.add(item);
            }
        }
        return items;
    }


    public ObservableList<FolderItem> getFolders() {
        return folderItems;
    }

    public FolderItem getMasterFolder() {
        return masterFolder;
    }

    public FolderItem getSearchFolder() {
        return searchFolder;
    }
}
