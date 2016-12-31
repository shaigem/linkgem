package com.github.shaigem.linkgem.fx.propertysheet;

import com.github.shaigem.linkgem.fx.propertysheet.editor.LocationPropertyEditor;
import com.github.shaigem.linkgem.fx.propertysheet.editor.StringPropertyEditor;
import com.github.shaigem.linkgem.fx.propertysheet.editor.StringPropertyEditorWithEmptyValidation;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Created on 2016-12-30.
 */
public enum ItemPropertyEditor {

    DEFAULT(StringPropertyEditor.class),
    NAME(StringPropertyEditorWithEmptyValidation.class),
    LOCATION(LocationPropertyEditor.class);

    private Class<? extends PropertyEditor<?>> propertyEditorClass;

    ItemPropertyEditor(Class<? extends PropertyEditor<?>> propertyEditorClass) {
        this.propertyEditorClass = propertyEditorClass;
    }

    public Class<? extends PropertyEditor<?>> getPropertyEditorClass() {
        return propertyEditorClass;
    }
}


