package com.github.shaigem.linkgem.ui.events;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;

import java.util.Optional;

/**
 * Request for adding a item to a folder.
 *
 * @author Ronnie Tran
 */
public class AddItemToFolderRequest {

    private FolderItem folderToAddTo;
    private Item item;

    public AddItemToFolderRequest(FolderItem folderToAddTo, Item item) {
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
