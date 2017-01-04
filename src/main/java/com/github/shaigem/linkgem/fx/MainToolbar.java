package com.github.shaigem.linkgem.fx;

import com.github.shaigem.linkgem.ui.search.SearchPresenter;
import com.github.shaigem.linkgem.ui.search.SearchView;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Button;
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


         searchView = new SearchView();


        final Button aboutButton =
                button(GlyphsDude.createIcon(FontAwesomeIcon.COG, "1.8em"), "");

        getRightSection().getChildren().addAll(searchView.getViewWithoutRootContainer(), aboutButton);
        eventStudio().addAnnotatedListeners(this);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public SearchPresenter getSearchPresenter() {
        return (SearchPresenter) searchView.getPresenter();
    }

    private Button button(Text icon, String text) {
        Button button = button(text);
        button.setGraphic(icon);
        return button;
    }

    private Button button(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("main-toolbar-button");
        return button;
    }

}
