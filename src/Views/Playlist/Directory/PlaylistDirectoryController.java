package Views.Playlist.Directory;

import EventSystem.EventBus;
import EventSystem.EventCallable;
import Utilities.AppGlobal;
import Views.Playlist.PlaylistController;
import javafx.fxml.FXML;

import Enums.QueueType;
import javafx.fxml.Initializable;

import Models.Playlist;
import Utilities.PlaylistUtility;
import javafx.collections.FXCollections;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the playlist-directory.fxml page.
 *
 * @author Nick Gordon
 * @since 4/8/2018
 */

public class PlaylistDirectoryController implements Initializable
{
    class PlaylistActionAdded extends EventCallable<Playlist> {
        @Override
        public Void call() {
            PlaylistDirectoryController.this.addPlaylistToDirectory(super.getData());
            return null;
        }
    }

    @FXML
    private ListView<Playlist> playlistDirectory;

    public PlaylistDirectoryController() {
        EventBus.subscribe(this, "playlist-added", new PlaylistActionAdded());
    }

    /**
     * Loads the playlists stored on the local file system.
     *
     * FXML lifecycle function.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlaylistUtility playlistUtil = new PlaylistUtility();

        // Check if the playlists.json file exists if it does we can load the playlists.
        if(playlistUtil.doesFileExist())
        {
            // Load the playlists.
            ArrayList<Playlist> playlists = playlistUtil.readMany();
            playlistDirectory.setItems(FXCollections.observableArrayList(playlists));

            // Create a custom cell factory for the ListView.
            playlistDirectory.setCellFactory(param -> new ListCell<Playlist>() {
                @Override
                protected void updateItem(Playlist playlist, boolean empty) {
                    super.updateItem(playlist, empty);

                    if (empty || playlist == null || playlist.getName() == null)
                    {
                        setText(null);
                    }
                    else
                    {
                        // Add right click context menu with items that have actions for playing the playlist
                        // and managing the tracks in the playlist.
                        ContextMenu playlistOptions = new ContextMenu();

                        MenuItem playItem = new MenuItem("Play");

                        playItem.setOnAction((event) -> {
                            AppGlobal.getLayoutController().toQueue(QueueType.PLAYLIST, playlist);
                        });

                        MenuItem manageTracksItem = new MenuItem("Manage Tracks");

                        manageTracksItem.setOnAction((event) -> {
                            AppGlobal.getLayoutController().toPlaylistTracks(playlist);
                        });

                        playlistOptions.getItems().addAll(playItem, manageTracksItem);

                        setContextMenu(playlistOptions);
                        setText(playlist.getName());
                    }
                }
            });
        }
    }

    /**
     * Adds a new playlist to the ListViews items.
     * @param playlist The playlist to add to the UI.
     */
    public void addPlaylistToDirectory(Playlist playlist)
    {
        playlistDirectory.getItems().add(playlist);
    }
}
