package com.github.shaigem.linkgem.ui.listeners;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.ui.dialog.bookmark.BookmarkDialog;
import com.github.shaigem.linkgem.ui.dialog.folder.FolderDialog;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import org.sejda.eventstudio.Listener;

/**
 * Created on 2016-12-30.
 */
public class ItemDialogListener implements Listener<OpenItemDialogRequest> {

    @Override
    public void onEvent(OpenItemDialogRequest event) {
        if (event.getWorkingItem() instanceof BookmarkItem) {
            new BookmarkDialog().showDialog(event);
        } else if (event.getWorkingItem() instanceof FolderItem) {
            new FolderDialog().showDialog(event);
        }
    }
}
