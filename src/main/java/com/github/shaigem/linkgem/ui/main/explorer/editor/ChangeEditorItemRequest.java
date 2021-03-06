package com.github.shaigem.linkgem.ui.main.explorer.editor;

import com.github.shaigem.linkgem.model.item.Item;

import java.util.Optional;

/**
 * Request to change the item that is being edited in the item editor found in the explorer view.
 *
 * @author Ronnie Tran
 */
public class ChangeEditorItemRequest {

    private final Item item;

    public ChangeEditorItemRequest(Item item) {
        this.item = item;
    }

    public Optional<Item> getItem() {
        return Optional.ofNullable(item);
    }
}
