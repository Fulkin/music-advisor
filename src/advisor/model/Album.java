package advisor.model;

import java.util.List;

/**
 * @author Fulkin
 * Created on 05.02.2022
 */

public class Album {
    String name;
    List<String> artists;
    String link;

    public Album(String name, List<String> artists, String link) {
        this.name = name;
        this.artists = artists;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getLink() {
        return link;
    }
}
