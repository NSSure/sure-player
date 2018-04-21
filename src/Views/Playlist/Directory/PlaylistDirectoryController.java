package Views.Playlist.Directory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import Models.Playlist;
import Utilities.PlaylistUtility;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlaylistDirectoryController implements Initializable
{
    @FXML
    private ListView<Playlist> playlistDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlaylistUtility playlistUtil = new PlaylistUtility();

        if(playlistUtil.doesFileExist())
        {
            ArrayList<Playlist> playlists = playlistUtil.readMany();
            playlistDirectory.setItems(FXCollections.observableArrayList(playlists));

            playlistDirectory.setCellFactory(param -> new ListCell<Playlist>() {
                @Override
                protected void updateItem(Playlist item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getName() == null)
                    {
                        setText(null);
                    }
                    else
                    {
                        setText(item.getName());
                    }
                }
            });
        }
    }
}
