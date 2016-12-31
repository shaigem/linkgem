package com.github.shaigem.linkgem.ui.main.explorer.folder.impl;

import com.github.shaigem.linkgem.ui.main.explorer.folder.AbstractFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.FolderViewMode;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;

/**
 * Created on 2016-12-28.
 */
public class TileFolderView extends AbstractFolderView {


    @Override
    public void createControl() {
    }



    @Override
    public void onFolderChanged() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public ToggleButton getToggleButton() {
        final ToggleButton toggleButton = new ToggleButton("Tile");
        toggleButton.setUserData(FolderViewMode.GRID);
        return toggleButton;

    }

    @Override
    public Control getControl() {
        return null;
    }
}
