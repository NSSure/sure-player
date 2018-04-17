package Models;

import com.mpatric.mp3agic.ID3v2;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Nick on 3/27/2018.
 */
public class Track
{
    private String path;
    private String artist;
    private String album;
    private String year;
    private ID3v2 tags;

    private StringProperty name = new SimpleStringProperty();

    public Track(String name, String path)
    {
        setName(name);
        this.path = path;
    }

    public String getArtist() {
        if(tags != null){
            return tags.getArtist();
        }

        return "";
    }

    public String getAlbum() {
        if(tags != null){
            return tags.getArtist();
        }

        return "";
    }

    public String getYear() {
        if(tags != null){
            return tags.getYear();
        }

        return "";
    }

    public String getPath()
    {
        return path;
    }

    public void setTags(ID3v2 tags) {
        this.tags = tags;
    }

    // Bindable properties.

    public StringProperty nameProperty()
    {
        return name;
    }

    public final String getName()
    {
        return name.get();
    }

    public final void setName(String value)
    {
        this.name.set(value);
    }
}
