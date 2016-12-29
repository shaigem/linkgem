package com.github.shaigem.linkgem.gui.main.explorer.editor;

import com.github.shaigem.linkgem.fx.propertysheet.PropertyEditorItem;
import com.github.shaigem.linkgem.gui.events.ItemSelectionChangedEvent;
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
public class EditorPresenter implements Initializable {

    private static final String DEFAULT_CATEGORY = "Default";

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
            updatePropertySheetItems(item.get());
        } else {
            setPropertySheetVisibility(false);
        }
    }

    private void updatePropertySheetItems(Item item) {
        setPropertySheetVisibility(true);
        final PropertyEditorItem<String> nameItem = (new PropertyEditorItem<>
                (DEFAULT_CATEGORY, item.nameProperty(), "Name", "Stuff"));

        final PropertyEditorItem<String> descriptionItem = (new PropertyEditorItem<>
                (DEFAULT_CATEGORY, item.descriptionProperty(), "Description", ""));

        if (item instanceof BookmarkItem) {
            final BookmarkItem bookmarkItem = (BookmarkItem) item;
            final PropertyEditorItem<String> locationItem = (new PropertyEditorItem<>
                    (DEFAULT_CATEGORY, bookmarkItem.locationProperty(), "Link", ""));
            propertySheet.getItems().setAll(nameItem, locationItem, descriptionItem);
        } else {
            propertySheet.getItems().setAll(nameItem, descriptionItem);
        }
    }

    private void setPropertySheetVisibility(boolean show) {
        propertySheet.setVisible(show);
    }
}
