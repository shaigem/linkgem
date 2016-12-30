package com.github.shaigem.linkgem;

import com.airhacks.afterburner.injection.Injector;
import com.github.shaigem.linkgem.ui.main.MainWindowView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created on 2016-12-21.
 */
public class LinkGemApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new MainWindowView().getView());
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinkGem");
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
