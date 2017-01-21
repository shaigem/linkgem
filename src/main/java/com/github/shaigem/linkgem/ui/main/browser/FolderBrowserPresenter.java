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
import java.util.Optional;
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
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Selected Folder");
        alert.setHeaderText("Delete Selected Folder");
        alert.setContentText("Are you sure you want to delete the selected folder?");
        final Optional<ButtonType> buttonType = alert.showAndWait();
        buttonType.ifPresent(type -> {
            if (type == ButtonType.OK) {
                onDeleteFolderRequest(new DeleteFolderRequest(selectedFolder));
            }
        });
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
                deleteFolderMenuItem.setDisable(selectedFolderIsReadOnly || newValue.getValue() == folderRepository.getMasterFolder());
            }
            eventStudio().broadcast(new SelectedFolderChangedEvent(oldValue == null ? null :
                    oldValue.getValue(), newValue == null ? null : newValue.getValue()));
        });
    }

    @EventListener
    private void onDeleteFolderRequest(DeleteFolderRequest event) {
        final FolderItem folderToDelete = (FolderItem) event.getItemToDelete();
        final FolderItem parentFolder = folderToDelete.getParentFolder();
        // remove the folder from the browser tree list
        parentFolder.getAsTreeItem().getChildren().remove(folderToDelete.getAsTreeItem());
        // the parent folder will remove the folder from its children list
        parentFolder.getChildren().remove(folderToDelete);

        //TODO disable deleting master folder!
    }

    @EventListener
    private void onOpenFolderRequest(OpenFolderRequest request) {
        folderTreeView.getSelectionModel().select(request.getFolder().getAsTreeItem());
    }

    /**
     * Listener which listens if there are any requests to add a item to a folder.
     *
     * @param request the request to listen for
     */
    @EventListener
    private void onAddItemToFolderRequest(AddItemToFolderRequest request) {
        final FolderItem folderToAddTo = request.getFolderToAddTo();
        request.getItemToAdd().ifPresent(itemToAdd -> {
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
