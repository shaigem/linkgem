package com.github.shaigem.linkgem.fx;

import com.github.shaigem.linkgem.ui.events.SaveAllEvent;
import com.github.shaigem.linkgem.ui.search.SearchPresenter;
import com.github.shaigem.linkgem.ui.search.SearchView;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * The toolbar that is used in the main window.
 *
 * @author Ronnie Tran
 */
public class MainToolbar extends CustomToolbar {

    private final SearchView searchView;

    public MainToolbar() {
        super();
        getStyleClass().addAll("main-toolbar");
        // create the logo which will be shown on the far left side of the toolbar
        final Image logoImage = new Image(this.getClass().getResource("/images/miu.png").toExternalForm());
        final ImageView logoView = new ImageView(logoImage);
        getLeftSection().getChildren().addAll(logoView);
        // create the save all button
        final Button saveAllButton = button(GlyphsDude.createIcon(MaterialDesignIcon.CONTENT_SAVE, "1.8em")
                , "Save all bookmarks");
        // broadcast an event to all event listeners listening for a SaveAllEvent.
        saveAllButton.setOnAction(event -> eventStudio().broadcast(new SaveAllEvent()));
        // create the search view
        searchView = new SearchView();
        // create the about button
        final Button aboutButton =
                button(GlyphsDude.createIcon(FontAwesomeIcon.COG, "1.8em"), "");
        getRightSection().getChildren().addAll(searchView.getViewWithoutRootContainer(), saveAllButton, aboutButton);
        eventStudio().addAnnotatedListeners(this);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public SearchPresenter getSearchPresenter() {
        return (SearchPresenter) searchView.getPresenter();
    }

    /**
     * Create a button with a icon as the button's graphic.
     *
     * @param icon        the icon
     * @param tooltipText the tooltip that the button will show
     * @return the new button
     */
    private Button button(Text icon, String tooltipText) {
        Button button = button("");
        if (!tooltipText.isEmpty()) {
            button.setTooltip(new Tooltip(tooltipText));
        }
        button.setGraphic(icon);
        return button;
    }

    /**
     * Creates a new button with a custom style class.
     *
     * @param text the text to be shown on the button
     * @return the new button
     */
    private Button button(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("main-toolbar-button");
        return button;
    }

}
