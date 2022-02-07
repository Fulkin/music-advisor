package advisor.model;

/**
 * @author Fulkin
 * Created on 05.02.2022
 */

public class Playlist {
    String name;
    String link;

    public Playlist(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
