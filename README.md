# Recommend Song
A simple app that recommends you songs based on your selection of songs.

## Demo

![Untitled_ Sep 24, 2022 11_45 AM](https://user-images.githubusercontent.com/74200100/192089279-1dce146f-57a4-47bc-91b0-e1bf0633a2f2.gif)

## Implementation

[Spotify's web api](https://developer.spotify.com/documentation/web-api/) used as a data source thanks to [this library](https://github.com/spotify-web-api-java/spotify-web-api-java).

Written [service layer](https://github.com/hamza-aloglu/recommend-song/tree/main/src/main/java/service) on top of spotify library to provide data to controller and hence to my frontend.

### Recommendation logic

Recommended songs are taken from artists of songs given by the user. At most 3 songs are recommended.

- 1st song is taken from top songs of most recurred artist among user given songs.
- 2nd song is taken from top songs of artist which is similar artist with most recurred artist.
- 3rd song is taken from top songs of random artist among user given songs which is not most recurred artist.

Each song randomly selected from top songs. Selection method of each song is different and implemented with lambda expression.

<br><br><br>
------------------------------------------------------------------------------------------------------

#### TODO

- Real time song selection
- Dynamic spotify access token
- Run with docker

<br><br>

Backend:  Java/Spring

Frontend: JavaScript/CSS
