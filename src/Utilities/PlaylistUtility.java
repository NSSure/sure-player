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
        super("Storage/Playlists.json", Playlist.class);
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

    public void update(Playlist playlist)
    {
        ArrayList<Playlist> playlists = super.readMany();

        for(int i = 0; i < playlists.size(); i++)
        {
            if(playlists.get(i).getID().compareTo(playlist.getID()) == 0)
            {
                playlists.set(i, playlist);
                super.writeMany(playlists);
            }
        }
    }
}
