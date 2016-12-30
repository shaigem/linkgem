package com.github.shaigem.linkgem.ui.dialog;

import com.airhacks.afterburner.views.FXMLView;
import com.github.shaigem.linkgem.ui.events.OpenItemDialogRequest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created on 2016-12-30.
 */
public abstract class ItemDialog extends Stage {

    private enum ButtonType {
        CANCEL, OK
    }

    private FXMLView content;

    private ButtonType clickedButton = ButtonType.CANCEL;

    protected ItemDialog(FXMLView content) {
        super();
        this.content = content;
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
        setResizable(false);
        final Scene scene = new Scene(content.getView());
        setScene(scene);
        getContentPresenter().getOKButton().setOnAction(event -> {
            clickedButton = ButtonType.OK;
            Platform.runLater(this::close); // MUST have Platform.runLater!!! Otherwise, crash on Linux
        });
        getContentPresenter().getCancelButton().setOnAction(event -> close());
    }

    public void showDialog(final OpenItemDialogRequest request) {
        getContentPresenter().initProperties(request);
        showAndWait();
        if (clickedButton == ButtonType.OK) {
            getContentPresenter().saveProperties();
        }
    }

    private DialogBasedItemEditor getContentPresenter() {
        return (DialogBasedItemEditor) content.getPresenter();
    }
}
