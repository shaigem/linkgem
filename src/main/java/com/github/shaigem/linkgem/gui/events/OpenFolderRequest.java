package com.github.shaigem.linkgem.gui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;

/**
 * Created on 2016-12-28.
 */
public class OpenFolderRequest {

    private FolderItem folder;

    public OpenFolderRequest(FolderItem folder) {
        this.folder = folder;
    }

    public FolderItem getFolder() {
        return folder;
    }
}
