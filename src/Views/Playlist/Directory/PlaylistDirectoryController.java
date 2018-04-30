package Views.Playlist.Directory;

import Views.Layout.LayoutController;
import javafx.fxml.FXML;
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

public class PlaylistDirectoryController implements Initializable
{
    @FXML
    private ListView<Playlist> playlistDirectory;

    private LayoutController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlaylistUtility playlistUtil = new PlaylistUtility();

        if(playlistUtil.doesFileExist())
        {
            ArrayList<Playlist> playlists = playlistUtil.readMany();
            playlistDirectory.setItems(FXCollections.observableArrayList(playlists));

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
                        ContextMenu playlistOptions = new ContextMenu();

                        MenuItem playItem = new MenuItem("Play");
                        playItem.setOnAction((event) -> { });

                        MenuItem manageTracksItem = new MenuItem("Manage Tracks");
                        manageTracksItem.setOnAction((event) -> { parentController.toPlaylistTracks(playlist); });

                        playlistOptions.getItems().addAll(playItem, manageTracksItem);

                        setContextMenu(playlistOptions);
                        setText(playlist.getName());
                    }
                }
            });
        }
    }

    public void setParentController(LayoutController parentController)
    {
        this.parentController = parentController;
    }
}
