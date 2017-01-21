package com.github.shaigem.linkgem.ui.dialog;

import com.github.shaigem.linkgem.ui.events.OpenItemEditorDialogRequest;
import javafx.scene.control.Button;

/**
 * Basic interface which specifies what a dialog item editor should implement.
 *
 * @author Ronnie Tran
 */
public interface DialogBasedItemEditor {

    /**
     * Load all of the item's properties that are being edited.
     *
     * @param request the open item dialog request
     */
    void initProperties(final OpenItemEditorDialogRequest request);

    /**
     * Save any properties to the original items.
     */
    void saveProperties();

    Button getOKButton();

    Button getCancelButton();
}
