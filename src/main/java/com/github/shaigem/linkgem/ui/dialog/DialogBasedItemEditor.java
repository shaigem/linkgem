package com.github.shaigem.linkgem.ui.dialog;

import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import javafx.scene.control.Button;

/**
 * Created on 2016-12-30.
 */
public interface DialogBasedItemEditor {

    void initProperties(final OpenItemDialogRequest request);

    void saveProperties();

    Button getOKButton();

    Button getCancelButton();
}
