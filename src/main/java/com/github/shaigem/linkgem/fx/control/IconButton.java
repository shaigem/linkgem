package com.github.shaigem.linkgem.fx.control;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Created on 2017-01-01.
 */
public class IconButton extends Button {


    public IconButton(Text icon, String text) {
        super(text);
        getStyleClass().add("icon-button");
        setPrefSize(32,32);
        setGraphic(icon);
    }


}
