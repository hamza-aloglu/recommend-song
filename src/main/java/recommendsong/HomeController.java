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
    @CrossOrigin
    @PostMapping("/tracks")
    List<Track> recommendTracks(@RequestBody String trackNames) throws IOException, ParseException, SpotifyWebApiException {
        String[] trackNamesArray = trackNames.split(",");

        TrackService trackService = new TrackService();
        List<String> userGivenTrackIds = trackService.findTrackIdFromName(trackNamesArray);

        ArtistService artistService = new ArtistService();
        Map<String, Integer> artistIdsWithFrequency = artistService.getArtistIdsWithFrequency(userGivenTrackIds);

        Artist mostRepeatedArtist = artistService.findMostRepeatedArtist(artistIdsWithFrequency);

        // -- Recommendation logic --
        List<Track> recommendedTracks = new ArrayList<>();
        PercentChance trackSelectionMethod = (chance -> chance / 2);

        // Add 1 top track from most repeated artist.
        Track[] mostRepeatedArtistTopTracks = artistService.findTopTracks(mostRepeatedArtist);
        Track mostRepeatedArtistTopTrack = trackService.getRandomTrack(mostRepeatedArtistTopTracks, 75, userGivenTrackIds, trackSelectionMethod);
        if (mostRepeatedArtistTopTrack != null) {
            recommendedTracks.add(mostRepeatedArtistTopTrack);
        }

        // Add 1 top track from related artist of most repeated artist
        Track[] relatedArtistTopTracks = artistService.getRelatedArtistTopTracks(mostRepeatedArtist);
        Track relatedArtistTopTrack = trackService.getRandomTrack(relatedArtistTopTracks, 75, userGivenTrackIds, trackSelectionMethod);
        if (relatedArtistTopTrack != null) {
            recommendedTracks.add(relatedArtistTopTrack);
        }

        // Add 1 top track from random artist's top track.
        Track[] topTracksOfRandomArtist = artistService.getTopTrackOfRandomArtist(artistIdsWithFrequency, mostRepeatedArtist);
        Track randomArtistTopTrack = trackService.getRandomTrack(topTracksOfRandomArtist, 75, userGivenTrackIds, trackSelectionMethod);
        if (randomArtistTopTrack != null) {
            recommendedTracks.add(randomArtistTopTrack);
        }

        return recommendedTracks;
    }
}
