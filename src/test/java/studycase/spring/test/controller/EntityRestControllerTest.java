package studycase.spring.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import studycase.database.entities.Artist;
import studycase.database.service.EntityService;
import studycase.spring.config.WebAppContext;
import studycase.spring.test.config.TestContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
public class EntityRestControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
    private EntityService entityServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(entityServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
	
	@Test
	public void testGetEntities_ArtistsSuccesfullyRetrieved() throws Exception{
//		Artist first = new Artist("J nummer 1");
//		Artist second = new Artist("J nummer 2");
//		Artist third = new Artist("J nummer 3");
//		first.setArtistId(1);
//		second.setArtistId(2);
//		third.setArtistId(3);
//		
//		when(entityServiceMock.getEntities(Artist.class)).thenReturn(Arrays.asList(first, second, third));
//		
//		mockMvc.perform(get("/api/artists/"))
//			.andExpect(status().isOk());
	}
	
	
}
