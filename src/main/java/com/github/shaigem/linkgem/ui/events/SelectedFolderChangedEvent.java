package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;

/**
 * Created on 2016-12-27.
 */
public class SelectedFolderChangedEvent {

    private final FolderItem oldFolder;
    private final FolderItem newFolder;

    public SelectedFolderChangedEvent(FolderItem oldFolder, FolderItem newFolder) {
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
    }

    public FolderItem getOldFolder() {
        return oldFolder;
    }

    public FolderItem getNewFolder() {
        return newFolder;
    }
}
