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
        getStyleClass().addAll(CommonStyle.TOOLBAR_CONTENT.getStyleClasses());
        titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll(CommonStyle.TITLE_LABEL.getStyleClasses());
        getLeftSection().getChildren().addAll(titleLabel);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }
}
