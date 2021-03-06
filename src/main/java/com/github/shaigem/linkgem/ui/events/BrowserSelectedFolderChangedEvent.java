package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;

/**
 * Event which notifies listeners that the user has selected another folder in the browser view.
 */
public class BrowserSelectedFolderChangedEvent {

    private final FolderItem oldFolder;
    private final FolderItem newFolder;

    public BrowserSelectedFolderChangedEvent(FolderItem oldFolder, FolderItem newFolder) {
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
