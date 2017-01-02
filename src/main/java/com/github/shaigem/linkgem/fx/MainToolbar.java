package com.github.shaigem.linkgem.fx;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Created on 2017-01-01.
 */
public class MainToolbar extends CustomToolbar {

    public MainToolbar() {
        super();
        getStyleClass().addAll("main-toolbar");

        final Button saveButton = button(GlyphsDude.createIcon(MaterialDesignIcon.CONTENT_SAVE, "1.8em"), "Save");
        final Button testButton =
                button(GlyphsDude.createIcon(MaterialDesignIcon.TABLE, "1.8em"), "Table");
        getLeftSection().getChildren().addAll(saveButton, testButton);

        final Button aboutButton =
                button(GlyphsDude.createIcon(FontAwesomeIcon.COG, "1.8em"), "");

        getRightSection().getChildren().addAll(aboutButton);

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

}
