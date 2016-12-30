package com.github.shaigem.linkgem.ui.dialog.bookmark;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.ui.dialog.DialogBasedItemEditor;
import com.github.shaigem.linkgem.ui.events.AddItemToFolderEvent;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-30.
 */
public class BookmarkDialogPresenter implements Initializable, DialogBasedItemEditor {

    private FolderItem folder;
    private BookmarkItem editingBookmark;
    private boolean add;

    @FXML
    TextField nameTextField;
    @FXML
    TextField locationTextField;
    @FXML
    TextArea descriptionTextArea;

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
        this.editingBookmark = (BookmarkItem) request.getWorkingItem();
        this.add = request.isAdd();
        nameTextField.setText(editingBookmark.getName());
        locationTextField.setText(editingBookmark.getLocation());
        descriptionTextArea.setText(editingBookmark.getDescription());
    }

    @Override
    public void saveProperties() {
        editingBookmark.setName(nameTextField.getText());
        editingBookmark.setLocation(locationTextField.getText());
        editingBookmark.setDescription(descriptionTextArea.getText());
        if (add) {
            eventStudio().broadcast(new AddItemToFolderEvent(folder, editingBookmark));
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
