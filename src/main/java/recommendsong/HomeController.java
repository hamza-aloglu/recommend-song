package recommendsong;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import service.ArtistService;
import service.TrackService;

import java.io.IOException;
import java.util.*;

@RestController
public class HomeController {

    @GetMapping("/trackJson")
    Track giveMeTrack() throws IOException, ParseException, SpotifyWebApiException {
        TrackService trackService = new TrackService();
        String trackId = trackService.findTrackIdFromName("DRE");

        return trackService.getTrack(trackId);

    }

    @CrossOrigin
    @PostMapping("/tracks")
    List<Track> recommendTracks(@RequestBody String trackNames) throws IOException, ParseException, SpotifyWebApiException {
        String[] trackNamesArray = trackNames.split(",");

        TrackService trackService = new TrackService();
        List<String> userGivenTrackIds = trackService.findTrackIdFromName(trackNamesArray);

        ArtistService artistService = new ArtistService();
        Map<String, Integer> artistIdsWithFrequency = artistService.getArtistIdsWithFrequency(userGivenTrackIds);

        Artist mostRepeatedArtist = artistService.findMostRepeatedArtist(artistIdsWithFrequency);


        //-- Recommendation logic --

        List<Track> recommendedTracks = new ArrayList<>();
        PercentChance trackSelectionMethod = (chance -> chance / 2);




        return recommendedTracks;
    }

    

    private int generateRandomNumber(int lowerBound, int upperBound) {
        Random random = new Random();
        return random.nextInt(lowerBound, upperBound);
    }
}
