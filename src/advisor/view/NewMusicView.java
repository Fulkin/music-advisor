package advisor.view;

import advisor.model.Album;
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
 * Created on 07.02.2022
 */

public class NewMusicView extends View<Album> {

    private final List<Album> albumList;

    public NewMusicView(String resourceServer, String accessToken, int pages, HttpClient client) {
        super(resourceServer, accessToken, pages, client);
        albumList = new ArrayList<>();
    }

    @Override
    protected HttpRequest getHttpRequest(String s) {
        String apiPath = String.format("%s%s",
                resourceServer, "new-releases");
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiPath))
                .GET()
                .build();
    }

    @Override
    protected List<Album> getList(HttpResponse<String> response) {
        JsonArray jsonArray = JsonParser.parseString(response.body())
                .getAsJsonObject().get("albums")
                .getAsJsonObject().getAsJsonArray("items");
        for (JsonElement element : jsonArray) {
            String albumName = element.getAsJsonObject()
                    .get("name").getAsString();
            String linkName = element.getAsJsonObject()
                    .get("external_urls").getAsJsonObject()
                    .get("spotify").getAsString();
            List<String> artistList = new ArrayList<>();
            JsonArray artists = element.getAsJsonObject()
                    .getAsJsonArray("artists");
            for (JsonElement artist : artists) {
                String artistName = artist.getAsJsonObject()
                        .get("name").getAsString();
                artistList.add(artistName);
            }
            albumList.add(new Album(albumName, artistList, linkName));
        }
        return albumList;
    }

    @Override
    protected String getData(Album album) {
        return album.getName() + "\n" +
                album.getArtists() + "\n" +
                album.getLink() + "\n";
    }

    @Override
    protected Map<String, String> getCategories() {
        return null;
    }
}
