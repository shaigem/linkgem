package com.github.shaigem.linkgem.ui.main.browser;

import com.github.shaigem.linkgem.fx.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.*;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
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
    StackPane toolbarPane;
    @FXML
    TreeView<FolderItem> folderTreeView;
    @FXML
    Button createButton;

    @FXML
    MenuItem addFolderMenuItem;
    @FXML
    MenuItem addBookmarkMenuItem;
    @FXML
    MenuItem editFolderMenuItem;
    @FXML
    MenuItem deleteFolderMenuItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeToolbar();
        setupCreateButton();
        TreeItem<FolderItem> rootFolder = new TreeItem<>();
        rootFolder.setExpanded(true);
        folderTreeView.setShowRoot(false);
        folderTreeView.setRoot(rootFolder);
        rootFolder.getChildren().add(folderRepository.getMasterFolder().getAsTreeItem());
        rootFolder.getChildren().add(folderRepository.getSearchFolder().getAsTreeItem());
        folderTreeView.setCellFactory((v) -> new CustomTreeCellImpl());
        listenForTreeViewSelection();
        folderTreeView.getSelectionModel().selectFirst();
        // folderTreeView.setContextMenu( new ItemContextMenu(this, folderRepository.getMasterFolder()));
        eventStudio().addAnnotatedListeners(this);
    }


    @FXML
    private void onEditFolderAction() {
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        eventStudio().broadcast(new OpenItemDialogRequest(selectedFolder,
                selectedFolder, false));
    }

    @FXML
    private void onAddFolderAction() {
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        eventStudio().broadcast(new OpenItemDialogRequest(selectedFolder,
                new FolderItem("New Folder"), true));
    }

    @FXML
    private void onAddBookmarkAction() {
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        eventStudio().broadcast(new OpenItemDialogRequest(selectedFolder,
                new BookmarkItem("New Bookmark"), true));
    }

    @FXML
    private void onDeleteFolderAction() {

    }

    private void initializeToolbar() {
        toolbarPane.getChildren().add(new ThemeTitledToolbar("Browser"));
    }

    private void setupCreateButton() {
        final Text icon = GlyphsDude.createIcon(MaterialIcon.CREATE_NEW_FOLDER, "1.6em");
        createButton.setGraphic(icon);
        createButton.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(folderRepository.getMasterFolder(),
                new FolderItem("New Folder"), true)));
    }

    private void listenForTreeViewSelection() {
        // 1. Selection on treeview changes
        // 2. broadcast event
        // 3. Change explorer view
        folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final boolean selectedFolderIsReadOnly = newValue.getValue().isReadOnly();
                addFolderMenuItem.setDisable(selectedFolderIsReadOnly);
                addBookmarkMenuItem.setDisable(selectedFolderIsReadOnly);
                editFolderMenuItem.setDisable(selectedFolderIsReadOnly);
                deleteFolderMenuItem.setDisable(selectedFolderIsReadOnly);
            }
            eventStudio().broadcast(new SelectedFolderChangedEvent(oldValue == null ? null :
                    oldValue.getValue(), newValue == null ? null : newValue.getValue()));
        });
    }

    @EventListener
    private void onDeleteFolderRequest(DeleteFolderRequest event) {
        final FolderItem itemToDelete = (FolderItem) event.getItemToDelete();
        folderRepository.getMasterFolder().getAsTreeItem().getChildren().remove(itemToDelete.getAsTreeItem());
        folderRepository.getMasterFolder().getChildren().remove(itemToDelete);
        // TODO master folder
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
                folderRepository.getFolders().add((FolderItem) itemToAdd);
                folderToAddTo.getAsTreeItem().getChildren().add(((FolderItem) itemToAdd).getAsTreeItem());
            }
        });
    }

    private final class CustomTreeCellImpl extends TreeCell<FolderItem> {

        private Text icon;

        CustomTreeCellImpl() {
            setGraphicTextGap(10);
        }

        @Override
        public void updateItem(FolderItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                //  setText(null);
                textProperty().unbind();
                setText(null);
                setGraphic(null);
            } else {
                if (item == folderRepository.getSearchFolder()) {
                    icon = GlyphsDude.createIcon(MaterialDesignIcon.MAGNIFY, "1.6em");
                } else {
                    icon = getTreeItem().isExpanded() && !getTreeItem().isLeaf() ?
                            GlyphsDude.createIcon(FontAwesomeIcon.FOLDER_OPEN, "1.6em")
                            : GlyphsDude.createIcon(FontAwesomeIcon.FOLDER, "1.6em");
                }
                // setText(item.getName());
                textProperty().bind(item.nameProperty());
                setGraphic(icon);
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
