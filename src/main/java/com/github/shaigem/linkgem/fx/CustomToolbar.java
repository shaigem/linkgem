package com.github.shaigem.linkgem.fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created on 2017-01-01.
 */
public class CustomToolbar extends HBox {

    private HBox leftSection;
    private HBox centerSection;
    private HBox rightSection;

    public CustomToolbar() {
        super();
        setFillHeight(true);
        setSpacing(8);
        setAlignment(Pos.CENTER);
        leftSection = new HBox();
        centerSection = new HBox();
        rightSection = new HBox();
        setMargin(leftSection, new Insets(0, 0, 0, 12));
        setMargin(rightSection, new Insets(0, 12, 0, 0));
        HBox.setHgrow(leftSection, Priority.ALWAYS);
        HBox.setHgrow(centerSection, Priority.ALWAYS);
        HBox.setHgrow(rightSection, Priority.ALWAYS);
        leftSection.spacingProperty().bind(spacingProperty());
        centerSection.spacingProperty().bind(spacingProperty());
        rightSection.spacingProperty().bind(spacingProperty());
        leftSection.setAlignment(Pos.CENTER_LEFT);
        centerSection.setAlignment(Pos.CENTER);
        rightSection.setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(leftSection, centerSection, rightSection);
    }

    public HBox getLeftSection() {
        return leftSection;
    }

    public HBox getRightSection() {
        return rightSection;
    }

    public HBox getCenterSection() {
        return centerSection;
    }

}
