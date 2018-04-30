package Views.Playlist.Tracks;

import Enums.QueueType;
import Views.Layout.LayoutController;
import javafx.fxml.FXML;

import javafx.beans.property.*;
import Models.Playlist;
import Utilities.PlaylistUtility;
import Utilities.AppGlobal;
import Utilities.TrackManager;
import Models.Track;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created on 4/29/2018 by Nick Gordon
 */
public class PlaylistTracksController
{
    @FXML
    private ListView<Track> playlistTracks;

    @FXML
    private Label lblPlaylistName;

    @FXML
    private Label lblPlaylistDescription;

    @FXML
    private Label lblSongCount;

    private TrackManager trackManager;

    private Playlist playlist;

    private ArrayList<Track> selectedTracks = new ArrayList<>();

    private List<UUID> playlistTrackIDs;

    private LayoutController parentController;

    public PlaylistTracksController()
    {
        trackManager = AppGlobal.getTrackManagerInstance();
    }

    public void initialize()
    {
        playlistTracks.setItems(FXCollections.observableArrayList(trackManager.getTracks()));

        CheckedListViewCheckObserver<Track> observer = new CheckedListViewCheckObserver<>();

        observer.addListener((obs, old, curr) ->
        {
            if (curr.getValue())
            {
                selectedTracks.add(curr.getKey());
            }
            else
            {
                selectedTracks.remove(curr.getKey());
            }
        });

        playlistTracks.setCellFactory(CheckBoxListCell.forListView(observer::getObserverForObject, new StringConverter<Track>() {
            @Override
            public String toString(Track object) {
                return object.getName();
            }

            @Override
            public Track fromString(String string) {
                return null;
            }
        }));
    }

    @FXML
    public void onPlayClicked()
    {
        this.parentController.toQueue(QueueType.PLAYLIST, playlist);
    }

    @FXML
    public void onSaveClicked(MouseEvent event)
    {
        playlist.setTracks(selectedTracks);

        PlaylistUtility playlistUtil = new PlaylistUtility();
        playlistUtil.update(playlist);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Action Completed");
        alert.setHeaderText(null);
        alert.setContentText("Successfully updated your playlist tracks.");
        alert.showAndWait();
    }

    @FXML
    public void onCancelClicked(MouseEvent event)
    {
        this.parentController.toPreviousCenterNode();
    }

    public void setPlaylist(Playlist playlist)
    {
        this.playlist = playlist;
        playlistTrackIDs = playlist.getTracks().stream().map(x -> x.getID()).collect(Collectors.toList());

        lblPlaylistName.setText("PLAYLIST - " + playlist.getName());
        lblPlaylistDescription.setText(playlist.getDescription());
        lblSongCount.setText(playlist.getTracks().size() + " songs");
    }

    public void setParentController(LayoutController parentController)
    {
        this.parentController = parentController;
    }

    // Source - https://stackoverflow.com/questions/25347347/how-to-add-listener-to-the-checkbox-inside-a-listview-that-uses-checkboxlistcell
    private class CheckedListViewCheckObserver<T> extends SimpleObjectProperty<Pair<T, Boolean>>
    {
        BooleanProperty getObserverForObject(T object)
        {
            Track track = (Track)object;
            BooleanProperty value = new SimpleBooleanProperty(false);

            if(playlistTrackIDs.contains(track.getID()))
            {
                selectedTracks.add(track);
                value.set(true);
            }

            value.addListener((observable, oldValue, newValue) -> {
                CheckedListViewCheckObserver.this.set(new Pair<>(object, newValue));
            });

            return value;
        }
    }
}
