package service;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistService extends SpotifyService {
    public Map<String, Integer> getArtistsWithFrequency(List<String> trackIds) throws IOException, ParseException, SpotifyWebApiException {
        Map<String, Integer> artistsWithFrequency = new HashMap<>();

        for (String trackId : trackIds) {
            Track track = spotifyApi.getTrack(trackId).build().execute();
            String artistId = track.getArtists()[0].getId();

            if (artistsWithFrequency.containsKey(artistId)) {
                int freq = artistsWithFrequency.get(artistId);
                freq++;
                artistsWithFrequency.put(artistId, freq);
            } else {
                artistsWithFrequency.put(artistId, 1);
            }
        }

        return artistsWithFrequency;
    }

    public Artist findMostRepeatedArtist(Map<String, Integer> artistsWithFrequency) throws IOException, ParseException, SpotifyWebApiException {
        String mostRepeatedArtistId = null;
        int maxRepeatedNumber = 0;
        for (String key : artistsWithFrequency.keySet()) {
            int artistOccurrence = artistsWithFrequency.get(key);

            if (artistOccurrence > maxRepeatedNumber) {
                mostRepeatedArtistId = key;
                maxRepeatedNumber = artistOccurrence;
            }
        }

        return spotifyApi.getArtist(mostRepeatedArtistId).build().execute();
    }

    public Track[] findTopTracks(Artist artist) throws IOException, ParseException, SpotifyWebApiException {
        return spotifyApi.getArtistsTopTracks(artist.getId(), this.country).build().execute();
    }

    public Track[] getRelatedArtistTopTracks(Artist artist) throws IOException, ParseException, SpotifyWebApiException {
        Artist[] relatedArtist = spotifyApi.getArtistsRelatedArtists(artist.getId()).build().execute();
        return this.findTopTracks(relatedArtist[0]);
    }
}
