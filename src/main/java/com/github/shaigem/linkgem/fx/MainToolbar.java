package com.github.shaigem.linkgem.fx;

import com.github.shaigem.linkgem.ui.search.SearchPresenter;
import com.github.shaigem.linkgem.ui.search.SearchView;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import static org.sejda.eventstudio.StaticStudio.eventStudio;

/**
 * Created on 2017-01-01.
 */
public class MainToolbar extends CustomToolbar {

    private final SearchView searchView;

    public MainToolbar() {
        super();
        getStyleClass().addAll("main-toolbar");

        final Image logoImage = new Image(this.getClass().getResource("/images/miu.png").toExternalForm());
        final ImageView logoView = new ImageView(logoImage);
        getLeftSection().getChildren().addAll(logoView);


        //   final Button saveAllButton = button(GlyphsDude.createIcon(MaterialDesignIcon.CONTENT_SAVE_ALL, "1.8em")
        //         , "Save all changes");

        searchView = new SearchView();
        final Button aboutButton =
                button(GlyphsDude.createIcon(FontAwesomeIcon.COG, "1.8em"), "");

        getRightSection().getChildren().addAll(searchView.getViewWithoutRootContainer(), /*saveAllButton ,*/ aboutButton);
        eventStudio().addAnnotatedListeners(this);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public SearchPresenter getSearchPresenter() {
        return (SearchPresenter) searchView.getPresenter();
    }

    private Button button(Text icon, String tooltipText) {
        Button button = button(tooltipText);
        button.setTooltip(new Tooltip(tooltipText));
        button.setGraphic(icon);
        return button;
    }

    private Button button(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("main-toolbar-button");
        return button;
    }

}
