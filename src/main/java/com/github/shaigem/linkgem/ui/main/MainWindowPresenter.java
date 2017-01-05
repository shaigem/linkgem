package com.github.shaigem.linkgem.ui.main;

import com.github.shaigem.linkgem.fx.MainToolbar;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.SearchItemRequest;
import com.github.shaigem.linkgem.ui.events.SelectedFolderChangedEvent;
import com.github.shaigem.linkgem.ui.listeners.ItemDialogListener;
import com.github.shaigem.linkgem.ui.main.browser.FolderBrowserView;
import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerPresenter;
import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerView;
import com.github.shaigem.linkgem.ui.main.explorer.editor.ItemEditorView;
import com.github.shaigem.linkgem.util.TooltipUtil;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.MasterDetailPane;
import org.sejda.eventstudio.ReferenceStrength;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-21.
 */
public class MainWindowPresenter implements Initializable {

    @Inject
    private FolderRepository folderRepository;

    @FXML
    VBox root;
    @FXML
    StackPane itemSidebarPane;
    @FXML
    StackPane explorerPane;
    @FXML
    StackPane toolbarPane;

    private final PauseTransition searchTransition = new PauseTransition(Duration.millis(600));

    private MainToolbar toolbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeToolbar();
        initializeItemSidebar();
        initializeExplorer();
        TooltipUtil.changeDefaultTooltipActivationDuration();
        eventStudio().add(new ItemDialogListener(), 0, ReferenceStrength.STRONG);
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener(priority = 100)
    private void onSelectedFolderChanged(SelectedFolderChangedEvent event) {
        FolderItem viewingFolder = event.getNewFolder();
        if (viewingFolder != null) {
            // TODO also disable context menu for search folder
            if (viewingFolder != folderRepository.getSearchFolder()) {
                resetSearch();
            }
        }
    }

    private void initializeToolbar() {
        toolbar = new MainToolbar();
        toolbar.getSearchPresenter().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchTransition.playFromStart();
                searchTransition.setOnFinished(event -> eventStudio().broadcast(new SearchItemRequest(newValue)));
            }
        });
        toolbarPane.getChildren().add(toolbar);
    }

    private void initializeItemSidebar() {
        final FolderBrowserView itemSidebarView = new FolderBrowserView();
        itemSidebarPane.getChildren().add(itemSidebarView.getView());
    }

    private void initializeExplorer() {
        final MasterDetailPane masterDetailPane = new MasterDetailPane();
        final FolderExplorerView folderExplorerView = new FolderExplorerView();
        final FolderExplorerPresenter explorerPresenter = (FolderExplorerPresenter) folderExplorerView.getPresenter();
        explorerPresenter.setMainWindowPresenter(this);
        final ItemEditorView editorView = new ItemEditorView();
        masterDetailPane.setMasterNode(folderExplorerView.getView());
        masterDetailPane.setDetailNode(editorView.getView());
        masterDetailPane.setDetailSide(Side.BOTTOM);
        masterDetailPane.setShowDetailNode(true);
        explorerPane.getChildren().add(masterDetailPane);
    }

    private void resetSearch() {
        toolbar.getSearchPresenter().resetText();
        folderRepository.getSearchFolder().getChildren().clear();
        searchTransition.stop();
    }

    public MainToolbar getToolbar() {
        return toolbar;
    }
}
