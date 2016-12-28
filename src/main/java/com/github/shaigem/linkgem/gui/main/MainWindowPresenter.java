package com.github.shaigem.linkgem.gui.main;

import com.github.shaigem.linkgem.gui.main.explorer.FolderExplorerView;
import com.github.shaigem.linkgem.gui.main.sidebar.ItemBrowserView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;
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

    @Inject
    private FolderExplorerView folderExplorerView;

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
        explorerPane.getChildren().add(folderExplorerView.getView());
    }


}
