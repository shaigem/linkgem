package com.github.shaigem.linkgem.gui.main;

import com.github.shaigem.linkgem.gui.main.explorer.FolderExplorerView;
import com.github.shaigem.linkgem.gui.main.explorer.editor.EditorView;
import com.github.shaigem.linkgem.gui.main.sidebar.ItemBrowserView;
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

    private void initializeItemSidebar() {
        final ItemBrowserView itemSidebarView = new ItemBrowserView();
        itemSidebarPane.getChildren().add(itemSidebarView.getView());
    }

    private void initializeExplorer() {
        final MasterDetailPane masterDetailPane = new MasterDetailPane();
        final FolderExplorerView folderExplorerView = new FolderExplorerView();
        final EditorView editorView = new EditorView();
        masterDetailPane.setMasterNode(folderExplorerView.getView());
        masterDetailPane.setDetailNode(editorView.getView());
        masterDetailPane.setDetailSide(Side.BOTTOM);
        masterDetailPane.setShowDetailNode(true);
        explorerPane.getChildren().add(masterDetailPane);
    }
}
