package com.github.shaigem.linkgem.ui.main.browser;

import com.github.shaigem.linkgem.fx.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.ui.events.*;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.beans.binding.Bindings;
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
        eventStudio().addAnnotatedListeners(this);
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
        folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                eventStudio().broadcast(new SelectedFolderChangedEvent(oldValue == null ? null :
                        oldValue.getValue(), newValue == null ? null : newValue.getValue())));
    }

    @EventListener
    private void onDeleteItem(DeleteItemEvent event) {
        final Item deletedItem = event.getDeletedItem();
        // TODO
        if (deletedItem instanceof FolderItem) {
            //   folderRepository.getFolders().remove(deletedItem);
            //    rootFolder.getChildren().remove(((FolderItem) deletedItem).getAsTreeItem());
        }
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

        private ContextMenu menu;
        private Text icon;

        CustomTreeCellImpl() {
            createContextMenu();
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
                setContextMenu(null);
            } else {
                icon = getTreeItem().isExpanded() && !getTreeItem().isLeaf() ?
                        GlyphsDude.createIcon(FontAwesomeIcon.FOLDER_OPEN, "1.4em")
                        : GlyphsDude.createIcon(FontAwesomeIcon.FOLDER, "1.4em");
                // setText(item.getName());
                textProperty().bind(item.nameProperty());
                setGraphic(icon);
                setContextMenu(menu);
            }
        }

        private void createContextMenu() {
            menu = new ContextMenu();
            // TODO remove item action
            final MenuItem editFolder = createMenuItem("Edit Folder...");
            editFolder.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getItem(), getItem(), false)));
            final SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

            final MenuItem newFolder = createMenuItem("Add Folder...");
            newFolder.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getItem(), new FolderItem("New Folder"), true)));

            final MenuItem newBookmark = createMenuItem("Add Bookmark...");
            newBookmark.setOnAction(event -> eventStudio().broadcast(new OpenItemDialogRequest(getItem(),
                    new BookmarkItem("New Bookmark"), true)));
            menu.getItems().addAll(editFolder, separatorMenuItem, newFolder, newBookmark);
        }

        private MenuItem createMenuItem(String text) {
            final MenuItem item = new MenuItem(text);
            // TODO change this when we add a remove button
            item.disableProperty().bind(Bindings.when(itemProperty().isEqualTo(folderRepository.getSearchFolder())).
                    then(true).otherwise(false));
            return item;
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
