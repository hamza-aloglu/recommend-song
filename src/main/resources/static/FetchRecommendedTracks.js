async function fetchRecommendedTracks(data) {
    // always try using const rather than let
    const response = await fetch("http://localhost:8080/tracks", {
        method: 'POST',
        body: JSON.stringify(data)
    });

    if (response.ok) {
        return await response.json();
    } else {
        alert("HTTP response not ok");
    }
}