package com.github.shaigem.linkgem.ui.main.explorer;

import com.github.shaigem.linkgem.fx.CommonStyle;
import com.github.shaigem.linkgem.fx.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.SelectedFolderChangedEvent;
import com.github.shaigem.linkgem.ui.main.explorer.folder.AbstractFolderView;
import com.github.shaigem.linkgem.ui.main.explorer.folder.FolderViewMode;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.SegmentedButton;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-25.
 */
public class FolderExplorerPresenter implements Initializable {

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

        // load all toggle buttons to the right section
        final ToggleGroup toggleGroup = new ToggleGroup();
        final SegmentedButton viewSegmentedButton = new SegmentedButton();
        final ToggleButton tableToggleButton = FolderViewMode.TABLE.getFolderView().createToggleButton();
        final ToggleButton gridToggleButton = FolderViewMode.GRID.getFolderView().createToggleButton();
        tableToggleButton.setSelected(true);
        viewSegmentedButton.setToggleGroup(toggleGroup);
        viewSegmentedButton.getButtons().addAll(tableToggleButton, gridToggleButton);

        viewSettingsMenuButton = new MenuButton();
        viewSettingsMenuButton.setTooltip(new Tooltip("Change view-specific settings"));
        viewSettingsMenuButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.SETTINGS, "1.8em"));
        viewSettingsMenuButton.getStyleClass().addAll(CommonStyle.ICON_MENU_BUTTON.getStyleClasses());
        viewSettingsMenuButton.getItems().setAll(currentViewModeSetting.getFolderView().createSettings());

        toolbar.getRightSection().getChildren().addAll(viewSegmentedButton, viewSettingsMenuButton);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                Platform.runLater(() -> toggleGroup.selectToggle(oldValue));
            }
            if (newValue != null && oldValue != null) {
                updateViewModeSetting((FolderViewMode) newValue.getUserData());
            }
        });
        toolbarPane.getChildren().addAll(toolbar);
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
}
