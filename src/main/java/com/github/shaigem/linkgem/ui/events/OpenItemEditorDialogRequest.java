package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;

/**
 * Request to open the item editor dialog.
 *
 * @author Ronnie Tran
 */
public class OpenItemEditorDialogRequest {

    private FolderItem workingFolder;
    private Item workingItem;
    /**
     * Are we editing the item or are we creating a new item to add.
     */
    private boolean add;

    public OpenItemEditorDialogRequest(FolderItem workingFolder, Item workingItem, boolean add) {
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
