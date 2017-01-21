package com.github.shaigem.linkgem.fx.toolbar;

import javafx.scene.control.Label;

/**
 * A custom toolbar that follows a theme style that is set in CSS. It also contains a title label by default.
 *
 * @author Ronnie Tran
 */
public class ThemeTitledToolbar extends CustomToolbar {

    private Label titleLabel;

    public ThemeTitledToolbar(String title) {
        if (title.isEmpty()) {
            title = "No Title";
        }
        getStylesheets().add(ThemeTitledToolbar.class.getResource("themetoolbar.css").toExternalForm());
        getStyleClass().addAll("toolbar-parent");
        titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("toolbar-title");
        getLeftSection().getChildren().addAll(titleLabel);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }
}
