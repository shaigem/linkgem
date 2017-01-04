package com.github.shaigem.linkgem.fx;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.CustomTextField;

/**
 * Created on 2017-01-01.
 */
public class MainToolbar extends CustomToolbar {

    private final CustomTextField searchTextField;

    public MainToolbar() {
        super();
        getStyleClass().addAll("main-toolbar");

        final Image logoImage = new Image(this.getClass().getResource("/images/miu.png").toExternalForm());
        final ImageView logoView = new ImageView(logoImage);
        getLeftSection().getChildren().addAll(logoView);

        final Button aboutButton =
                button(GlyphsDude.createIcon(FontAwesomeIcon.COG, "1.8em"), "");

        searchTextField = new CustomTextField();
        searchTextField.setRight(GlyphsDude.createIcon(MaterialDesignIcon.MAGNIFY, "1.8em"));
        searchTextField.setPromptText("Search Bookmarks");
        getRightSection().getChildren().addAll(searchTextField, aboutButton);

    }

    private Button button(Text icon, String text) {
        Button button = button(text);
        button.setGraphic(icon);
        return button;
    }

    private Button button(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("main-toolbar-button");
        return button;
    }


    public CustomTextField getSearchTextField() {
        return searchTextField;
    }
}
