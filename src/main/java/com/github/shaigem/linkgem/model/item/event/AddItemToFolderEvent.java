package com.github.shaigem.linkgem.model.item.event;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;

import java.util.Optional;

/**
 * Created on 2016-12-27.
 */
public class AddItemToFolderEvent {

    private FolderItem folderToAddTo;
    private Item item;

    public AddItemToFolderEvent( FolderItem folderToAddTo, Item item) {
        this.folderToAddTo = folderToAddTo;
        this.item = item;

    }

    public FolderItem getFolderToAddTo() {
        return folderToAddTo;
    }
    public Optional<Item> getItemToAdd() {
        return Optional.ofNullable(item);
    }
}
