package com.github.shaigem.linkgem.gui.main.explorer.editor;

import com.github.shaigem.linkgem.gui.events.SelectedFolderChangedEvent;
import com.github.shaigem.linkgem.fx.propertysheet.PropertyEditorItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.PropertySheet;
import org.sejda.eventstudio.annotation.EventListener;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-28.
 */
public class EditorPresenter implements Initializable {

    @FXML
    PropertySheet propertySheet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener //TODO change this to use a listener for when a user selects a item in the folder explorer
    private void onSelectedFolderChanged(SelectedFolderChangedEvent event) {
        final FolderItem folder = event.getNewFolder();


        final PropertyEditorItem<String> nameItem = (new PropertyEditorItem<>
                ("", folder.nameProperty(), "Name", ""));

        // TODO create a custom popup editor for the description
        // purpose is to allow the description box to be expanded and so more text can be viewed
        // use a popover control?

        final PropertyEditorItem<String> descriptionItem = (new PropertyEditorItem<>
                ("", folder.descriptionProperty(), "Description", ""));

        propertySheet.getItems().setAll(nameItem, descriptionItem);

    }
}
