package com.github.shaigem.linkgem.ui.main.explorer.editor;

import com.github.shaigem.linkgem.fx.propertysheet.ItemPropertyEditor;
import com.github.shaigem.linkgem.fx.propertysheet.PropertyEditorItem;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.PropertySheet;
import org.sejda.eventstudio.annotation.EventListener;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Presenter which handles the item editor. Allows users to edit the properties of a item.
 *
 * @author Ronnie Tran
 */
public class ItemEditorPresenter implements Initializable {

    private Item editingItem;

    @FXML
    PropertySheet propertySheet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventStudio().addAnnotatedListeners(this);
    }

    /**
     * Listens if there are any requests to edit a new item.
     *
     * @param event the request to listen for
     */
    @EventListener
    private void onChangeEditorItemRequest(ChangeEditorItemRequest event) {
        final Optional<Item> item = event.getItem();
        if (item.isPresent()) {
            setEditingItem(item.get());
        } else {
            // hide the property sheet
            setPropertySheetVisibility(false);
        }
    }

    /**
     * Updates the property sheet control with the editing item's properties.
     */
    private void updatePropertySheetItems() {
        setPropertySheetVisibility(true);

        final PropertyEditorItem<String> nameItem = (new PropertyEditorItem<>(editingItem.nameProperty(), "Name",
                "The name of the item",
                ItemPropertyEditor.NAME));

        final PropertyEditorItem<String> descriptionItem = (new PropertyEditorItem<>
                (editingItem.descriptionProperty(), "Description", "The description of the item",
                        ItemPropertyEditor.DEFAULT));

        if (editingItem instanceof BookmarkItem) {
            final BookmarkItem bookmarkItem = (BookmarkItem) editingItem;
            final PropertyEditorItem<String> locationItem = (new PropertyEditorItem<>
                    (bookmarkItem.locationProperty(), "Location", "The URL/location of the item",
                            ItemPropertyEditor.LOCATION));
            propertySheet.getItems().setAll(nameItem, locationItem, descriptionItem);
        } else {
            propertySheet.getItems().setAll(nameItem, descriptionItem);
        }
    }

    private void setPropertySheetVisibility(boolean show) {
        propertySheet.setVisible(show);
    }

    private void setEditingItem(Item item) {
        this.editingItem = item;
        editingItemDidChange();
    }

    private void editingItemDidChange() {
        updatePropertySheetItems();
    }
}
