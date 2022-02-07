package advisor;

import advisor.view.*;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

/**
 * @author Fulkin
 * Created on 25.01.2022
 */

public class ConnectServer {
    private static final String API_PATH = "/v1/browse/";
    private static ConnectServer connectServer = null;
    private static final HttpClient CLIENT = HttpClient.newBuilder().build();
    private static String resourceServer;
    private static String accessToken = "";
    private static String code;
    private static int pages = 5;
    private View view;

    private ConnectServer() {
        resourceServer = "https://api.spotify.com" + API_PATH;
        code = "";
        view = null;
    }

    public static ConnectServer getInstance() {
        if (connectServer == null) {
            connectServer = new ConnectServer();
        }
        return connectServer;
    }


    public void getAccess(String server) {
        try (
                FileInputStream fis = new FileInputStream("./src/resources/config.properties")
                //FileInputStream fis = new FileInputStream("./Music Advisor/task/src/resources/config.properties")
        ) {
            if ("".equals(server)) {
                server = "https://accounts.spotify.com";
            }
            Properties property = new Properties();

            System.out.println("use this link to request the access code:");
            property.load(fis);

            String uri = String.format("%s%s?client_id=%s&redirect_uri=%s&response_type=code",
                    server,
                    property.getProperty("uri.auth"),
                    property.getProperty("uri.client"),
                    property.getProperty("uri.redirect")
            );
            System.out.println(uri);
            System.out.println("waiting for code...");
            createServer();
            System.out.println("code received");
            System.out.println("making http request for access_token...");
            accessToken = request(server, property);
            System.out.println("Success!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createServer() {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);

            server.createContext("/", exchange -> {
                String query = exchange.getRequestURI().getQuery();
                String response;
                if (query != null && query.contains("code")) {
                    code = query;
                    response = "Got the code. Return back to your program.";
                } else {
                    response = "Authorization code not found. Try again.";
                }
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            });

            server.start();

            while (code.length() == 0) {
                Thread.sleep(100);
            }
            server.stop(2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String request(String server, Properties property) throws IOException, InterruptedException {
        String uri = String.format("%s%s",
                server,
                property.getProperty("uri.post")
        );

        String requests = String.format("grant_type=authorization_code&%s&redirect_uri=%s&client_id=%s&client_secret=%s",
                code,
                property.getProperty("uri.redirect"),
                property.getProperty("uri.client"),
                property.getProperty("uri.client_secret")
        );

        URI uriToConnect = URI.create(uri);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriToConnect)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requests))
                .build();
        var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        return JsonParser.parseString(response.body())
                .getAsJsonObject()
                .get("access_token").getAsString();
    }

    public String takeAdvice(String[] strings) {
        String advice;
        switch (strings[0]) {
            case "new":
                view = new NewMusicView(resourceServer, accessToken, pages, CLIENT);
                advice = view.getPage();
                break;
            case "featured":
                view = new FeaturedMusicView(resourceServer, accessToken, pages, CLIENT);
                advice = view.getPage();
                break;
            case "categories":
                view = new CategoriesView(resourceServer, accessToken, pages, CLIENT);
                advice = view.getPage();
                break;
            case "playlists":
                view = new PlaylistCategoryView(resourceServer, accessToken, pages, CLIENT);
                advice = view.getPage(strings);
                break;
            case "prev":
                if (view != null) {
                    advice = view.prev();
                } else {
                    advice = "Choose other command";
                }
                break;
            case "next":
                if (view != null) {
                    advice = view.next();
                } else {
                    advice = "Choose other command";
                }
                break;
            default:
                advice = "Unknown command\n";
        }
        return advice;
    }

    public static void setResourceServer(String resourceServerName) {
        if (!"".equals(resourceServerName)) {
            resourceServer = resourceServerName + API_PATH;
        }
    }


    public static void setPage(String page) {
        if (!"".equals(page)) {
            pages = Integer.parseInt(page);
        }
    }
}
