package recommendsong;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    @PostMapping("/tracks")
    String recommendSongs(@RequestBody String trackNames) throws IOException, ParseException, SpotifyWebApiException {
        // Find artists of 5 songs
        String[] trackNamesArray = trackNames.split(",");
        String spotifyAccessToken = "";

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(spotifyAccessToken)
                .build();

        List<Artist> artists = new ArrayList<>();

        for (String trackName: trackNamesArray) {
            // Find artist id
            Track track = spotifyApi.searchTracks(trackName).limit(1).build().execute().getItems()[0];
            String  artistId = track.getArtists()[0].getId();

            // Find and add artist
            Artist artist = spotifyApi.getArtist(artistId).build().execute();
            artists.add(artist);
        }

        // Find most duplicated artist


        // Send random 3 songs of that artist which any of them is not any of given songs.


        return trackNames;
    }
}
