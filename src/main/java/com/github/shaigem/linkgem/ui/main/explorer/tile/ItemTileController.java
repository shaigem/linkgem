package com.github.shaigem.linkgem.ui.main.explorer.tile;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.Item;

/**
 * Created on 2016-12-31.
 */
public class ItemTileController {

    private ItemTileView parentView;
    private Item item;

    public ItemTileController(ItemTileView parentView) {
        this.parentView = parentView;
    }

    private void itemDidChange() {
        parentView.getNameLabel().setText(item.getName());
        parentView.getDescriptionLabel().setText(item.getDescription().isEmpty() ? "No description" : item.getDescription());
        if (item instanceof BookmarkItem) {
            parentView.getLocationLabel().setText(((BookmarkItem) item).getLocation());
        }
        parentView.setBackgroundFill(item.getBackgroundColor());
    }

    public void updateItem(Item item) {
        this.item = item;
        itemDidChange();
    }


}
