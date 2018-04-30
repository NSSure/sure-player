package Models;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 4/8/2018 by Nick Gordon
 */
public class Playlist
{
    public UUID ID;
    public String name;
    private String description;
    private String imagePath;
    private ArrayList<Track> tracks;

    public Playlist(String name, String description)
    {
        this.ID = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.imagePath = "";
        this.tracks = new ArrayList<>();
    }

    public UUID getID()
    {
        return this.ID;
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
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        imagePath = imagePath;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }
}
