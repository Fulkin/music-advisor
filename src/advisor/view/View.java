package advisor.view;

import advisor.model.Category;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Fulkin
 * Created on 05.02.2022
 */

public abstract class View<T> {

    private List<T> tList;
    private Map<String, String> categoriesId;
    private String idCategories = "";
    protected final String resourceServer;
    protected final String accessToken;
    protected final HttpClient client;
    protected final int objToPage;
    private int totalPages;
    private int firstElem;
    private int currentPage;

    protected View(String resourceServer, String accessToken, int objToPage, HttpClient client) {
        this.resourceServer = resourceServer;
        this.accessToken = accessToken;
        this.client = client;
        this.objToPage = objToPage;
        firstElem = 0;
        totalPages = 5;
        currentPage = 0;
    }

    private static String checkError(HttpResponse<String> response) {
        JsonElement jsonElement = JsonParser.parseString(response.body())
                .getAsJsonObject().get("error");
        if (jsonElement != null) {
            return jsonElement.getAsJsonObject().get("message").getAsString();
        } else {
            return null;
        }
    }

    public String getPage(String[] s) {
        categoriesId = getCategories();
        if (categoriesId == null) {
            throw new IllegalArgumentException();
        }
        StringBuilder category = new StringBuilder();
        for (int i = 1; i < s.length; i++) {
            category.append(s[i]).append(" ");
        }
        idCategories = categoriesId.get(category.toString().trim());
        if (idCategories == null) {
            return "Unknown category name.\n";
        }
        return getPage();
    }

    public String getPage() {
        HttpRequest request = getHttpRequest(idCategories);
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String error = checkError(response);
        if (error != null) {
            return error + "\n";
        }

        tList = getList(response);

        totalPages = tList.size() / objToPage;
        int end = firstElem + objToPage;
        if (tList.size() - totalPages * objToPage != 0) {
            totalPages++;
        }

        StringBuilder sb = new StringBuilder();
        String space = getSpace();
        for (int i = firstElem; i < end; i++) {
            sb.append(getData(tList.get(i))).append(space);
        }
        currentPage = 1;
        sb.append("---PAGE ").append(currentPage)
                .append(" OF ").append(totalPages)
                .append("---").append("\n");
        return sb.toString();
    }

    public String prev() {
        if (currentPage == 1) {
            return "No more pages.\n";
        }
        StringBuilder sb = new StringBuilder();
        firstElem -= objToPage;
        int end = firstElem + objToPage;
        for (int i = firstElem; i < end; i++) {
            sb.append(getData(tList.get(i))).append("\n");
        }
        currentPage--;
        sb.append("---PAGE ").append(currentPage)
                .append(" OF ").append(totalPages)
                .append("---").append("\n");
        return sb.toString();
    }

    public String next() {
        if (currentPage == Math.ceil(tList.size() / objToPage)) {
            return "No more pages.\n";
        }
        StringBuilder sb = new StringBuilder();
        firstElem += objToPage;
        int end = firstElem + objToPage;
        end = end > tList.size() ? (end - (end - tList.size())) : end;
        for (int i = firstElem; i < end; i++) {
            sb.append(getData(tList.get(i))).append("\n");
        }
        currentPage++;
        sb.append("---PAGE ").append(currentPage)
                .append(" OF ").append(totalPages)
                .append("---").append("\n");
        return sb.toString();
    }

    private String getSpace() {
        String space = "\n";
        if (tList.get(0) instanceof Category) {
            space = "";
        }
        return space;
    }

    protected abstract HttpRequest getHttpRequest(String s);

    protected abstract List<T> getList(HttpResponse<String> response);

    protected abstract String getData(T t);

    protected abstract Map<String, String> getCategories();
}
