package studycase.spring.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "studycase.database.service")
@Import(WebAppContext.class)
public class AppContext {
	
	  @Bean
	  public SessionFactory sessionFactory() {
		  org.hibernate.cfg.Configuration conf = new org.hibernate.cfg.Configuration();
		  conf.configure();
		  ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		  return conf.buildSessionFactory(serviceRegistry);
	  }
}

