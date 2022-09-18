package service;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackService extends SpotifyService {
    public List<String> findTrackIdFromName(String[] trackNames) throws IOException, ParseException, SpotifyWebApiException {
        List<String> trackIds = new ArrayList<>();

        for (String trackName: trackNames) {
            String trackId = this.findTrackIdFromName(trackName);
            trackIds.add(trackId);
        }
        return trackIds;
    }

    public String findTrackIdFromName(String trackName) throws IOException, ParseException, SpotifyWebApiException {
        return spotifyApi.searchTracks(trackName)
                .limit(1)
                .build()
                .execute()
                .getItems()[0]
                .getId();
    }

    public Track getTrack(String Id) throws IOException, ParseException, SpotifyWebApiException {
        return spotifyApi.getTrack(Id)
                .build()
                .execute();
    }
}
