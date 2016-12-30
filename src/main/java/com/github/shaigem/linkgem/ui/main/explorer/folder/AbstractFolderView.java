package com.github.shaigem.linkgem.ui.main.explorer.folder;

import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerPresenter;
import com.github.shaigem.linkgem.model.item.FolderItem;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;

/**
 * Created on 2016-12-27.
 */
public abstract class AbstractFolderView {

    private FolderExplorerPresenter folderExplorerPresenter;

    public abstract void createControl();

    public abstract void onFolderChanged();

    public abstract void destroy();

    public abstract ToggleButton getToggleButton();

    public abstract Control getControl();

    protected FolderItem getViewingFolder() {
        return getFolderExplorerPresenter().getViewingFolder();
    }

    public void setFolderExplorerPresenter(FolderExplorerPresenter folderExplorerPresenter) {
        this.folderExplorerPresenter = folderExplorerPresenter;
    }

    public FolderExplorerPresenter getFolderExplorerPresenter() {
        return folderExplorerPresenter;
    }
}
