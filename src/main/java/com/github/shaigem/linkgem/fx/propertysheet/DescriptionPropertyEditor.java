package com.github.shaigem.linkgem.fx.propertysheet;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

/**
 * Created on 2016-12-29.
 */
public class DescriptionPropertyEditor extends AbstractPropertyEditor<String, TextArea> {


    public DescriptionPropertyEditor(PropertySheet.Item property) {
        super(property, new TextArea());
        getEditor().setPrefSize(50, 50);
    }

    @Override
    protected ObservableValue<String> getObservableValue() {
        return getEditor().textProperty();
    }

    @Override
    public void setValue(String value) {
        getEditor().setText(value);
    }


}