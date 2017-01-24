package studycase.spring.test.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public void testGetEntities_ServiceAccessThrowsException() throws Exception {
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
    public void testGetEntity_ServiceAccessThrowsException() throws Exception {
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


	// test addArtist
	@Test
	public void testAddArtist_InvalidObjectNotAnArtist() throws Exception{
		String tableName = "artists/";
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(new String()))
				)
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddArtist_InvalidObjectArtistNameZeroLength() throws Exception{
		String tableName = "artists/";
		
		Artist artist = new Artist("");
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(artist))
				)
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddArtist_ServiceAccessThrowsException() throws Exception{
		String tableName = "artists/";
		
		Artist artist = (Artist) artists.get(0);

    	when(entityServiceMock.addArtist(artist.getArtistName()))
    		.thenThrow(new EntityServiceException(new Exception("test exception")));
		
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(artist))
				)
				.andExpect(status().isInternalServerError());
		
		verify(entityServiceMock, times(1)).addArtist("one1");
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddArtist_SuccesfullyAdded() throws Exception{
		String tableName = "artists/";
		
		Artist artist = (Artist) artists.get(0);
		artist.setArtistId(1);
		
		when(entityServiceMock.addArtist(artist.getArtistName()))
			.thenReturn(artist);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(artist))
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.artistName", is("one1")))
				.andExpect(jsonPath("$.artistId", is(1)));

		verify(entityServiceMock, times(1)).addArtist("one1");
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	// test addAlbum
	@Test
	public void testAddAlbum_InvalidObjectNotAnAlbum() throws Exception{
		String tableName = "albums/";
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(new String()))
				)
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddAlbum_InvalidObjectAlbumNameZeroLength() throws Exception{
		String tableName = "albums/";
		
		Artist artist = (Artist) artists.get(0);
		Album album = artist.makeAlbum("");
		
		when(entityServiceMock.getEntityById(Artist.class, artist.getArtistId()))
			.thenReturn(artist);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(album))
				)
				.andExpect(status().isBadRequest());

		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddAlbum_InvalidObjectArtistNotFound() throws Exception{
		String tableName = "albums/";
		
		Artist artist = (Artist) artists.get(0);
		Album album = (Album) albums.get(0);

		when(entityServiceMock.getEntityById(Artist.class, artist.getArtistId()))
			.thenReturn(null);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(album))
				)
				.andExpect(status().isBadRequest());
		
		verify(entityServiceMock, times(1)).getEntityById(Artist.class, artist.getArtistId());
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	
	@Test
	public void testAddAlbum_ServiceAccessFindArtistThrowsException() throws Exception{
		String tableName = "albums/";

		Artist artist = (Artist) artists.get(0);
		Album album = (Album) albums.get(0);
		
		when(entityServiceMock.getEntityById(Artist.class, artist.getArtistId()))
			.thenThrow(new EntityServiceException(new Exception("test exception")));
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(album))
				)
				.andExpect(status().isInternalServerError());
		
		verify(entityServiceMock, times(1)).getEntityById(Artist.class, artist.getArtistId());
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddAlbum_ServiceAccessAddAlbumThrowsException() throws Exception{
		String tableName = "albums/";

		Artist artist = (Artist) artists.get(0);
		Album album = (Album) albums.get(0);
		
		when(entityServiceMock.getEntityById(Artist.class, artist.getArtistId()))
			.thenReturn(artist);
		when(entityServiceMock.addAlbum(artist, album.getAlbumName()))
			.thenThrow(new EntityServiceException(new Exception("test exception")));
		
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(album))
				)
				.andExpect(status().isInternalServerError());
		
		verify(entityServiceMock, times(1)).getEntityById(Artist.class, artist.getArtistId());
		verify(entityServiceMock, times(1)).addAlbum(artist, "a1");
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	
	@Test
	public void testAddAlbum_SuccesfullyAdded() throws Exception{
		String tableName = "albums/";

		Artist artist = (Artist) artists.get(0);
		Album album = (Album) albums.get(0);
		
		when(entityServiceMock.getEntityById(Artist.class, artist.getArtistId()))
			.thenReturn(artist);
		when(entityServiceMock.addAlbum(artist, album.getAlbumName()))
			.thenReturn(album);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(album))
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.albumName", is("a1")))
				.andExpect(jsonPath("$.albumId", is(1)))
				.andExpect(jsonPath("$.artist.artistName", is("one1")))
				.andExpect(jsonPath("$.artist.artistId", is(1)));
		
		verify(entityServiceMock, times(1)).getEntityById(Artist.class, artist.getArtistId());
		verify(entityServiceMock, times(1)).addAlbum(artist, "a1");
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	// helper method for converting object to JSON //
	
	public static byte[] convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

	// test addSong
	@Test
	public void testAddSong_InvalidObjectNotAnSong() throws Exception{
		String tableName = "songs/";
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(new String()))
				)
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddSong_InvalidObjectSongNameZeroLength() throws Exception{
		String tableName = "songs/";
		
		Album album = (Album) albums.get(0);
		Song song = album.makeSong("");
		
		when(entityServiceMock.getEntityById(Album.class, album.getAlbumId()))
			.thenReturn(album);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(song))
				)
				.andExpect(status().isBadRequest());

		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddSong_InvalidObjectAlbumNotFound() throws Exception{
		String tableName = "songs/";
		
		Album album = (Album) albums.get(0);

		when(entityServiceMock.getEntityById(Album.class, album.getAlbumId()))
			.thenReturn(null);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(song))
				)
				.andExpect(status().isBadRequest());
		
		verify(entityServiceMock, times(1)).getEntityById(Album.class, album.getAlbumId());
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddSong_ServiceAccessFindAlbumThrowsException() throws Exception{
		String tableName = "songs/";

		Album album = (Album) albums.get(0);
		
		when(entityServiceMock.getEntityById(Album.class, album.getAlbumId()))
			.thenThrow(new EntityServiceException(new Exception("test exception")));
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(song))
				)
				.andExpect(status().isInternalServerError());
		
		verify(entityServiceMock, times(1)).getEntityById(Album.class, album.getAlbumId());
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddSong_ServiceAccessAddSongThrowsException() throws Exception{
		String tableName = "songs/";

		Album album = (Album) albums.get(0);
		
		when(entityServiceMock.getEntityById(Album.class, album.getAlbumId()))
			.thenReturn(album);
		when(entityServiceMock.addSong(album, song.getSongName()))
			.thenThrow(new EntityServiceException(new Exception("test exception")));
		
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(song))
				)
				.andExpect(status().isInternalServerError());
		
		verify(entityServiceMock, times(1)).getEntityById(Album.class, album.getAlbumId());
		verify(entityServiceMock, times(1)).addSong(album, "leuk liedje");
		verifyNoMoreInteractions(entityServiceMock);
	}
	
	@Test
	public void testAddSong_SuccesfullyAdded() throws Exception{
		String tableName = "songs/";

		Album album = (Album) albums.get(0);
		
		when(entityServiceMock.getEntityById(Album.class, album.getAlbumId()))
			.thenReturn(album);
		when(entityServiceMock.addSong(album, song.getSongName()))
			.thenReturn(song);
		
		mockMvc.perform(post(API_LOC + tableName)
				.contentType(contentType)
				.content(convertObjectToJson(song))
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.songName", is("leuk liedje")))
				.andExpect(jsonPath("$.songId", is(1)))
				.andExpect(jsonPath("$.album.albumName", is("a1")))
				.andExpect(jsonPath("$.album.albumId", is(1)))
				.andExpect(jsonPath("$.album.artist.artistName", is("one1")))
				.andExpect(jsonPath("$.album.artist.artistId", is(1)));
		
		verify(entityServiceMock, times(1)).getEntityById(Album.class, album.getAlbumId());
		verify(entityServiceMock, times(1)).addSong(album, "leuk liedje");
		verifyNoMoreInteractions(entityServiceMock);
	}

}
