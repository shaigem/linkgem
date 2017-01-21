package com.github.shaigem.linkgem.ui.main.explorer;

import com.github.shaigem.linkgem.fx.ThemeTitledToolbar;
import com.github.shaigem.linkgem.model.item.BookmarkItem;
import com.github.shaigem.linkgem.model.item.FolderItem;
import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.model.item.ItemType;
import com.github.shaigem.linkgem.repository.FolderRepository;
import com.github.shaigem.linkgem.sort.SortOrder;
import com.github.shaigem.linkgem.sort.impl.BookmarkMergeSortingRoutine;
import com.github.shaigem.linkgem.ui.events.*;
import com.github.shaigem.linkgem.ui.main.MainWindowPresenter;
import com.github.shaigem.linkgem.ui.main.explorer.editor.ChangeEditorItemRequest;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Callback;
import org.sejda.eventstudio.annotation.EventListener;

import javax.inject.Inject;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Presenter which handles the presentation logic for the folder explorer.
 * What a folder explorer does is allow users to explore the contents of a folder.
 * The folder browser only allows you to browse for folders but a folder explorer allows users to explorer what is inside the folders.
 *
 * @author Ronnie Tran
 */
public class FolderExplorerPresenter implements Initializable {

    /**
     * Actions that can be performed in the explorer view.
     */
    public enum ExplorerAction {
        ADD_FOLDER, ADD_BOOKMARK, DELETE, SHOW_IN_FOLDER
    }

    private MainWindowPresenter mainWindowPresenter;

    /**
     * The item that is being shown in the explorer.
     */
    private ObjectProperty<FolderItem> viewingFolder;

    @Inject
    private FolderRepository folderRepository;

    @FXML
    StackPane toolbarPane;
    @FXML
    StackPane itemsView;

    @FXML
    TableView<Item> itemTableView;
    @FXML
    TableColumn<Item, Image> iconColumn;
    @FXML
    TableColumn<Item, String> nameColumn;
    @FXML
    TableColumn<Item, String> descriptionColumn;
    @FXML
    TableColumn<Item, String> locationColumn;
    @FXML
    TableColumn<Item, ItemType> typeColumn;

    @FXML
    MenuItem addBookmarkMenuItem;
    @FXML
    MenuItem addFolderMenuItem;
    @FXML
    MenuItem showInFolderMenuItem;
    @FXML
    MenuItem deleteSelectedItemMenuItem;

    private final static String DEFAULT_PLACEHOLDER_TEXT = "Folder contains no items";

    private Label placeholder;
    private ThemeTitledToolbar toolbar;
    private MenuButton viewSettingsMenuButton;

    private BooleanProperty viewingFolderIsReadOnly;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewingFolder = new SimpleObjectProperty<>(folderRepository.getMasterFolder());
        viewingFolderIsReadOnly = new SimpleBooleanProperty();
        addBookmarkMenuItem.disableProperty().bind(viewingFolderIsReadOnly);
        addFolderMenuItem.disableProperty().bind(viewingFolderIsReadOnly);
        deleteSelectedItemMenuItem.disableProperty().bind
                (itemTableView.getSelectionModel().selectedItemProperty().isNull());
        showInFolderMenuItem.disableProperty().bind(itemTableView.getSelectionModel().selectedItemProperty().isNull().or(viewingFolder.isNotEqualTo(folderRepository.getSearchFolder())));
        initTable();
        initToolbar();
        initColumns();
        eventStudio().addAnnotatedListeners(this);
    }

    /**
     * Initializes settings and properties for the item table.
     */
    private void initTable() {
        placeholder = new Label(DEFAULT_PLACEHOLDER_TEXT);
        itemTableView.setPlaceholder(placeholder);
        itemTableView.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Item rowData = row.getItem();
                    if (rowData instanceof FolderItem) {
                        // when a user double clicks on a folder, open it!
                        eventStudio().broadcast(new OpenFolderInExplorerRequest((FolderItem) rowData));
                    }

                }
            });
            return row;
        });
        // when the item that is selected changes, update our editor to show the newly selected item's properties
        itemTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                eventStudio().broadcast(new ChangeEditorItemRequest(newValue)));
    }

    /**
     * Setup columns and how they will be displayed.
     */
    private void initColumns() {
        iconColumn.setCellValueFactory(e -> e.getValue().iconProperty());
        // icon column only shows the icon of a bookmark item
        iconColumn.setCellFactory(new Callback<TableColumn<Item, Image>, TableCell<Item, Image>>() {
            @Override
            public TableCell<Item, Image> call(TableColumn<Item, Image> param) {
                return new TableCell<Item, Image>() {

                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Image item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            imageView.setImage(item);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        nameColumn.setCellValueFactory(e -> e.getValue().nameProperty());
        // name cell will have a tooltip when a user hovers over it
        nameColumn.setCellFactory(column -> new TooltipTableCell());
        // location property will only show if it is a bookmark, not a folder.
        locationColumn.setCellValueFactory(e -> {
            if (e.getValue() instanceof BookmarkItem) {
                final BookmarkItem bookmarkItem = (BookmarkItem) e.getValue();
                return bookmarkItem.locationProperty();
            }
            return null;
        });
        // location cell will have a tooltip when a user hovers over it
        locationColumn.setCellFactory(column -> new TooltipTableCell());
        descriptionColumn.setCellValueFactory(e -> e.getValue().descriptionProperty());
        // description cell will have a tooltip when a user hovers over it
        descriptionColumn.setCellFactory(column -> new TooltipTableCell());
        typeColumn.setCellValueFactory(e -> e.getValue().itemTypeProperty());

    }

    /**
     * Perform an explorer action.
     *
     * @param action the action to perform.
     */
    public void performAction(ExplorerAction action) {

        switch (action) {
            case ADD_BOOKMARK:
                onAddBookmarkAction();
                break;
            case ADD_FOLDER:
                onAddFolderAction();
                break;
            case DELETE: // deletes the selected item
                onDeleteItemAction();
                break;

        }
    }

    /**
     * Adds a new bookmark to the viewing folder.
     */
    @FXML
    private void onAddBookmarkAction() {
        eventStudio().broadcast
                (new OpenItemEditorDialogRequest(getViewingFolder(), new BookmarkItem("New Bookmark"), true));
    }

    /**
     * Adds a new folder to the viewing folder.
     */
    @FXML
    private void onAddFolderAction() {
        eventStudio().broadcast
                (new OpenItemEditorDialogRequest(getViewingFolder(), new FolderItem("New Folder"), true));
    }

    /**
     * Deletes the selected item.
     */
    @FXML
    private void onDeleteItemAction() {
        final Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Selected Item");
        alert.setHeaderText("Delete " + (selectedItem instanceof FolderItem ? "Folder" : "Bookmark") + ": " + selectedItem);
        alert.setContentText("Are you sure you want to delete the selected folder?");
        final Optional<ButtonType> buttonType = alert.showAndWait();
        buttonType.ifPresent(type -> {
            if (type == ButtonType.OK) {

                if (selectedItem instanceof FolderItem) {
                    eventStudio().broadcast(new DeleteFolderRequest(selectedItem));

                } else if (selectedItem instanceof BookmarkItem) {
                    selectedItem.getParentFolder().removeItem(selectedItem);
                }
                // remove the item from the search folder if the user is currently searching
                folderRepository.getSearchFolder().removeItem(selectedItem);
            }
        });
    }

    /**
     * When a user is searching for items, this action will allow users to go to the selected item's parent folder.
     * Example: if the bookmark item that was searched for was found in folder Games, this action will go to the Games folder.
     */
    @FXML
    private void onShowInFolderAction() {
        final Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
        eventStudio().broadcast(new OpenFolderInExplorerRequest(selectedItem.getParentFolder()));
    }

    private FilteredList<Item> searchData;
    private SortedList<Item> sortedSearchData;

    /**
     * Handles any search requests.
     *
     * @param request the request to search for an item
     */
    @EventListener
    private void onSearchItemRequest(SearchItemRequest request) {
        final String searchTerm = request.getSearchTerm();
        if (searchData == null) {
            searchData = new FilteredList<>(folderRepository.getSearchFolder().getChildren(), p -> true);
            sortedSearchData = new SortedList<>(searchData);
            sortedSearchData.comparatorProperty().bind(itemTableView.comparatorProperty());
        }

        if (searchTerm.isEmpty()) { // open the master folder if search is empty
            eventStudio().broadcast(new OpenFolderInExplorerRequest(folderRepository.getMasterFolder()));
            return;
        }
        // if the search term contains a item's name, show that data!
        // if the search term contains a bookmark's URL, show that data!
        searchData.setPredicate(item -> {
            if (searchTerm.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchTerm.toLowerCase();
            if (item.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (item instanceof BookmarkItem && ((BookmarkItem) item).getLocation().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });
        ObservableList<Item> items = folderRepository.collectAllItems();
        // search folder will initially contain every single item
        folderRepository.getSearchFolder().getChildren().setAll(items);
        // open the search folder to show the results
        eventStudio().broadcast(new OpenFolderInExplorerRequest(folderRepository.getSearchFolder()));
    }

    /**
     * Listens if the browser has a different selected folder.
     *
     * @param event the event
     */
    @EventListener
    private void onBrowserSelectedFolderChanged(BrowserSelectedFolderChangedEvent event) {
        FolderItem viewingFolder = event.getNewFolder();
        if (viewingFolder != null) {
            setViewingFolder(viewingFolder);
            final ObservableList<Item> children = viewingFolder.getChildren();
            final boolean isSearchFolder = viewingFolder == folderRepository.getSearchFolder();
            placeholder.setText(isSearchFolder ? "Search returned no results" : DEFAULT_PLACEHOLDER_TEXT);
            if (isSearchFolder) {
                // if it is a search folder, show the items that the search matches
                itemTableView.setItems(sortedSearchData);
            } else {
                // show the items of the new folder we are going to view
                itemTableView.setItems(children);
            }
            System.out.println("Selected Folder Changed!");
        }
    }

    private void initToolbar() {
        toolbar = new ThemeTitledToolbar("Explorer");
        createLeftSectionToolbarItems();
        createRightSectionToolbarItems();
        toolbarPane.getChildren().addAll(toolbar);
    }

    private void createLeftSectionToolbarItems() {
        final Button editViewingFolderButton = new Button();
        editViewingFolderButton.visibleProperty().bind(viewingFolderIsReadOnly.not());
        editViewingFolderButton.setTooltip(new Tooltip("Edit the viewing folder"));
        editViewingFolderButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.PENCIL, "1.8em"));
        editViewingFolderButton.setOnAction(event -> eventStudio().broadcast(new OpenItemEditorDialogRequest(getViewingFolder(), getViewingFolder(), false)));
        toolbar.getLeftSection().getChildren().addAll(editViewingFolderButton);
    }

    private void createRightSectionToolbarItems() {
        createViewSettingsMenu();
        final Button deleteSelectedItemButton = createFolderActionButton(GlyphsDude.createIcon(MaterialDesignIcon.DELETE, "1.8em"),
                "Delete the selected item", ExplorerAction.DELETE);
        deleteSelectedItemButton.disableProperty().bind(itemTableView.getSelectionModel().selectedItemProperty().isNull());
        final Button addBookmarkButton = createFolderActionButton(GlyphsDude.createIcon(MaterialDesignIcon.BOOKMARK_PLUS, "1.8em"),
                "Add a new bookmark", ExplorerAction.ADD_BOOKMARK);
        final Button addFolderButton = createFolderActionButton(GlyphsDude.createIcon(MaterialDesignIcon.FOLDER_PLUS, "1.8em"), "Add a new folder", ExplorerAction.ADD_FOLDER);
        toolbar.getRightSection().getChildren().addAll(addFolderButton, addBookmarkButton, deleteSelectedItemButton, viewSettingsMenuButton);
    }

    /**
     * Create a button which performs folder actions such as adding an item to a folder.
     *
     * @param icon              the button's icon
     * @param tooltip           the tooltip text
     * @param explorerAction    the action
     * @param disableIfReadOnly indicate if this button should disable if the folder is read-only (cannot edit folder but can edit contents)
     * @return the button
     */
    private Button createFolderActionButton(Text icon, String tooltip, ExplorerAction explorerAction, boolean disableIfReadOnly) {
        final Button button = new Button();
        button.setGraphic(icon);
        button.setTooltip(new Tooltip(tooltip));
        button.setOnAction(event -> performAction(explorerAction));
        if (disableIfReadOnly) {
            button.disableProperty().bind(viewingFolderIsReadOnly);
        }
        return button;
    }

    private Button createFolderActionButton(Text icon, String tooltip, ExplorerAction explorerAction) {
        return this.createFolderActionButton(icon, tooltip, explorerAction, true);
    }

    /**
     * Creates the view settings menu which allows users to modify the settings of the explorer table.
     */
    private void createViewSettingsMenu() {
        viewSettingsMenuButton = new MenuButton();
        viewSettingsMenuButton.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.MENU, "1.8em"));

        final MenuItem sortAscendingMenuItem = new MenuItem("Sort by Ascending (A-Z)");
        sortAscendingMenuItem.setOnAction(event -> performManualSorting(SortOrder.ASCENDING));
        final MenuItem sortDescendingMenuItem = new MenuItem("Sort by Descending (Z-A)");
        sortDescendingMenuItem.setOnAction(event -> performManualSorting(SortOrder.DESCENDING));

        viewSettingsMenuButton.getItems().add(sortAscendingMenuItem);
        viewSettingsMenuButton.getItems().add(sortDescendingMenuItem);
        viewSettingsMenuButton.getItems().add(new SeparatorMenuItem());

        // loop through all of the columns except for the first one which is the icon column and create a
        // menu item which allows users to toggle which column to display
        for (int i = 1; i < itemTableView.getColumns().size(); i++) {
            final TableColumn<Item, ?> itemTableColumn = itemTableView.getColumns().get(i);
            final String columnName = itemTableColumn.getText();
            final CheckMenuItem checkMenuItem = new CheckMenuItem
                    ("Show " + columnName + " Column");
            checkMenuItem.setSelected(true);
            itemTableColumn.visibleProperty().bindBidirectional(checkMenuItem.selectedProperty());
            // we must always show the name column so don't allow users to hide it!
            if (columnName.equals("Name")) {
                checkMenuItem.setDisable(true);
            }
            viewSettingsMenuButton.getItems().add(checkMenuItem);
        }
    }


    public void setViewingFolder(FolderItem viewingFolder) {
        this.viewingFolder.set(viewingFolder);
        viewingFolderIsReadOnly.bind(viewingFolder.readOnlyProperty());
        toolbar.getTitleLabel().textProperty().bind(viewingFolder.nameProperty());
    }

    public FolderItem getViewingFolder() {
        return viewingFolder.get();
    }

    public void setMainWindowPresenter(MainWindowPresenter mainWindowPresenter) {
        this.mainWindowPresenter = mainWindowPresenter;
    }

    public MainWindowPresenter getMainWindowPresenter() {
        return mainWindowPresenter;
    }

    /**
     * Manually sorts the explorer's items by the given sort order.
     *
     * @param order the order to sort the items in
     */
    private void performManualSorting(SortOrder order) {
        if (getViewingFolder().getChildren().isEmpty()) {
            return;
        }
        Item[] itemsToSort = new Item[getViewingFolder().getChildren().size()];
        itemsToSort = getViewingFolder().getChildren().toArray(itemsToSort);
        final BookmarkMergeSortingRoutine bookmarkMergeSortingRoutine = new BookmarkMergeSortingRoutine();
        getViewingFolder().getChildren().setAll(bookmarkMergeSortingRoutine.sort(order, itemsToSort));
    }

    /**
     * Table cell which shows a tooltip for the cell's text.
     */
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

