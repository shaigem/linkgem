package com.github.shaigem.linkgem.ui.main.explorer.editor;

import com.github.shaigem.linkgem.fx.propertysheet.ItemPropertyEditor;
import com.github.shaigem.linkgem.fx.propertysheet.PropertyEditorItem;
import com.github.shaigem.linkgem.ui.events.ItemSelectionChangedEvent;
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
 * Created on 2016-12-28.
 */
public class ItemEditorPresenter implements Initializable {

    private Item editingItem;

    @FXML
    PropertySheet propertySheet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    private void onItemSelectionChanged(ItemSelectionChangedEvent event) {
        final Optional<Item> item = event.getSelectedItem();
        if (item.isPresent()) {
            setEditingItem(item.get());
        } else {
            setPropertySheetVisibility(false);
        }
    }

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
