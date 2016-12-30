package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;

/**
 * Created on 2016-12-30.
 */
public class OpenItemDialogRequest {


    private FolderItem workingFolder;
    private Item workingItem;
    private boolean add;

    public OpenItemDialogRequest(FolderItem workingFolder, Item workingItem, boolean add) {
        this.workingFolder = workingFolder;
        this.workingItem = workingItem;
        this.add = add;
    }


    public FolderItem getWorkingFolder() {
        return workingFolder;
    }

    public Item getWorkingItem() {
        return workingItem;
    }


    public boolean isAdd() {
        return add;
    }
}
