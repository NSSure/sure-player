package Views.Queue;

import Models.Playlist;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;

import Enums.QueueType;
import javafx.beans.Observable;
import javafx.scene.input.MouseButton;

import Models.Track;
import Utilities.AppGlobal;
import Utilities.TrackManager;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Controller for the video-playback.fxml page.
 *
 * @author Nick Gordon
 * @since 4/19/2018
 */
public class QueueController
{
    @FXML
    private TableView<Track> queuedTracksTable;

    @FXML
    private Label lblQueueDescription;

    @FXML
    private Label lblCurrentTrackName;

    @FXML
    private Label lblCurrentTrackArtist;

    @FXML
    private Label lblCurrentTrackAlbum;

    @FXML
    private Label lblCurrentTrackYear;

    private TrackManager trackManager;

    private QueueType queueType;

    public QueueController()
    {
        trackManager = AppGlobal.getTrackManagerInstance();
    }

    /**
     * Sets the queued tracks on the track manager to the tracks associated with the playlist.
     *
     * @param queueType The type of queue.
     * @param sourceQueuedPlaylist
     */
    public void startQueue(QueueType queueType, Playlist sourceQueuedPlaylist)
    {
        this.queueType = queueType;
        trackManager.setQueuedTracks(new LinkedList<>(sourceQueuedPlaylist.getTracks()));
        configureQueue();
    }

    /**
     * Sets the queued tracks on the track manager to the tracks matching the filter for the specific queue type.
     *
     * @param queueType The type of queue.
     * @param sourceQueuedTracked The track to filter from.
     */
    public void startQueue(QueueType queueType, Track sourceQueuedTracked)
    {
        this.queueType = queueType;

        // For the different queue types related to a track we need to filter the full track list to get any tracks
        // that match our specific query and set those matching tracks as the queued tracks in the track manager.
        switch (queueType)
        {
            case TRACKS:
                trackManager.setQueuedTracks(trackManager.getQueuedTracks());
                break;
            case ALBUM:
                trackManager.setQueuedTracks(trackManager.getTracks().stream().filter(a -> a.getArtist().compareToIgnoreCase(sourceQueuedTracked.getAlbum()) == 0).collect(Collectors.toCollection(LinkedList::new)));
                trackManager.setCurrentTrack(trackManager.getQueuedTracks().get(0));
                break;
            case ARTIST:
                trackManager.setQueuedTracks(trackManager.getTracks().stream().filter(a -> a.getArtist().compareToIgnoreCase(sourceQueuedTracked.getArtist()) == 0).collect(Collectors.toCollection(LinkedList::new)));
                trackManager.setCurrentTrack(trackManager.getQueuedTracks().get(0));
                break;
            case GENRE:
                if(sourceQueuedTracked.getGenre() != null)
                {
                    trackManager.setQueuedTracks(trackManager.getTracks().stream().filter(a -> a.getGenre().compareToIgnoreCase(sourceQueuedTracked.getGenre()) == 0).collect(Collectors.toCollection(LinkedList::new)));
                    trackManager.setCurrentTrack(trackManager.getQueuedTracks().get(0));
                }
                else
                {
                    trackManager.setQueuedTracks(new LinkedList<>());
                }
                break;
            default:
                break;
        }

        configureQueue();
    }

    /**
     * Sets the queue tracks table items, configures UI text, and sets actions/events.
     */
    private void configureQueue()
    {
        queuedTracksTable.setItems(FXCollections.observableArrayList(trackManager.getQueuedTracks()));

        trackManager.currentTrackProperty().addListener(this::onCurrentTrackChanged);
        trackManager.queuedTracksProperty().addListener(this::trackQueueChanged);

        // Set UI text.
        lblCurrentTrackName.setText(trackManager.getCurrentTrack().getName());
        lblCurrentTrackArtist.setText(trackManager.getCurrentTrack().getArtist());
        lblCurrentTrackAlbum.setText(trackManager.getCurrentTrack().getAlbum());
        lblCurrentTrackYear.setText(trackManager.getCurrentTrack().getYear());

        lblQueueDescription.setText("Currently Playing - " + this.queueType);

        queuedTracksTable.setOnMouseClicked(this::onQueuedTracksTableClicked);
    }

    /**
     * Fired when the current track in the track manager changes.  This updates the UI to reflect the new
     * current track.
     * @param observable
     */
    private void onCurrentTrackChanged(Observable observable)
    {
        lblCurrentTrackName.setText(trackManager.getCurrentTrack().getName());
        lblCurrentTrackArtist.setText(trackManager.getCurrentTrack().getArtist());
        lblCurrentTrackAlbum.setText(trackManager.getCurrentTrack().getAlbum());
        lblCurrentTrackYear.setText(trackManager.getCurrentTrack().getYear());
    }

    /**
     * Fired when the entire queued track listing changes.  We need to update the table view to reflect this
     * new set of queued tracks.
     * @param observable
     */
    private void trackQueueChanged(Observable observable)
    {
        queuedTracksTable.setItems(FXCollections.observableArrayList(AppGlobal.getTrackManagerInstance().getQueuedTracks()));
    }

    /**
     * Fired on a mouse click of the table.  Handles setting the selected track for a single click.  And toggling playback
     * on a double click.
     *
     * @param event The mouse click event.
     */
    private void onQueuedTracksTableClicked(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            TableView.TableViewSelectionModel model = queuedTracksTable.getSelectionModel();
            Track selectedTrack = (Track)model.getSelectedItem();
            trackManager.setSelectedTrack(selectedTrack);

            if(event.getClickCount() == 2){
                trackManager.toggleTrack(selectedTrack);
            }
        }
    }

    /**
     * Get the current queue type.
     * @return The current queue type.
     */
    public QueueType getQueueType()
    {
        return this.queueType;
    }
}
