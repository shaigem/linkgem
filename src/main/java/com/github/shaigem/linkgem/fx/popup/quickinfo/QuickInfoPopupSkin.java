package com.github.shaigem.linkgem.fx.popup.quickinfo;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

/**
 * Created on 2016-12-30.
 */
public class QuickInfoPopupSkin implements Skin<QuickInfoPopup> {

    private QuickInfoPopup quickInfoPopup;

    private Region region;

    private TextFlow infoTextFlow;
    private Text infoText;

    QuickInfoPopupSkin(QuickInfoPopup popup) {
        quickInfoPopup = popup;
        region = quickInfoPopup.getRoot();
        Bindings.bindContent(region.getStyleClass(), quickInfoPopup.getStyleClass());
        infoTextFlow = quickInfoPopup.getRoot();
        infoTextFlow.getStyleClass().addAll("info-text-flow");
        infoText = new Text();
        infoText.textProperty().bind(popup.informationProperty());
        infoText.getStyleClass().add("info-text");
        infoTextFlow.getChildren().add(infoText);
        infoTextFlow.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3);
    }


    @Override
    public QuickInfoPopup getSkinnable() {
        return quickInfoPopup;
    }

    @Override
    public Node getNode() {
        return region;
    }

    @Override
    public void dispose() {
        quickInfoPopup = null;
        region = null;
    }
}
