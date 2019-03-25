package Views.Playlist.Tracks;

import Helpers.Constants;
import Utilities.NavigationService;
import javafx.fxml.FXML;

import Enums.QueueType;

import java.io.IOException;
import java.util.List;

import Views.Layout.LayoutController;
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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for the playlist-tracks.fxml page.
 *
 * @author Nick Gordon
 * @since 4/29/2018
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

    /**
     * Populates the playlist tracks ListView and configures its properties.
     * FXML lifecycle function.
     */
    public void initialize()
    {
        playlistTracks.setItems(FXCollections.observableArrayList(trackManager.getTracks()));

        CheckedListViewCheckObserver<Track> observer = new CheckedListViewCheckObserver<>();

        // Fired when an item in the ListView is checked or unchecked.
        // Source - https://stackoverflow.com/questions/25347347/how-to-add-listener-to-the-checkbox-inside-a-listview-that-uses-checkboxlistcell
        observer.addListener((obs, old, curr) ->
        {
            // If the track was selected then we add it to the selected tracks.
            if (curr.getValue())
            {
                if(!selectedTracks.contains(curr.getKey()))
                {
                    selectedTracks.add(curr.getKey());
                }
            }
            // If the track was deselected we need to remove it from the selected tracks.
            else
            {
                selectedTracks.remove(curr.getKey());
            }
        });

        // Create a custom cell factory for handling the check state changes and setting the display text.
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

    /**
     * Calls toQueue on the parent controller.
     */
    @FXML
    public void onPlayClicked() throws IOException
    {
        AppGlobal.getLayoutController().toQueue(QueueType.PLAYLIST, playlist);
    }

    /**
     * Updates the tracks for the playlist on the local file system.
     * @param event The mouse click event.
     */
    @FXML
    public void onSaveClicked(MouseEvent event)
    {
        // Set the selected tracks on the playlist object.
        playlist.setTracks(selectedTracks);

        // Update the playlist locally.
        PlaylistUtility playlistUtil = new PlaylistUtility();
        playlistUtil.update(playlist);

        // Update the playlist UI information.
        lblPlaylistName.setText("PLAYLIST - " + playlist.getName());
        lblPlaylistDescription.setText(playlist.getDescription());
        lblSongCount.setText(playlist.getTracks().size() + " songs");

        // Tell the user their update was successful.
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

    /**
     * Sets the playlist that the view is going to display the tracks for.
     * @param playlist The playlist to display tracks for.
     */
    public void setPlaylist(Playlist playlist)
    {
        this.playlist = playlist;

        // Get all the IDs of the tracks in the playlist.
        playlistTrackIDs = playlist.getTracks().stream().map(x -> x.getID()).collect(Collectors.toList());

        // Configure the UI information for the playlist.
        lblPlaylistName.setText("PLAYLIST - " + playlist.getName());
        lblPlaylistDescription.setText(playlist.getDescription());
        lblSongCount.setText(playlist.getTracks().size() + " songs");
    }

    /**
     * Sets the parent controller of the view.
     * @param parentController The parent controller of the view.
     */
    public void setParentController(LayoutController parentController)
    {
        this.parentController = parentController;
    }

    /**
     * Loads the playlists stored on the local file system.
     * Source - https://stackoverflow.com/questions/25347347/how-to-add-listener-to-the-checkbox-inside-a-listview-that-uses-checkboxlistcell
     *
     * @param <T>
     */
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
