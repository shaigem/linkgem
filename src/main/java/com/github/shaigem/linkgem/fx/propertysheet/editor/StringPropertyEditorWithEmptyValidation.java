package com.github.shaigem.linkgem.fx.propertysheet.editor;

import com.github.shaigem.linkgem.fx.control.NodeTextField;
import com.github.shaigem.linkgem.fx.popup.quickinfo.QuickInfoIcon;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Allows the editing of string based properties. Validation support is enabled.
 * If the input is empty, the validation will warn the user and will not save any changes.
 *
 * @author Ronnie Tran
 */
public class StringPropertyEditorWithEmptyValidation implements PropertyEditor<String> {

    private NodeTextField textField;
    private PropertySheet.Item item;
    private ValidationSupport validationSupport;
    private Text exclamationTriangle;

    public StringPropertyEditorWithEmptyValidation(PropertySheet.Item aItem) {
        this.item = aItem;
        textField = new NodeTextField();
        validationSupport = new ValidationSupport();
        validationSupport.setErrorDecorationEnabled(false); // have to be false because of a unknown bug
        Validator<String> theValidator = Validator.createEmptyValidator("Cannot be empty");
        Platform.runLater(() -> validationSupport.registerValidator(textField, false, theValidator));
        exclamationTriangle = QuickInfoIcon.create("Input cannot be empty!",
                GlyphsDude.createIcon(FontAwesomeIcon.EXCLAMATION_TRIANGLE, "1.4em"));
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                commitChanges();
            }
        });
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitChanges();
            }
        });
        // if the input is invalid, show the exclamation triangle. Otherwise, hide it.
        validationSupport.invalidProperty().addListener((observable, oldValue, newValue) ->
                textField.setLeft(newValue ? exclamationTriangle : null));
    }


    protected void commitChanges() {
        if (!validationSupport.isInvalid()) {
            item.setValue(textField.getText());
        }
    }

    @Override
    public Node getEditor() {
        return textField;
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(String aValue) {
        getTextField().setText(aValue);
        Platform.runLater(validationSupport::redecorate);
    }

    public PropertySheet.Item getItem() {
        return item;
    }

    TextField getTextField() {
        return textField;
    }

    ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}
