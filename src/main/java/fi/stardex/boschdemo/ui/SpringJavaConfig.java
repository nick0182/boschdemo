package fi.stardex.boschdemo.ui;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.ui.controller.RootLayoutController;
import javafx.fxml.FXMLLoader;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.List;

@Configuration
@Import(DBConfig.class)
public class SpringJavaConfig {

    @Bean
    public ViewHolder rootLayout() {
        return loadView("/fxml/RootLayout.fxml");
    }

    @Bean
    public RootLayoutController rootLayoutController() {
        return (RootLayoutController) rootLayout().getController();
    }

    @Bean
    @Autowired
    public List<Injector> injectorList(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.setFlushMode(FlushMode.MANUAL);
        session.setDefaultReadOnly(true);

        List<Injector> injectors = session.createQuery("select injector from Injector injector").list();
        rootLayoutController().getComboBoxModels().getItems().setAll(injectors);
        rootLayoutController().getComboBoxModels().getSelectionModel().selectFirst();
        return injectors;
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
