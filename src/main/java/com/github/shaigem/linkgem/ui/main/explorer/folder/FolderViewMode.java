package com.github.shaigem.linkgem.ui.main.explorer.folder;

import com.github.shaigem.linkgem.ui.main.explorer.folder.impl.TableFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.impl.TileFolderView;

/**
 * Created on 2016-12-27.
 */
public enum FolderViewMode {

    TABLE("Table", new TableFolderView()), GRID("Grid", new TileFolderView());

    private String name;
    private AbstractFolderView folderView;

    FolderViewMode(String name, AbstractFolderView folderView) {
        this.name = name;
        this.folderView = folderView;
    }

    public String getName() {
        return name;
    }

    public AbstractFolderView getFolderView() {
        return folderView;
    }
}
