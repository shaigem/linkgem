package com.github.shaigem.linkgem.ui.events;

/**
 * Event that notifies event listeners to save all items to JSON.
 *
 * @author Ronnie Tran
 */
public class SaveAllEvent {

    /**
     * Determines whether or not a dialog will show if saving has been successful.
     */
    private boolean showSuccessDialog = false;

    public SaveAllEvent(boolean showSuccessDialog) {
        this.showSuccessDialog = showSuccessDialog;
    }

    public boolean isShowSuccessDialog() {
        return showSuccessDialog;
    }
}
