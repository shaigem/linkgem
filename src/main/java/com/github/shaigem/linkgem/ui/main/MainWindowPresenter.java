package com.github.shaigem.linkgem.ui.main;

import com.github.shaigem.linkgem.fx.MainToolbar;
import com.github.shaigem.linkgem.ui.listeners.ItemDialogListener;
import com.github.shaigem.linkgem.ui.main.browser.FolderBrowserView;
import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerPresenter;
import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerView;
import com.github.shaigem.linkgem.ui.main.explorer.editor.ItemEditorView;
import com.github.shaigem.linkgem.util.TooltipUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.MasterDetailPane;
import org.sejda.eventstudio.ReferenceStrength;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-21.
 */
public class MainWindowPresenter implements Initializable {

    @FXML
    VBox root;
    @FXML
    StackPane itemSidebarPane;
    @FXML
    StackPane explorerPane;
    @FXML
    StackPane toolbarPane;

    private MainToolbar toolbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeToolbar();
        initializeItemSidebar();
        initializeExplorer();
        TooltipUtil.changeDefaultTooltipActivationDuration();
        eventStudio().add(new ItemDialogListener(), 0, ReferenceStrength.STRONG);

    }

    private void initializeToolbar() {
   toolbar = new MainToolbar();
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

    public MainToolbar getToolbar() {
        return toolbar;
    }
}
