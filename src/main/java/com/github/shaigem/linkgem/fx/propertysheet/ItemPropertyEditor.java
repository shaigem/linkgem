package com.github.shaigem.linkgem.fx.propertysheet;

import com.github.shaigem.linkgem.fx.propertysheet.editor.LocationPropertyEditor;
import com.github.shaigem.linkgem.fx.propertysheet.editor.StringPropertyEditor;
import com.github.shaigem.linkgem.fx.propertysheet.editor.StringPropertyEditorWithEmptyValidation;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Editors that edit a certain property in a bookmark. An editor for a item's name will be a String editor.
 *
 * @author Ronnie Tran
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


