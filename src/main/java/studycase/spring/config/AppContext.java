package studycase.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "studycase.database.service")
@Import(WebAppContext.class)
public class AppContext {

}

