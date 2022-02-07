package advisor.view;

import advisor.model.Category;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fulkin
 * Created on 07.02.2022
 */

public class CategoriesView extends View<Category> {

    protected Map<String, String> categoriesWithId;
    private final List<Category> categoryList;

    public CategoriesView(String resourceServer, String accessToken, int pages, HttpClient client) {
        super(resourceServer, accessToken, pages, client);
        categoryList = new ArrayList<>();
        categoriesWithId = new HashMap<>();
    }

    @Override
    protected HttpRequest getHttpRequest(String s) {
        String apiPath = String.format("%s%s",
                resourceServer, "categories");
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiPath))
                .GET()
                .build();
    }

    @Override
    protected List<Category> getList(HttpResponse<String> response) {
        JsonArray jsonArray = JsonParser.parseString(response.body())
                .getAsJsonObject().getAsJsonObject("categories")
                .getAsJsonArray("items");
        for (JsonElement element : jsonArray) {
            String playlistName = element.getAsJsonObject()
                    .get("name").getAsString();
            String playlistId = element.getAsJsonObject()
                    .get("id").getAsString();

            categoriesWithId.put(playlistName, playlistId);
            categoryList.add(new Category(playlistName));
        }
        return categoryList;
    }

    @Override
    protected String getData(Category category) {
        return category.getName() + "\n";
    }

    @Override
    protected Map<String, String> getCategories() {
        getPage();
        return categoriesWithId;
    }
}
