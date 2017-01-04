package com.github.shaigem.linkgem.ui.main.explorer;

import com.github.shaigem.linkgem.fx.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.model.item.ItemType;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.sort.SortOrder;
import com.github.shaigem.linkgem.sort.impl.MergeSortingRoutine;
import com.github.shaigem.linkgem.ui.events.ItemSelectionChangedEvent;
import com.github.shaigem.linkgem.ui.events.OpenFolderRequest;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import com.github.shaigem.linkgem.ui.events.SelectedFolderChangedEvent;
import com.github.shaigem.linkgem.ui.main.MainWindowPresenter;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2016-12-25.
 */
public class FolderExplorerPresenter implements Initializable {

    public enum ExplorerAction {
        ADD_FOLDER, ADD_BOOKMARK, DELETE
    }

    private MainWindowPresenter mainWindowPresenter;

    private FolderItem viewingFolder;

    @Inject
    private FolderRepository folderRepository;

    @FXML
    StackPane toolbarPane;
    @FXML
    StackPane itemsView;

    @FXML
    TableView<Item> itemTableView;
    @FXML
    TableColumn<Item, String> nameColumn;
    @FXML
    TableColumn<Item, String> descriptionColumn;
    @FXML
    TableColumn<Item, String> locationColumn;
    @FXML
    TableColumn<Item, ItemType> typeColumn;

    private ThemeTitledToolbar toolbar;
    private MenuButton viewSettingsMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewingFolder = folderRepository.getRootFolder();
        final Label placeholder = new Label("Folder contains no items");
        itemTableView.setPlaceholder(placeholder);
        itemTableView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Item rowData = row.getItem();
                    if (rowData instanceof FolderItem) {
                        eventStudio().broadcast(new OpenFolderRequest((FolderItem) rowData));
                    }

                }
            });
            return row;
        });
        itemTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                eventStudio().broadcast(new ItemSelectionChangedEvent(newValue)));
        initToolbar();
        initColumns();
        eventStudio().addAnnotatedListeners(this);
    }

    private void initColumns() {
        nameColumn.setCellValueFactory(e -> e.getValue().nameProperty());
        final TableColumn<Item, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(e -> {
            if (e.getValue() instanceof BookmarkItem) {
                final BookmarkItem bookmarkItem = (BookmarkItem) e.getValue();
                return bookmarkItem.locationProperty();
            }
            return null;
        });
        locationColumn.setCellFactory(column -> new TooltipTableCell());

        descriptionColumn.setCellValueFactory(e -> e.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(column -> new TooltipTableCell());

        typeColumn.setCellValueFactory(e -> e.getValue().itemTypeProperty());

    }

    public void performAction(ExplorerAction action) {

        switch (action) {
            case ADD_BOOKMARK:
                onAddBookmarkAction();
                break;
            case ADD_FOLDER:
                onAddFolderAction();
                break;

        }
    }

    @FXML
    private void onAddBookmarkAction() {
        eventStudio().broadcast
                (new OpenItemDialogRequest(getViewingFolder(), new BookmarkItem("New Bookmark"), true));
    }

    @FXML
    private void onAddFolderAction() {
        eventStudio().broadcast
                (new OpenItemDialogRequest(getViewingFolder(), new FolderItem("New Folder"), true));
    }

    @EventListener
    private void onSelectedFolderChanged(SelectedFolderChangedEvent event) {
        FolderItem viewingFolder = event.getNewFolder();
        if (viewingFolder != null) {
            itemTableView.setItems(viewingFolder.getChildren());
            toolbar.getTitleLabel().textProperty().bind(viewingFolder.nameProperty());
            System.out.println("Selected Folder Changed!");
        }
    }

    private void initToolbar() {
        toolbar = new ThemeTitledToolbar("Explorer");
        createRightSectionToolbarItems();
        toolbarPane.getChildren().addAll(toolbar);
    }

    private void createRightSectionToolbarItems() {
        createViewSettingsMenu();
        final Button deleteButton = new Button();
        deleteButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.DELETE, "1.8em"));
        deleteButton.setContextMenu(new ContextMenu(new MenuItem("Delete All")));
        deleteButton.setOnAction(event -> performAction(ExplorerAction.DELETE));

        final Button addBookmarkButton = new Button();
        addBookmarkButton.setTooltip(new Tooltip("Add a new bookmark"));
        addBookmarkButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.BOOKMARK_PLUS, "1.8em"));
        addBookmarkButton.setOnAction(event -> performAction(ExplorerAction.ADD_BOOKMARK));

        final Button addFolderButton = new Button();
        addFolderButton.setTooltip(new Tooltip("Add a new folder"));
        addFolderButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.FOLDER_PLUS, "1.8em"));
        addFolderButton.setOnAction(event -> performAction(ExplorerAction.ADD_FOLDER));

        toolbar.getRightSection().getChildren().addAll(addFolderButton, addBookmarkButton, deleteButton, viewSettingsMenuButton);
    }


    private void createViewSettingsMenu() {
        viewSettingsMenuButton = new MenuButton();
        viewSettingsMenuButton.setTooltip(new Tooltip("Change view-specific settings"));
        viewSettingsMenuButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.SETTINGS, "1.8em"));

        final MenuItem sortAscendingMenuItem = new MenuItem("Sort by Ascending (A-Z)");
        sortAscendingMenuItem.setOnAction(event -> performManualSorting(SortOrder.ASCENDING));
        final MenuItem sortDescendingMenuItem = new MenuItem("Sort by Descending (Z-A)");
        sortDescendingMenuItem.setOnAction(event -> performManualSorting(SortOrder.DESCENDING));

        sortAscendingMenuItem.disableProperty().bind(Bindings.isEmpty(getViewingFolder().getChildren()));
        sortDescendingMenuItem.disableProperty().bind(Bindings.isEmpty(getViewingFolder().getChildren()));

        viewSettingsMenuButton.getItems().add(sortAscendingMenuItem);
        viewSettingsMenuButton.getItems().add(sortDescendingMenuItem);
        viewSettingsMenuButton.getItems().add(new SeparatorMenuItem());

        for (TableColumn<Item, ?> itemTableColumn : itemTableView.getColumns()) {
            final String columnName = itemTableColumn.getText();
            final CheckMenuItem checkMenuItem = new CheckMenuItem
                    ("Show " + columnName + " Column");
            checkMenuItem.setSelected(true);
            itemTableColumn.visibleProperty().bindBidirectional(checkMenuItem.selectedProperty());
            if (columnName.equals("Name")) {
                // we must always show the name column so don't allow users to hide it
                checkMenuItem.setDisable(true);
            }
            viewSettingsMenuButton.getItems().add(checkMenuItem);
        }
    }


    public FolderItem getViewingFolder() {
        return viewingFolder;
    }


    public void setMainWindowPresenter(MainWindowPresenter mainWindowPresenter) {
        this.mainWindowPresenter = mainWindowPresenter;
    }

    public MainWindowPresenter getMainWindowPresenter() {
        return mainWindowPresenter;
    }

    private void performManualSorting(SortOrder order) {
        if (getViewingFolder().getChildren().isEmpty()) {
            // just in case!
            return;
        }
        Item[] itemsToSort = new Item[getViewingFolder().getChildren().size()];
        itemsToSort = getViewingFolder().getChildren().toArray(itemsToSort);
        MergeSortingRoutine mergeSortingRoutine = new MergeSortingRoutine();
        getViewingFolder().getChildren().setAll(mergeSortingRoutine.sort(order, itemsToSort));

    }

    private final static class TooltipTableCell extends TableCell<Item, String> {

        final Tooltip tooltip;

        TooltipTableCell() {
            tooltip = new Tooltip();
            tooltip.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
            tooltip.setWrapText(true);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                setTooltip(null);
            } else {
                setText(item);
                if (!item.isEmpty()) {
                    tooltip.setText(item);
                    setTooltip(tooltip);
                } else {
                    tooltip.setText("");
                    setTooltip(null);
                }
            }
        }
    }


}

