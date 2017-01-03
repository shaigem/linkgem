package com.github.shaigem.linkgem.ui.main.explorer.folder.impl;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.model.item.ItemType;
import com.github.shaigem.linkgem.ui.events.ItemSelectionChangedEvent;
import com.github.shaigem.linkgem.ui.events.OpenFolderRequest;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import com.github.shaigem.linkgem.ui.main.explorer.folder.AbstractFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.FolderViewMode;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.*;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-27.
 */
public class TableFolderView extends AbstractFolderView {

    // TODO add options to hide certain columns?

    private TableView<Item> tableView;

    @Override
    public void createControl() {
        tableView = new TableView<>();
        final Label placeholder = new Label("Folder contains no items");
        tableView.setPlaceholder(placeholder);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setEditable(false);
        createColumns();
        tableView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Item rowData = row.getItem();
                    if (rowData instanceof FolderItem) {
                        eventStudio().broadcast(new OpenFolderRequest((FolderItem) rowData));
                    }

                }
            });
            return row;
        });
        if (getViewingFolder() != null) {
            tableView.setItems(getViewingFolder().getChildren());
            tableView.setContextMenu(createContextMenu());
        }
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                eventStudio().broadcast(new ItemSelectionChangedEvent(newValue)));
    }

    @Override
    public void onFolderChanged() {
        tableView.setItems(getViewingFolder().getChildren());
    }

    @Override
    public List<MenuItem> createSettings() {
        final List<MenuItem> menuItems = new ArrayList<>();
        for (TableColumn<Item, ?> itemTableColumn : tableView.getColumns()) {
            final String columnName = itemTableColumn.getText();
            final CheckMenuItem checkMenuItem = new CheckMenuItem
                    ("Show " + columnName + " Column");
            checkMenuItem.setSelected(true);
            itemTableColumn.visibleProperty().bindBidirectional(checkMenuItem.selectedProperty());
            if (columnName.equals("Name")) {
                // we must always show the name column so don't allow users to hide it
                checkMenuItem.setDisable(true);
            }
            menuItems.add(checkMenuItem);
        }
        return menuItems;
    }

    @Override
    public void destroy() {
        tableView = null;
    }

    @Override
    public Control getControl() {
        return tableView;
    }

    @Override
    public ToggleButton createToggleButton() {
        return iconToggleButton(FolderViewMode.TABLE, MaterialDesignIcon.TABLE);
    }

    private void createColumns() {
        final TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(e -> e.getValue().nameProperty());
        final TableColumn<Item, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(e -> {
            if (e.getValue() instanceof BookmarkItem) {
                final BookmarkItem bookmarkItem = (BookmarkItem) e.getValue();
                return bookmarkItem.locationProperty();
            }
            return null;
        });
        locationColumn.setCellFactory(column -> new TooltipTableCell());

        final TableColumn<Item, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(e -> e.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(column -> new TooltipTableCell());

        final TableColumn<Item, ItemType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(e -> e.getValue().itemTypeProperty());

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(locationColumn);
        tableView.getColumns().add(descriptionColumn);
        tableView.getColumns().add(typeColumn);
    }

    private ContextMenu createContextMenu() {
        // TODO a Add Item In action? Allows user to add a item inside a selected folder
        // TODO remove item action
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem newFolder = new MenuItem("Add Folder...");
        newFolder.setOnAction(event -> eventStudio().broadcast
                (new OpenItemDialogRequest(getViewingFolder(), new FolderItem("New Folder"), true)));

        final MenuItem newBookmark = new MenuItem("Add Bookmark...");
        newBookmark.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getViewingFolder(),
                new BookmarkItem("New Bookmark"), true)));
        contextMenu.getItems().addAll(newFolder, newBookmark);
        return contextMenu;
    }

    private final static class TooltipTableCell extends TableCell<Item, String> {

        final Tooltip tooltip;

        TooltipTableCell() {
            tooltip = new Tooltip();
            tooltip.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
            tooltip.setWrapText(true);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
            } else {
                setText(item);
                if (!item.isEmpty()) {
                    tooltip.setText(item);
                    setTooltip(tooltip);
                } else {
                    tooltip.setText("");
                    setTooltip(null);
                }
            }
        }
    }
}
