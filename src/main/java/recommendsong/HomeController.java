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

        // Add 1 top track from most repeated artist.
        // Starting from 50%, chance of recommending next top track will be decreased by half for each top track.
        Track[] mostRepeatedArtistTopTracks = artistService.findTopTracks(mostRepeatedArtist);
        Track mostRepeatedArtistTopTrack = this.getRandomTrack(mostRepeatedArtistTopTracks, 75, userGivenTrackIds, trackSelectionMethod);
        if (mostRepeatedArtistTopTrack != null) {
            recommendedTracks.add(mostRepeatedArtistTopTrack);
        }

        // Add 1 top track from related artist of most repeated artist
        Track[] relatedArtistTopTracks = artistService.getRelatedArtistTopTracks(mostRepeatedArtist);
        Track relatedArtistTopTrack = this.getRandomTrack(relatedArtistTopTracks, 75, userGivenTrackIds, trackSelectionMethod);
        if (mostRepeatedArtistTopTrack != null) {
            recommendedTracks.add(relatedArtistTopTrack);
        }


        return recommendedTracks;
    }

    private Track getRandomTrack(Track[] tracks, int trackSelectionChance, List<String> restrictedTracksIds, PercentChance trackAdditionChance) {
        for (Track track : tracks) {
            int randomNumber = this.generateRandomNumber(0, 100);

            // tracks that will be given to user should not be tracks which user gave us.
            if (!restrictedTracksIds.contains(track.getId()) && randomNumber < trackSelectionChance) {
                return track;
            }

            trackSelectionChance = trackAdditionChance.calculate(trackSelectionChance);
        }
        return null;
    }

    private int generateRandomNumber(int lowerBound, int upperBound) {
        Random random = new Random();
        return random.nextInt(lowerBound, upperBound);
    }
}
