package com.github.shaigem.linkgem.ui.main;

import com.github.shaigem.linkgem.ui.main.explorer.FolderExplorerView;
import com.github.shaigem.linkgem.ui.main.explorer.editor.ItemEditorView;
import com.github.shaigem.linkgem.ui.main.browser.FolderBrowserView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MasterDetailPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created on 2016-12-21.
 */
public class MainWindowPresenter implements Initializable {

    @FXML
    StackPane itemSidebarPane;
    @FXML
    StackPane explorerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeItemSidebar();
        initializeExplorer();
    }

    @FXML
    private void onPlaceholderAction() {
    }

    private void initializeItemSidebar() {
        final FolderBrowserView itemSidebarView = new FolderBrowserView();
        itemSidebarPane.getChildren().add(itemSidebarView.getView());
    }

    private void initializeExplorer() {
        final MasterDetailPane masterDetailPane = new MasterDetailPane();
        final FolderExplorerView folderExplorerView = new FolderExplorerView();
        final ItemEditorView editorView = new ItemEditorView();
        masterDetailPane.setMasterNode(folderExplorerView.getView());
        masterDetailPane.setDetailNode(editorView.getView());
        masterDetailPane.setDetailSide(Side.BOTTOM);
        masterDetailPane.setShowDetailNode(true);
        explorerPane.getChildren().add(masterDetailPane);
    }
}
