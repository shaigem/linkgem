package com.github.shaigem.linkgem.ui.listeners;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.ui.dialog.bookmark.BookmarkEditorDialog;
import com.github.shaigem.linkgem.ui.dialog.folder.FolderEditorDialog;
import com.github.shaigem.linkgem.ui.events.OpenItemEditorDialogRequest;
import org.sejda.eventstudio.Listener;

/**
 * Listens for any requests to open the item editor dialog.
 *
 * @author Ronnie Tran
 */
public class ItemDialogListener implements Listener<OpenItemEditorDialogRequest> {

    @Override
    public void onEvent(OpenItemEditorDialogRequest event) {
        if (event.getWorkingItem() instanceof BookmarkItem) {
            new BookmarkEditorDialog().showDialog(event);
        } else if (event.getWorkingItem() instanceof FolderItem) {
            new FolderEditorDialog().showDialog(event);
        }
    }
}
