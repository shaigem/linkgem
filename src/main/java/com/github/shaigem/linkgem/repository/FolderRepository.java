package com.github.shaigem.linkgem.repository;

import com.github.shaigem.linkgem.model.item.FolderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created on 2016-12-25.
 */
public class FolderRepository {

    private final FolderItem rootFolder = new FolderItem("Bookmarks");

    private ObservableList<FolderItem> folderItems = FXCollections.observableArrayList();

    public ObservableList<FolderItem> getFolders() {
        return folderItems;
    }

    public FolderItem getRootFolder() {
        return rootFolder;
    }
}
