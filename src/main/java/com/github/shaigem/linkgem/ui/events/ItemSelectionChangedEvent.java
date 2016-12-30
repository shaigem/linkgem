package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.Item;

import java.util.Optional;

/**
 * Created on 2016-12-29.
 */
public class ItemSelectionChangedEvent {

    private final Item selectedItem;

    public ItemSelectionChangedEvent(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public Optional<Item> getSelectedItem() {
        return Optional.ofNullable(selectedItem);
    }
}
