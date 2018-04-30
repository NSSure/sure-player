package Utilities;

import Models.Playlist;
import java.util.ArrayList;

/**
 * Extends the local storage utility and contains functionality specific to
 * operations related to playlists.
 *
 * @author Nick Gordon
 * @since 4/20/2018
 */
public class PlaylistUtility extends LocalStorage<Playlist>
{
    public PlaylistUtility()
    {
        super("Storage/Playlists.json", Playlist.class);
    }

    /**
     * Adds a single playlist to the local Playlists.json file.
     *
     * @param playlist The playlist to save locally.
     */
    public void add(Playlist playlist)
    {
        // If the Playlists.json file does not exist we need to create an empty one.
        if(!doesFileExist())
        {
            super.createEmpty();
        }

        // Read the existing playlists from the local file system.
        ArrayList<Playlist> playlists = super.readMany();

        // Add the new playlist and write the file back to the file system.
        playlists.add(playlist);
        super.writeMany(playlists);
    }

    /**
     * Updates a single playlist in the Playlists.json file.
     * @param playlist The playlist to updated locally.
     */
    public void update(Playlist playlist)
    {
        // Read the existing playlists from the local file system.
        ArrayList<Playlist> playlists = super.readMany();

        // Loop through the local playlists and find and replace the one that matches the one that needs to be updated.
        for(int i = 0; i < playlists.size(); i++)
        {
            if(playlists.get(i).getID().compareTo(playlist.getID()) == 0)
            {
                playlists.set(i, playlist);

                // Write the array with the updated playlist back to the file system.
                super.writeMany(playlists);
            }
        }
    }
}
