package com.github.shaigem.linkgem.ui.main.explorer.folder.impl;

import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.ui.main.explorer.folder.AbstractFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.FolderViewMode;
import com.github.shaigem.linkgem.ui.main.explorer.tile.ItemTileView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.util.Collections;
import java.util.List;

/**
 * Created on 2016-12-28.
 */
public class TileFolderView extends AbstractFolderView {

    private GridView<Item> gridView;

    @Override
    public void createControl() {
        gridView = new GridView<>();
        gridView.setVerticalCellSpacing(20);
        gridView.setHorizontalCellSpacing(5);
        gridView.setCellHeight(150);
        gridView.setCellWidth(150);
        gridView.setCellFactory(gridView -> new TestGridCell());
        if (getViewingFolder() != null) {
            gridView.setItems(getViewingFolder().getChildren());
        }
    }

    private class TestGridCell extends GridCell<Item> {

        private ItemTileView tileView;

        TestGridCell() {
            tileView = new ItemTileView();

        }

        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                tileView.getController().updateItem(item);
                setGraphic(tileView);
            }
        }
    }

    @Override
    public void onFolderChanged() {
        gridView.setItems(getViewingFolder().getChildren());
    }

    @Override
    public void destroy() {
        gridView = null;
    }

    @Override
    public ToggleButton createToggleButton() {
        return iconToggleButton(FolderViewMode.GRID, MaterialDesignIcon.GRID);
    }

    @Override
    public Control getControl() {
        return gridView;
    }

    @Override
    public List<MenuItem> createSettings() {
        return Collections.emptyList();
    }
}
