package studycase.spring.test.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.Song;
import studycase.database.service.EntityService;
import studycase.database.service.EntityServiceException;
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

    private static final String API_LOC = "/api/";
    private static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),                        
            Charset.forName("utf8")                     
            );
    
    
    // Test Objects //
    private static List<Entity> artists = new ArrayList<Entity>();
    private static List<Entity> albums = new ArrayList<Entity>();
    private static Song song;
    @BeforeClass
    public static void initTestObjects() {
    	List<String> artistNames = Arrays.asList("one1", "two2", "three3");
    	for (int i = 0; i < artistNames.size(); i++) {
    		Artist artist = new Artist(artistNames.get(i));
    		artist.setArtistId(i+1);
    		artists.add(artist);
    	}
    	
    	Artist artist = (Artist) artists.get(0);
    	List<String> albumNames = Arrays.asList("a1", "a2");
    	for (int i = 0; i < albumNames.size(); i++) {
    		Album album = artist.makeAlbum(albumNames.get(i));
    		album.setAlbumId(i+1);
    		albums.add(album);
    	}
    	
    	song = ((Album) albums.get(0)).makeSong("leuk liedje");
    	song.setSongId(1);
    }
    
    // reset the mock before each test //
    @Before
    public void setUp() {
        Mockito.reset(entityServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
	
    // test getEntities //
    @Test
    public void testGetEntities_IncorrectTableName() throws Exception {
    	String tableName = "none/";
    	
    	mockMvc.perform(get(API_LOC + tableName))
    		.andExpect(status().isBadRequest());
    		
    	verifyNoMoreInteractions(entityServiceMock);
    }
    
    @Test
    public void testGetEntities_DatabaseAccessThrowsException() throws Exception {
    	String tableName = "artists/";
    	
    	when(entityServiceMock.getEntities(Artist.class))
    		.thenThrow(new EntityServiceException(new Exception("test exception")));
    	
    	mockMvc.perform(get(API_LOC + tableName))
    		.andExpect(status().isInternalServerError());
    	
    	verify(entityServiceMock, times(1)).getEntities(Artist.class);	
    	verifyNoMoreInteractions(entityServiceMock);
    }
    
	@Test
	public void testGetEntities_ArtistsSuccesfullyRetrieved_EmptyList() throws Exception{
		String tableName = "artists/";
		
		when(entityServiceMock.getEntities(Artist.class))
			.thenReturn(new ArrayList<Entity>());
		
		mockMvc.perform(get(API_LOC + tableName))
			.andExpect(status().isNoContent());
		
		verify(entityServiceMock, times(1)).getEntities(Artist.class);
		verifyNoMoreInteractions(entityServiceMock);
	}
    
	@Test
	public void testGetEntities_ArtistsSuccesfullyRetrieved() throws Exception {
		String tableName = "artists/";
		
		when(entityServiceMock.getEntities(Artist.class))
			.thenReturn(artists);
		
		mockMvc.perform(get(API_LOC + tableName))
			.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType))
    		.andExpect(jsonPath("$", hasSize(3)))
    		.andExpect(jsonPath("$[0].artistId", is(1)))
    		.andExpect(jsonPath("$[0].artistName", is("one1")))
    		.andExpect(jsonPath("$[1].artistId", is(2)))
    		.andExpect(jsonPath("$[1].artistName", is("two2")))
    		.andExpect(jsonPath("$[2].artistId", is(3)))
    		.andExpect(jsonPath("$[2].artistName", is("three3")));
		
		verify(entityServiceMock, times(1)).getEntities(Artist.class);
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testGetEntities_AlbumsSuccesFullyRetrieved() throws Exception {
		String tableName = "albums/";
		
		when(entityServiceMock.getEntities(Album.class))
			.thenReturn(albums);
		
		mockMvc.perform(get(API_LOC + tableName))
			.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType))
    		.andExpect(jsonPath("$", hasSize(2)))
    		.andExpect(jsonPath("$[0].albumId", is(1)))
    		.andExpect(jsonPath("$[0].albumName", is("a1")))
    		.andExpect(jsonPath("$[1].albumId", is(2)))
    		.andExpect(jsonPath("$[1].albumName", is("a2")));
		
		verify(entityServiceMock, times(1)).getEntities(Album.class);
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	
	// test getEntity //
	@Test
	public void testGetEntity_IncorrectTableName() throws Exception {
    	String tableName = "none/";
    	int id = 1;
    	
    	mockMvc.perform(get(API_LOC + tableName + id))
    		.andExpect(status().isBadRequest());
    		
    	verifyNoMoreInteractions(entityServiceMock);
	}
	
    @Test
    public void testGetEntity_DatabaseAccessThrowsException() throws Exception {
    	String tableName = "artists/";
    	int id = 1;
    	
    	when(entityServiceMock.getEntityById(Artist.class, id))
    		.thenThrow(new EntityServiceException(new Exception("test exception")));
    	
    	mockMvc.perform(get(API_LOC + tableName + id))
    		.andExpect(status().isInternalServerError());
    	
    	verify(entityServiceMock, times(1)).getEntityById(Artist.class, id);	
    	verifyNoMoreInteractions(entityServiceMock);
    }
	
    @Test
	public void testGetEntity_ArtistsNotFound() throws Exception{
		String tableName = "artists/";
		int id = 1;
		
		when(entityServiceMock.getEntityById(Artist.class, id))
			.thenReturn(null);
		
		mockMvc.perform(get(API_LOC + tableName + id))
			.andExpect(status().isNoContent());

    	verify(entityServiceMock, times(1)).getEntityById(Artist.class, id);	
    	verifyNoMoreInteractions(entityServiceMock);
	}
    
	@Test
	public void testGetEntity_ArtistSuccesfullyRetrieved() throws Exception {
		String tableName = "artists/";
		int id = 1;

		when(entityServiceMock.getEntityById(Artist.class, id))
			.thenReturn(artists.get(0));
		
		mockMvc.perform(get(API_LOC + tableName + id))
			.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType))
    		.andExpect(jsonPath("$.artistId", is(1)))
    		.andExpect(jsonPath("$.artistName", is("one1")));
		
		verify(entityServiceMock, times(1)).getEntityById(Artist.class, id);	
    	verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testGetEntity_SongSuccesfullyRetrieved() throws Exception {
		String tableName = "songs/";
		int id = 1;

		when(entityServiceMock.getEntityById(Song.class, id))
			.thenReturn(song);
		
		mockMvc.perform(get(API_LOC + tableName + id))
			.andExpect(status().isOk())
    		.andExpect(content().contentType(contentType))
    		.andExpect(jsonPath("$.songId", is(1)))
    		.andExpect(jsonPath("$.songName", is("leuk liedje")))
    		.andExpect(jsonPath("$.album.albumId", is(1)))
    		.andExpect(jsonPath("$.album.albumName", is("a1")))
    		.andExpect(jsonPath("$.album.artist.artistId", is(1)))
    		.andExpect(jsonPath("$.album.artist.artistName", is("one1")));
		
		verify(entityServiceMock, times(1)).getEntityById(Song.class, id);	
    	verifyNoMoreInteractions(entityServiceMock);
	}
}
