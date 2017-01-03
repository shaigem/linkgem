package com.github.shaigem.linkgem.ui.main.explorer.tile;

import com.github.shaigem.linkgem.fx.control.IconButton;
import com.github.shaigem.linkgem.util.ColorUtil;
import com.github.shaigem.linkgem.util.RandomColorGenerator;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created on 2016-12-31.
 * @author Ronnie Tran
 */
public class ItemTileView extends BorderPane {

    private ItemTileController controller;

    private StackPane centerStackPane;
    private VBox bottomBox;

    private Label nameLabel;
    private Label descriptionLabel;
    private Label locationLabel;

    private Rectangle background;

    private IconButton iconButton; // TODO temporary


    public ItemTileView() {
        super();
        getStylesheets().add(ItemTileView.class.getResource("itemtile.css").toExternalForm());
        getStyleClass().addAll("item-tile");
        controller = new ItemTileController(this);
        setPrefSize(150, 150);
        createCenter();
        createBottom();
    }

    private void createCenter() {
        centerStackPane = new StackPane();
        centerStackPane.setPrefSize(150, 100);
        background = new Rectangle(150, 100);
        final Color randomColor = RandomColorGenerator.getInstance().getRandomColor();
        background.setFill(randomColor);
        nameLabel = new Label("YouTube");
        nameLabel.getStyleClass().add("item-tile-name");
        centerStackPane.getChildren().addAll(background, nameLabel);
        setCenter(centerStackPane);
    }


    private void createBottom() {
        bottomBox = new VBox();
        bottomBox.getStyleClass().addAll("item-tile-bottom-box");
        bottomBox.setPrefSize(150, 50);
        bottomBox.setSpacing(2);

        // create top box with the favicon
        final HBox titleBox = new HBox();

        descriptionLabel = new Label("No description");
        final Tooltip descriptionToolTip = new Tooltip();
        descriptionToolTip.textProperty().bind(descriptionLabel.textProperty());
        descriptionLabel.setTooltip(descriptionToolTip);
        descriptionLabel.getStyleClass().add("item-tile-description");
        descriptionLabel.setMinHeight(40);
        descriptionLabel.setWrapText(true);
        final Pane centerPane = new Pane();
        // TODO the favicon might become a button?
        iconButton = new IconButton( GlyphsDude.createIcon(FontAwesomeIcon.FOLDER, "32"), "");
        titleBox.getChildren().addAll(descriptionLabel, centerPane, iconButton);
        HBox.setHgrow(descriptionLabel, Priority.ALWAYS);
        HBox.setHgrow(centerPane, Priority.ALWAYS);
        HBox.setHgrow(iconButton, Priority.ALWAYS);

        // create other information
        locationLabel = new Label();
        locationLabel.getStyleClass().add("item-tile-location");
        final Tooltip locationToolTip = new Tooltip();
        locationToolTip.textProperty().bind(locationLabel.textProperty());
        locationLabel.setTooltip(locationToolTip);
        bottomBox.getChildren().addAll(titleBox, locationLabel);
        setBottom(bottomBox);
    }

    public ItemTileController getController() {
        return controller;
    }

    public void setBackgroundFill(Color color) {
        background.setFill(color);
        nameLabel.setTextFill(ColorUtil.color(color));
    }

    public Rectangle getBackgroundRectangle() {
        return background;
    }

    public Label getDescriptionLabel() {
        return descriptionLabel;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getLocationLabel() {
        return locationLabel;
    }

    public IconButton getIconButton() {
        return iconButton;
    }
}
