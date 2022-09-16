package service;

import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.SpotifyApi;

public abstract class SpotifyService {
    private final String spotifyAccessToken = "BQAXjRh5qb9CQtLcuBeNUpGo441a8UE7gPgdvCzu94gjGr4AiX0cmAJE1AQWe9zLAGVS3O4GMA24bI91W11JyBFX_8RRASpKilFpQ4hf8M5WsMD1Afrt5ysBU4IPX8bJZux6qWThi4aZFRWN42JbsPmsaNA0Xo4gokpb9On1kUZUvVbINmo3UH3oxXSaZHc";

    protected CountryCode country = CountryCode.TR;
    protected final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(this.spotifyAccessToken)
            .build();
}
