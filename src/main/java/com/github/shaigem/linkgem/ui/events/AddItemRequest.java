package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.ItemType;

/**
 * Created on 2016-12-29.
 */
public class AddItemRequest {

    private FolderItem toFolder;
    private ItemType itemType;

    public AddItemRequest(FolderItem toFolder, ItemType itemType) {
        this.toFolder = toFolder;
        this.itemType = itemType;
    }

    public FolderItem getToFolder() {
        return toFolder;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
