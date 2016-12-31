package com.github.shaigem.linkgem.fx.popup.quickinfo;

import javafx.scene.Cursor;
import javafx.scene.text.Text;

/**
 * @author Ronnie T.
 */
public final class QuickInfoIcon {

    public static Text create(final String infoText, final Text icon) {
        icon.setCursor(Cursor.DEFAULT);
        QuickInfoPopup.install(icon, new QuickInfoPopup(infoText));
        return icon;
    }
}
