package fi.stardex.boschdemo.ui;

import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@Configuration
@Import(DBConfig.class)
public class SpringJavaConfig {

    @Bean
    public ViewHolder rootLayout() {
        return loadView("/fxml/RootLayout.fxml");
    }

    private ViewHolder loadView(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
        ViewHolder viewHolder = new ViewHolder();
        try {
            viewHolder.setView(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println("Exception while loadView");
        }
        viewHolder.setController(fxmlLoader.getController());
        return viewHolder;
    }
}
