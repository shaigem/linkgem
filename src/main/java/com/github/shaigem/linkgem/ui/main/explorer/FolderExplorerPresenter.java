package com.github.shaigem.linkgem.ui.main.explorer;

import com.github.shaigem.linkgem.fx.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import com.github.shaigem.linkgem.ui.events.SelectedFolderChangedEvent;
import com.github.shaigem.linkgem.ui.main.explorer.folder.AbstractFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.FolderViewMode;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-25.
 */
public class FolderExplorerPresenter implements Initializable {

    public enum ExplorerAction {
        ADD_FOLDER, ADD_BOOKMARK;
    }

    private FolderViewMode currentViewModeSetting;

    private FolderItem viewingFolder;

    @Inject
    private FolderRepository folderRepository;

    @FXML
    StackPane toolbarPane;

    @FXML
    StackPane itemsView;

    private ThemeTitledToolbar toolbar;
    private MenuButton viewSettingsMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewingFolder = folderRepository.getRootFolder();
        updateViewModeSetting(FolderViewMode.TABLE);
        initToolbar();
        eventStudio().addAnnotatedListeners(this);
    }

    public void performAction(ExplorerAction action) {
        switch (action) {
            case ADD_BOOKMARK:
                performAddBookmark();
                break;
            case ADD_FOLDER:
                performAddFolder();
                break;
        }
    }

    private void performAddBookmark() {
        eventStudio().broadcast
                (new OpenItemDialogRequest(getViewingFolder(), new BookmarkItem("New Bookmark"), true));
    }

    private void performAddFolder() {
        eventStudio().broadcast
                (new OpenItemDialogRequest(getViewingFolder(), new FolderItem("New Folder"), true));
    }
    
    @EventListener
    private void onSelectedFolderChanged(SelectedFolderChangedEvent event) {
        viewingFolder = event.getNewFolder();
        if (viewingFolder != null) {
            currentViewModeSetting.getFolderView().onFolderChanged();
        }
        toolbar.getTitleLabel().textProperty().bind(viewingFolder.nameProperty());
        System.out.println("Selected Folder Changed!");
    }

    private void initToolbar() {
        toolbar = new ThemeTitledToolbar("Explorer");
        createRightSectionToolbarItems();
        toolbarPane.getChildren().addAll(toolbar);
    }

    private void createRightSectionToolbarItems() {
        final ToggleButton viewToggleButton = createViewToggleButtons();
        createViewSettingsMenu();
        final Button deleteButton = new Button();
        deleteButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.DELETE, "1.8em"));

        final Button addBookmarkButton = new Button();
        addBookmarkButton.setTooltip(new Tooltip("Add a new bookmark"));
        addBookmarkButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.BOOKMARK_PLUS, "1.8em"));
        addBookmarkButton.setOnAction(event -> performAction(ExplorerAction.ADD_BOOKMARK));

        final Button addFolderButton = new Button();
        addFolderButton.setTooltip(new Tooltip("Add a new folder"));
        addFolderButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.FOLDER_PLUS, "1.8em"));
        addFolderButton.setOnAction(event -> performAction(ExplorerAction.ADD_FOLDER));

        toolbar.getRightSection().getChildren().addAll(addFolderButton, addBookmarkButton, deleteButton, viewToggleButton, viewSettingsMenuButton);
    }

    private ToggleButton createViewToggleButtons() {
        final ViewToggleButton toggleButton =
                new ViewToggleButton();
        toggleButton.setSelected(true);
        return toggleButton;
    }

    private void createViewSettingsMenu() {
        viewSettingsMenuButton = new MenuButton();
        viewSettingsMenuButton.setTooltip(new Tooltip("Change view-specific settings"));
        viewSettingsMenuButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.SETTINGS, "1.8em"));
        viewSettingsMenuButton.getItems().setAll(currentViewModeSetting.getFolderView().createSettings());
    }

    private void updateViewModeSetting(FolderViewMode viewSetting) {
        if (this.currentViewModeSetting != viewSetting) {
            final FolderViewMode oldFolderViewMode = this.currentViewModeSetting;
            if (oldFolderViewMode != null) {
                oldFolderViewMode.getFolderView().destroy();
            }
            this.currentViewModeSetting = viewSetting;
            System.out.println("View Mode Update: " + this.currentViewModeSetting);
            viewModeSettingDidChange();
        }
    }

    private void viewModeSettingDidChange() {
        final AbstractFolderView folderView = currentViewModeSetting.getFolderView();
        if (folderView.getFolderExplorerPresenter() == null) {
            folderView.setFolderExplorerPresenter(this);
        }
        folderView.createControl();
        if (viewSettingsMenuButton != null) {
            viewSettingsMenuButton.getItems().setAll(folderView.createSettings());
        }
        itemsView.getChildren().setAll(folderView.getControl());
    }


    public FolderItem getViewingFolder() {
        return viewingFolder;
    }

    private class ViewToggleButton extends ToggleButton {

        private final Text listIcon = GlyphsDude.createIcon(MaterialDesignIcon.VIEW_LIST, "1.8em");

        private final Text gridIcon = GlyphsDude.createIcon(MaterialDesignIcon.VIEW_GRID, "1.8em");

        ViewToggleButton() {
            super();
            updateGraphic(listIcon);
            selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && oldValue != null) {
                    updateGraphic(newValue ? gridIcon : listIcon);
                    updateViewModeSetting(newValue ? FolderViewMode.TABLE : FolderViewMode.GRID);
                }
            });
        }

        private void updateGraphic(Node graphic) {
            if (getTooltip() == null) {
                setTooltip(new Tooltip("Toggle List mode"));
            }
            setGraphic(graphic);
            getTooltip().setText("Toggle " + (graphic == listIcon ? "List" : "Grid") + " mode");
        }
    }

}

