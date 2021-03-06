package com.github.shaigem.linkgem.repository;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Keeps track of all folders that are being used.
 *
 * @author Ronnie Tran
 */
public class FolderRepository {

    private final FolderItem masterFolder = new FolderItem("Bookmarks");

    private final FolderItem searchFolder = new FolderItem("Search", "Search results", true);

    private ObservableList<FolderItem> folderItems = FXCollections.observableArrayList();

    /**
     * Collects ALL of the items that the user has added in the program.
     *
     * @return the observable list of items
     */
    public ObservableList<Item> collectAllItems() {
        return collectItems(masterFolder);
    }

    /**
     * Collect items that are found in a folder.
     *
     * @param folderItem the folder with items that will be collected. If the folder contains more folders,
     *                   recursion will be used to search for more items in the folders.
     * @return the observable list with folders
     */
    private ObservableList<Item> collectItems(FolderItem folderItem) {
        ObservableList<Item> items = FXCollections.observableArrayList();
        for (Item item : folderItem.getChildren()) {
            if (item instanceof FolderItem) {
                items.add(item);
                items.addAll(collectItems((FolderItem) item));
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
