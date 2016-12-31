package com.github.shaigem.linkgem.ui.main.explorer.folder;

import com.github.shaigem.linkgem.ui.main.explorer.folder.impl.TableFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.impl.TileFolderView;

/**
 * Created on 2016-12-27.
 */
public enum FolderViewMode {

    TABLE(new TableFolderView()), GRID(new TileFolderView());

    private AbstractFolderView folderView;

    FolderViewMode(AbstractFolderView folderView) {
        this.folderView = folderView;
    }

    public AbstractFolderView getFolderView() {
        return folderView;
    }
}
