package recommendsong;

import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.*;

@RestController
public class HomeController {

    @PostMapping("/tracks")
    List<String> recommendTracks(@RequestBody String trackNames) throws IOException, ParseException, SpotifyWebApiException {
        // Find artists of 5 songs.Artists represented by id.
        String[] trackNamesArray = trackNames.split(",");
        String spotifyAccessToken = "BQAXjRh5qb9CQtLcuBeNUpGo441a8UE7gPgdvCzu94gjGr4AiX0cmAJE1AQWe9zLAGVS3O4GMA24bI91W11JyBFX_8RRASpKilFpQ4hf8M5WsMD1Afrt5ysBU4IPX8bJZux6qWThi4aZFRWN42JbsPmsaNA0Xo4gokpb9On1kUZUvVbINmo3UH3oxXSaZHc";

        System.out.println("user given track names");
        for (String track: trackNamesArray) {
            System.out.println(track);

        }

        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(spotifyAccessToken)
                .build();

        Map<String, Integer> artistsWithFrequency = new HashMap<>();
        List<String> userGivenTrackIds = new ArrayList<>();

        for (String trackName : trackNamesArray) {
            Track track = spotifyApi.searchTracks(trackName).limit(1).build().execute().getItems()[0];

            // Every user given track should be unique...
            // Models: track, artist
            if (userGivenTrackIds.contains(track.getId())) {
                continue;
            }

            System.out.println("user given track: " + track.getName());

            String artistId = track.getArtists()[0].getId();
            userGivenTrackIds.add(track.getId());

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

        Artist mostRepeatedArtist = spotifyApi.getArtist(mostRepeatedArtistId).build().execute();
        System.out.println("most repeated artist name: " + mostRepeatedArtist.getName());


        // Send random 3 songs of that artist which any of them is not any of given songs.
        Track[] mostRepeatedArtistsTracks = spotifyApi.getArtistsTopTracks(mostRepeatedArtist.getId(), CountryCode.TR).build().execute();


        List<String> recommendedTracks = new ArrayList<>();

        // Starting from 100%, chance of recommending next top track will be decreased by half for each top track.
        int percentChanceForTrackAddition = 100;
        for (Track mostRepeatedArtistsTrack: mostRepeatedArtistsTracks) {
            if (recommendedTracks.size() >= 3) {
                break;
            }

            int randomNumber = this.generateRandomNumber(0, 100);

            if (!userGivenTrackIds.contains(mostRepeatedArtistsTrack.getId()) && randomNumber < percentChanceForTrackAddition) {
                recommendedTracks.add(mostRepeatedArtistsTrack.getName());
                percentChanceForTrackAddition /= 2;
            }
        }

        return recommendedTracks;
    }

    private int generateRandomNumber(int lowerBound, int upperBound) {
        Random random = new Random();
        return random.nextInt(lowerBound, upperBound);
    }
}
