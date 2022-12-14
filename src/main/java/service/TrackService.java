package service;

import org.apache.hc.core5.http.ParseException;
import recommendsong.PercentChance;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackService extends SpotifyService {
    public List<String> findTrackIdFromName(String[] trackNames) throws IOException, ParseException, SpotifyWebApiException {
        List<String> trackIds = new ArrayList<>();

        for (String trackName : trackNames) {
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

    // Starting from 50%, chance of recommending next top track will be decreased by half for each top track.
    public Track getRandomTrack(Track[] tracks, int trackSelectionChance, List<String> restrictedTracksIds, PercentChance trackAdditionChance) {
        if (tracks == null) {
            return null;
        }

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
}
