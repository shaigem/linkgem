package com.github.shaigem.linkgem.fx.propertysheet.editor;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Created on 2016-12-28.
 */
public class StringPropertyEditor implements PropertyEditor<String> {
    private final TextField text;
    private PropertySheet.Item item;

    public StringPropertyEditor(PropertySheet.Item aItem) {
        item = aItem;
        text = new TextField();
        text.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                commitChanges();
            }
        });
        text.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitChanges();
            }
        });

    }

    protected void commitChanges() {
        item.setValue(text.getText());
    }

    @Override
    public Node getEditor() {
        return text;
    }

    @Override
    public String getValue() {
        return text.getText();
    }

    @Override
    public void setValue(String aValue) {
        text.setText(aValue);
    }

    public PropertySheet.Item getItem() {
        return item;
    }

    public TextField getTextField() {
        return text;
    }
}
