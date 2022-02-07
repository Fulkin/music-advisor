package advisor.view;

import advisor.model.Playlist;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fulkin
 * Created on 05.02.2022
 */

public class FeaturedMusicView extends View<Playlist> {

    private final List<Playlist> playlistList;

    public FeaturedMusicView(String resourceServer, String accessToken, int pages, HttpClient client) {
        super(resourceServer, accessToken, pages, client);
        playlistList = new ArrayList<>();
    }

    @Override
    protected HttpRequest getHttpRequest(String s) {
        String apiPath = String.format("%s%s",
                resourceServer, "featured-playlists");
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiPath))
                .GET()
                .build();
    }

    @Override
    protected List<Playlist> getList(HttpResponse<String> response) {
        JsonArray jsonArray = JsonParser.parseString(response.body())
                .getAsJsonObject().getAsJsonObject("playlists")
                .getAsJsonArray("items");
        for (JsonElement element : jsonArray) {
            String playlistName = element.getAsJsonObject()
                    .get("name").getAsString();
            String linkName = element.getAsJsonObject()
                    .get("external_urls").getAsJsonObject()
                    .get("spotify").getAsString();
            playlistList.add(new Playlist(playlistName, linkName));
        }
        return playlistList;
    }

    @Override
    protected String getData(Playlist playlist) {
        return playlist.getName() + "\n" + playlist.getLink() + "\n";
    }

    @Override
    protected Map<String, String> getCategories() {
        return null;
    }
}
