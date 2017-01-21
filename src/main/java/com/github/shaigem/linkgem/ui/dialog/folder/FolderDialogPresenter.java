package com.github.shaigem.linkgem.ui.dialog.folder;

import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.ui.dialog.DialogBasedItemEditor;
import com.github.shaigem.linkgem.ui.events.AddItemToFolderEvent;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-30.
 */
public class FolderDialogPresenter implements Initializable, DialogBasedItemEditor {

    private FolderItem folder;
    private FolderItem editingFolder;
    private boolean add;

    @FXML
    TextField nameTextField;
    @FXML
    TextField descriptionTextField;

    @FXML
    Button okButton;
    @FXML
    Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // empty
    }

    @Override
    public void initProperties(OpenItemDialogRequest request) {
        this.folder = request.getWorkingFolder();
        this.editingFolder = (FolderItem) request.getWorkingItem();
        this.add = request.isAdd();
        nameTextField.setText(editingFolder.getName());
        descriptionTextField.setText(editingFolder.getDescription());
    }

    @Override
    public void saveProperties() {
        editingFolder.setName(nameTextField.getText());
        editingFolder.setDescription(descriptionTextField.getText());
        if (add) {
            eventStudio().broadcast(new AddItemToFolderEvent(folder, editingFolder));
        }
    }

    @Override
    public Button getOKButton() {
        return okButton;
    }

    @Override
    public Button getCancelButton() {
        return cancelButton;
    }
}
