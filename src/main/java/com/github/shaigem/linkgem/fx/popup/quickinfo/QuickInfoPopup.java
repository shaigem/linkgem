package com.github.shaigem.linkgem.fx.popup.quickinfo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;

/**
 * Displays a popup that is similar to a tooltip showing information to the user.
 * The difference between this and a tooltip is that when the user hovers a node with a quick info popup,
 * it will be displayed instantly.
 *
 * @author Ronnie Tran
 */
public class QuickInfoPopup extends PopupControl {

    private static final String DEFAULT_STYLE_CLASS = "quick-info-popup";

    private static QuickInfoPopupBehaviour BEHAVIOR = new QuickInfoPopupBehaviour();

    private final TextFlow content;

    public QuickInfoPopup(String text) {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        content = new TextFlow();
        content.getStylesheets().add(
                QuickInfoPopup.class.getResource("quickinfopopup.css").toExternalForm());
        setAutoHide(true);
        setAutoFix(true);
        setHideOnEscape(true);
        setInformation(text);
    }

    private final StringProperty information
            = new SimpleStringProperty(this, "information");


    public StringProperty informationProperty() {
        return information;
    }

    public void setInformation(String information) {
        this.information.set(information);
    }

    public static void install(Node node, QuickInfoPopup t) {
        BEHAVIOR.install(node, t);
    }

    public final TextFlow getRoot() {
        return content;
    }


    private static class QuickInfoPopupBehaviour {

        private void install(Node node, QuickInfoPopup popup) {
            if (node == null) {
                return;
            }
            node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                if (!popup.isShowing()) {
                    Point2D p = node.localToScreen(node.getLayoutBounds().getMaxX(),
                            node.getLayoutBounds().getMaxY());
                    popup.show(node, p.getX(), p.getY());
                }
            });
            node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> popup.hide());
        }
    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new QuickInfoPopupSkin(this);
    }
}
