package com.github.shaigem.linkgem.gui.main.explorer.folder;

import com.github.shaigem.linkgem.gui.main.explorer.folder.impl.TableFolderView;

/**
 * Created on 2016-12-27.
 */
public enum FolderViewMode {

    TABLE(new TableFolderView()), GRID(null);

    private AbstractFolderView folderView;

    FolderViewMode(AbstractFolderView folderView) {
        this.folderView = folderView;
    }

    public AbstractFolderView getFolderView() {
        return folderView;
    }
}
