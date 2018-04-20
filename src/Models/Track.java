package Models;

import com.mpatric.mp3agic.ID3v2;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Nick on 3/27/2018.
 */
public class Track
{
    private String path;
    private long lengthInSeconds;
    private String duration;

    public Track(String name, String path, long lengthInSeconds, ID3v2 tags)
    {
        setName(name);

        this.path = path;
        this.lengthInSeconds = lengthInSeconds;

        if(tags != null)
        {
            setArtist(tags.getArtist());
            setAlbum(tags.getAlbum());
            setYear(tags.getYear());
        }
    }

    public String getPath()
    {
        return path;
    }

    public long getlengthInSeconds()
    {
        return lengthInSeconds;
    }

    public String getDuration()
    {
        double totalDurationInSeconds = getlengthInSeconds();

        int totalSeconds = (int)totalDurationInSeconds;

        //int hours = totalSecs / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    // Bindable properties.

    private StringProperty name = new SimpleStringProperty();

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

    private StringProperty artist = new SimpleStringProperty();

    public StringProperty artistProperty()
    {
        return artist;
    }

    public final String getArtist()
    {
        return artist.get();
    }

    public final void setArtist(String artist)
    {
        this.artist.set(artist);
    }

    private StringProperty album = new SimpleStringProperty();

    public StringProperty albumProperty()
    {
        return album;
    }

    public final String getAlbum()
    {
        return album.get();
    }

    public final void setAlbum(String album)
    {
        this.album.set(album);
    }

    private StringProperty year = new SimpleStringProperty();

    public StringProperty yearProperty()
    {
        return year;
    }

    public final String getYear()
    {
        return year.get();
    }

    public final void setYear(String year)
    {
        this.year.set(year);
    }
}
