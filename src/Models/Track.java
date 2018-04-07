package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Nick on 3/27/2018.
 */
public class Track
{
    private String path;

    public Track(String name, String path)
    {
        setName(name);
        this.path = path;
    }

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

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
}
