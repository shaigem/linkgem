package com.github.shaigem.linkgem.ui.main.browser;

import com.github.shaigem.linkgem.dnd.ItemDragAndDropManager;
import com.github.shaigem.linkgem.fx.toolbar.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.*;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Presenter for the folder browser. The folder browser allows users to browser folders.
 * It is displayed in a tree like structure.
 */
public class FolderBrowserPresenter implements Initializable {

    @Inject
    private FolderRepository folderRepository;
    @Inject
    private ItemDragAndDropManager dragAndDropManager;

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
        setupCreateNewFolderButton();
        TreeItem<FolderItem> rootFolder = new TreeItem<>();
        rootFolder.setExpanded(true);
        folderTreeView.setShowRoot(false);
        folderTreeView.setRoot(rootFolder);
        rootFolder.getChildren().add(folderRepository.getMasterFolder().getAsTreeItem());
        rootFolder.getChildren().add(folderRepository.getSearchFolder().getAsTreeItem());
        folderTreeView.setCellFactory((v) -> new CustomTreeCellImpl());
        listenForTreeViewSelection();
        folderTreeView.getSelectionModel().selectFirst();
        eventStudio().addAnnotatedListeners(this);
    }


    @FXML
    private void onEditFolderAction() {
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        eventStudio().broadcast(new OpenItemEditorDialogRequest(selectedFolder,
                selectedFolder, false));
    }

    @FXML
    private void onAddFolderAction() {
        // creates a new folder and adds it to the selected folder
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        eventStudio().broadcast(new OpenItemEditorDialogRequest(selectedFolder,
                new FolderItem("New Folder"), true));
    }

    @FXML
    private void onAddBookmarkAction() {
        // creates a new bookmark and adds it to the selected folder
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        eventStudio().broadcast(new OpenItemEditorDialogRequest(selectedFolder,
                new BookmarkItem("New Bookmark"), true));
    }

    /**
     * Deletes the selected folder.
     */
    @FXML
    private void onDeleteFolderAction() {
        final FolderItem selectedFolder = folderTreeView.getSelectionModel().getSelectedItem().getValue();
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Selected Folder");
        alert.setHeaderText("Delete Folder: " + selectedFolder);
        alert.setContentText("Are you sure you want to delete the selected folder? This will delete ALL of the folder's items.");
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

    private void setupCreateNewFolderButton() {
        final Text icon = GlyphsDude.createIcon(MaterialIcon.CREATE_NEW_FOLDER, "1.6em");
        createButton.setGraphic(icon);
        createButton.setOnAction(event -> eventStudio().broadcast(new OpenItemEditorDialogRequest(folderRepository.getMasterFolder(),
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
            eventStudio().broadcast(new BrowserSelectedFolderChangedEvent(oldValue == null ? null :
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
    }

    /**
     * Handles when there is a request to open a folder in the explorer.
     *
     * @param request the open in folder explorer request
     */
    @EventListener
    private void onOpenFolderInExplorerRequest(OpenFolderInExplorerRequest request) {
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
            if (itemToAdd.getParentFolder() != null) {
                // if the item already exists in the browser tree view, remove it!
                if (itemToAdd instanceof FolderItem) {
                    itemToAdd.getParentFolder().getAsTreeItem().getChildren().remove
                            (((FolderItem) itemToAdd).getAsTreeItem());
                }
            }
            folderToAddTo.addItem(itemToAdd);
            if (itemToAdd instanceof FolderItem) {
                folderRepository.getFolders().add((FolderItem) itemToAdd);
                folderToAddTo.getAsTreeItem().getChildren().add(((FolderItem) itemToAdd).getAsTreeItem());
            }
        });
    }

    /**
     * Custom tree cell which displays a custom icon for folders in the tree view.
     */
    private final class CustomTreeCellImpl extends TreeCell<FolderItem> {

        private Text icon;

        CustomTreeCellImpl() {
            setGraphicTextGap(10);

            // when the user starts to drag a folder
            setOnDragDetected(event -> {
                if (!isEmpty() || getItem() != null) {
                    if (getItem() != folderRepository.getSearchFolder() &&
                            getItem() != folderRepository.getMasterFolder()) {
                        final ClipboardContent content = new ClipboardContent();
                        content.put(ItemDragAndDropManager.SERIALIZED_MIME_TYPE, getIndex());
                        final Dragboard dashboard = startDragAndDrop(TransferMode.MOVE);
                        final Image dragImage = snapshot(new SnapshotParameters(), null);
                        dashboard.setDragView(dragImage);
                        dashboard.setContent(content);
                        dragAndDropManager.addItemToDragList(getItem());
                        event.consume();
                    }
                }
            });

            // when the user drags a item over this tree cell
            setOnDragOver(event -> {
                final Item toItem = getItem();
                // you cannot drag an item onto the search folder!
                if (toItem != folderRepository.getSearchFolder()) {
                    if (!isEmpty() || toItem != null) {
                        if (dragAndDropManager.hasItemsOnDragList()) {
                            final Item draggedItem = dragAndDropManager.getFirstItem();
                            if (draggedItem != toItem) {
                                if (draggedItem.getParentFolder() != getItem() && (draggedItem != getItem().getParentFolder())) {
                                    event.acceptTransferModes(TransferMode.MOVE);
                                    event.consume();
                                }
                            }
                        }
                    }
                }
            });

            // when the user drops the item that they are dragging over this tree cell
            setOnDragDropped(event -> {
                final Item draggedItem = dragAndDropManager.getFirstItem();
                    draggedItem.getParentFolder().removeItem(draggedItem);
                    eventStudio().broadcast(new AddItemToFolderRequest(getItem(), draggedItem));
                    dragAndDropManager.onDropComplete();
                    event.setDropCompleted(true);
                    event.consume();
            });

            setOnDragDone(event -> {
                if (event.getTransferMode() == null) // if transfer failed
                    dragAndDropManager.onDragFailure();
                event.consume();

            });
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
