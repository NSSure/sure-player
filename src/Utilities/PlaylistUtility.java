package Utilities;

import Models.Playlist;
import java.util.ArrayList;

/**
 * Created on 4/20/2018 by Nick Gordon
 */
public class PlaylistUtility extends LocalStorage<Playlist>
{
    public PlaylistUtility()
    {
        super("Storage/Playlist.json", Playlist.class);
    }

    public void add(Playlist playlist)
    {
        if(!doesFileExist())
        {
            super.createEmpty();
        }

        ArrayList<Playlist> playlists = super.readMany();

        playlists.add(playlist);
        super.writeMany(playlists);
    }
}
