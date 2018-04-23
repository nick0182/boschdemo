package fi.stardex.boschdemo.main;

import fi.stardex.boschdemo.ui.ViewHolder;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends AbstractJavaFxApplicationSupport {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewHolder rootLayoutViewHolder = context.getBean("rootLayout", ViewHolder.class);
        VBox rootLayout =(VBox) rootLayoutViewHolder.getView();

        Scene scene = new Scene(rootLayout);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Bosch coding demonstration");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launchApp(Main.class, args);
    }
}
