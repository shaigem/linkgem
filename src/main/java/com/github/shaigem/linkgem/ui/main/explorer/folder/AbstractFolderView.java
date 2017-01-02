package com.github.shaigem.linkgem.ui.main.explorer.folder;

import com.github.shaigem.linkgem.fx.CommonStyle;
import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerPresenter;
import com.github.shaigem.linkgem.model.item.FolderItem;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Created on 2016-12-27.
 */
public abstract class AbstractFolderView {

    private FolderExplorerPresenter folderExplorerPresenter;

    public abstract void createControl();

    public abstract void onFolderChanged();

    public abstract void destroy();

    public abstract ToggleButton createToggleButton();

    public abstract Control getControl();

    protected ToggleButton iconToggleButton(FolderViewMode folderViewMode, MaterialDesignIcon icon) {
        final ToggleButton toggleButton = new ToggleButton();
        Text iconText = GlyphsDude.createIcon(icon, "1.8em");
        iconText.setFill(Color.web("#B9B9B9")); // gray
        toggleButton.setGraphic(iconText);
        toggleButton.setUserData(folderViewMode);
        toggleButton.getStyleClass().addAll(CommonStyle.TOGGLE_BUTTON.getStyleClasses());
        toggleButton.setTooltip(new Tooltip(folderViewMode.getName() + " mode"));
        return toggleButton;
    }


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
