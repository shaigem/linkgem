package com.github.shaigem.linkgem.ui.main.browser;

import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.AddItemToFolderEvent;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import com.github.shaigem.linkgem.ui.events.OpenFolderRequest;
import com.github.shaigem.linkgem.ui.events.SelectedFolderChangedEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-21.
 */
public class FolderBrowserPresenter implements Initializable {

    @Inject
    private FolderRepository folderRepository;


    @FXML
    TreeView<FolderItem> folderTreeView;

    private TreeItem<FolderItem> rootFolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootFolder = folderRepository.getRootFolder().getAsTreeItem();
        rootFolder.setExpanded(true);
        folderTreeView.setRoot(rootFolder);
        folderTreeView.setCellFactory((v) -> new CustomTreeCellImpl());
        listenForTreeViewSelection();
        folderTreeView.getSelectionModel().selectFirst();
        eventStudio().addAnnotatedListeners(this);
    }

    private void listenForTreeViewSelection() {
        // 1. Selection on treeview changes
        // 2. broadcast event
        // 3. Change explorer view
        folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                eventStudio().broadcast(new SelectedFolderChangedEvent(oldValue == null ? null :
                        oldValue.getValue(), newValue == null ? null : newValue.getValue())));
    }

    @EventListener
    private void onOpenFolderRequest(OpenFolderRequest request) {
        folderTreeView.getSelectionModel().select(request.getFolder().getAsTreeItem());
    }

    @EventListener
    private void onAddItemToFolder(AddItemToFolderEvent event) {
        final FolderItem folderToAddTo = event.getFolderToAddTo();
        event.getItemToAdd().ifPresent(itemToAdd -> {
            folderToAddTo.addItem(itemToAdd);
            if (itemToAdd instanceof FolderItem) {
                // TODO add item dialog
                folderRepository.getFolders().add((FolderItem) itemToAdd);
                folderToAddTo.getAsTreeItem().getChildren().add(((FolderItem) itemToAdd).getAsTreeItem());
            }
        });
    }

    private final class CustomTreeCellImpl extends TreeCell<FolderItem> {
        private ContextMenu menu;

        CustomTreeCellImpl() {
            createContextMenu();
        }

        @Override
        public void updateItem(FolderItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                //  setText(null);
                textProperty().unbind();
                setText(null);
                setGraphic(null);
                setContextMenu(null);
            } else {
                // setText(item.getName());
                textProperty().bind(item.nameProperty());
                setGraphic(getTreeItem().getGraphic());
                setContextMenu(menu);
            }
        }

        private void createContextMenu() {
            menu = new ContextMenu();
             // TODO remove item action
            final MenuItem editFolder = new MenuItem("Edit Folder...");
            editFolder.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getItem(), getItem(), false)));
            final SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

            final MenuItem newFolder = new MenuItem("Add Folder...");
            newFolder.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getItem(), new FolderItem("New Folder"), true)));

            final MenuItem newBookmark = new MenuItem("Add Bookmark...");
            newBookmark.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getItem(),
                    new BookmarkItem("New Bookmark"), true)));
            menu.getItems().addAll(editFolder, separatorMenuItem, newFolder, newBookmark);
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
