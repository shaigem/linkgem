package com.github.shaigem.linkgem.gui.main.explorer.folder.impl;

import com.github.shaigem.linkgem.gui.main.explorer.OpenFolderRequest;
import com.github.shaigem.linkgem.gui.main.explorer.folder.AbstractFolderView;
import com.github.shaigem.linkgem.gui.main.explorer.folder.FolderViewMode;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import javafx.scene.control.*;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-27.
 */
public class TableFolderView extends AbstractFolderView {

    private TableView<Item> tableView;

    @Override
    public void createControl() {
        tableView = new TableView<>();
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
            tableView.setItems(getViewingFolder().getItems());
            tableView.setContextMenu(createContextMenu());
        }
    }

    @Override
    public void onFolderChanged() {
        tableView.setItems(getViewingFolder().getItems());
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
    public ToggleButton getToggleButton() {
        final ToggleButton toggleButton = new ToggleButton("Table");
        toggleButton.setUserData(FolderViewMode.TABLE);
        return toggleButton;
    }

    private void createColumns() {
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Item, String> locationColumn = new TableColumn<>("Location");
        nameColumn.setCellValueFactory(e -> e.getValue().nameProperty());
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(locationColumn);
    }

    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new ContextMenu();
        return contextMenu;
    }
}
