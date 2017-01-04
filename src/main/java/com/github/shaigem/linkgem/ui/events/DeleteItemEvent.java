package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.Item;

/**
 * Created on 2017-01-03.
 */
public class DeleteItemEvent {

    private Item deletedItem;
    public DeleteItemEvent(Item deletedItem) {
        this.deletedItem = deletedItem;
    }

    public Item getDeletedItem() {
        return deletedItem;
    }
}
