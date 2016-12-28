package com.github.shaigem.linkgem.repository;

import com.github.shaigem.linkgem.model.item.FolderItem;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created on 2016-12-27.
 */
public class ViewingFolderManager {

    private SimpleObjectProperty<FolderItem> viewingFolder = new SimpleObjectProperty<>();

    public SimpleObjectProperty<FolderItem> viewingFolderProperty() {
        return viewingFolder;
    }

    public void setViewingFolder(FolderItem viewingFolder) {
        this.viewingFolder.set(viewingFolder);
    }

    public FolderItem getViewingFolder() {
        return viewingFolder.get();
    }
}
