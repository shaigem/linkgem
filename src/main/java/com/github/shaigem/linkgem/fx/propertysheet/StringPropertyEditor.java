package com.github.shaigem.linkgem.fx.propertysheet;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

/**
 * Created on 2016-12-28.
 */
public class StringPropertyEditor extends AbstractPropertyEditor<String, TextField> {

    public StringPropertyEditor(PropertySheet.Item property) {
        super(property, new TextField());
    }

    @Override protected StringProperty getObservableValue() {
        return getEditor().textProperty();
    }

    @Override public void setValue(String value) {
        getEditor().setText(value);
    }
}
