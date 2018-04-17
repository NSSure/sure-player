package Models;

import java.util.ArrayList;

/**
 * Created by Nick on 4/8/2018.
 */
public class Playlist
{
    public String name;
    private String description;
    private String ImagePath;
    private ArrayList<String> TrackPaths;

    public Playlist(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public ArrayList<String> getTrackPaths() {
        return TrackPaths;
    }

    public void setTrackPaths(ArrayList<String> trackPaths) {
        TrackPaths = trackPaths;
    }

    public void addTrack(String path) {
        TrackPaths.add(path);
    }
}
