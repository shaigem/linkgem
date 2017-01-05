package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.Item;

/**
 * Created on 2017-01-03.
 */
public class DeleteFolderRequest {

    private Item itemToDelete;
    public DeleteFolderRequest(Item itemToDelete) {
        this.itemToDelete = itemToDelete;
    }

    public Item getItemToDelete() {
        return itemToDelete;
    }
}
