package com.github.shaigem.linkgem.fx;

import javafx.scene.control.Label;

/**
 * Created on 2017-01-01.
 */
public class ThemeTitledToolbar extends CustomToolbar {

    private Label titleLabel;

    public ThemeTitledToolbar(String title) {
        if(title.isEmpty()) {
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
