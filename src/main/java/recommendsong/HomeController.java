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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @PostMapping("/tracks")
    String recommendSongs(@RequestBody String trackNames) throws IOException, ParseException, SpotifyWebApiException {
        // Find artists of 5 songs.Artists represented by id.
        String[] trackNamesArray = trackNames.split(",");
        String spotifyAccessToken = "BQAOmB6w4NEU3joyCtw0O0D4B7lJGPX82E-nR2Da8RdCTPuc09wk5LvaCj0qK093Zy1GjiVl2vkij33DdyevGCAKWf3-Kw24PJAhYqTu-tBCBgGb1sW5pQV5ArHO3w0u7tx8gQiv2S0KWrm5CewdgKWVl738olkV50SqDZNfhr-SRphEKuNhqhCFG38JgHY";

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(spotifyAccessToken)
                .build();

        Map<String, Integer> artistsWithFrequency = new HashMap<>();

        for (String trackName : trackNamesArray) {
            Track track = spotifyApi.searchTracks(trackName).limit(1).build().execute().getItems()[0];
            String artistId = track.getArtists()[0].getId();

            if (artistsWithFrequency.containsKey(artistId)) {
                int freq = artistsWithFrequency.get(artistId);
                freq++;
                artistsWithFrequency.put(artistId, freq);
            } else {
                artistsWithFrequency.put(artistId, 1);
            }

        }

        // Find most repeated artist.
        String mostRepeatedArtistId = null;
        int maxRepeatedNumber = 0;
        for (String key : artistsWithFrequency.keySet()) {
            int artistOccurrence = artistsWithFrequency.get(key);

            if (artistOccurrence > maxRepeatedNumber) {
                mostRepeatedArtistId = key;
                maxRepeatedNumber = artistOccurrence;
            }
        }

        Artist artist = spotifyApi.getArtist(mostRepeatedArtistId).build().execute();


        // Send random 3 songs of that artist which any of them is not any of given songs.


        return trackNames;
    }
}
