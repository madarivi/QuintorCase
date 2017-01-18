package studycase.spring.test.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import studycase.database.service.EntityService;

@Configuration
public class TestContext {
	
	@Bean
	public EntityService entityService() {
		return Mockito.mock(EntityService.class);
	}
}
