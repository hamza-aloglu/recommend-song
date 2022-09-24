package service;

import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.SpotifyApi;

import java.util.Random;

public abstract class SpotifyService {
    // Dynamic access token will be implemented
    /*
     * with certain parameters you need to go to "https://accounts.spotify.com/authorize?" you'll receive code if user accept
     * with that code you can get access and refresh token and set it t spotify api and use it. I don't know how long it last
     */

    private final String spotifyAccessToken = "BQC1hS6WhctVbqZ3-8b4m_VeP2OSV2hmN5CLmOy4ru6d17X62U67-ewUPGxZUCtKhWttVamq8565InFg--rshQQDhgJhXBn-Vjh5HnuuSn_5CkZha-GwqvdJxjSiXpZTBIUNjUfmGldfrz6gEXVQoyYcJOiJd-VnBa2uu6QIA9bNTFXKEI4ARGWYUPvn1Rs";
    protected CountryCode country = CountryCode.TR;
    protected final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(this.spotifyAccessToken)
            .build();

    protected int generateRandomNumber(int lowerBound, int upperBound) {
        Random random = new Random();
        return random.nextInt(lowerBound, upperBound);
    }
}
