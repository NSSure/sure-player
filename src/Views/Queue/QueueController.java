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
 * Created on 4/19/2018 by Nick Gordon
 */
public class QueueController
{
    @FXML
    private TableView<Track> queuedTracksTable;

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

    public void startQueue(QueueType queueType, Playlist sourceQueuedPlaylist)
    {
        this.queueType = queueType;
        trackManager.setQueuedTracks(new LinkedList<>(sourceQueuedPlaylist.getTracks()));
        configureQueue();
    }

    public void startQueue(QueueType queueType, Track sourceQueuedTracked)
    {
        this.queueType = queueType;

        switch (queueType)
        {
            case TRACKS:
                trackManager.setQueuedTracks(trackManager.getTracks());
                break;
            case ALBUM:
                trackManager.setQueuedTracks(trackManager.getTracks().stream().filter(a -> a.getArtist().compareToIgnoreCase(sourceQueuedTracked.getAlbum()) == 0).collect(Collectors.toCollection(LinkedList::new)));
                break;
            case ARTIST:
                trackManager.setQueuedTracks(trackManager.getTracks().stream().filter(a -> a.getArtist().compareToIgnoreCase(sourceQueuedTracked.getArtist()) == 0).collect(Collectors.toCollection(LinkedList::new)));
                break;
            case GENRE:
                trackManager.setQueuedTracks(trackManager.getTracks().stream().filter(a -> a.getGenre().compareToIgnoreCase(sourceQueuedTracked.getGenre()) == 0).collect(Collectors.toCollection(LinkedList::new)));
                break;
            default:
                break;
        }

        configureQueue();
    }

    private void configureQueue()
    {
        queuedTracksTable.setItems(FXCollections.observableArrayList(trackManager.getQueuedTracks()));

        trackManager.currentTrackProperty().addListener(this::onCurrentTrackChanged);
        trackManager.queuedTracksProperty().addListener(this::trackQueueChanged);

        lblCurrentTrackName.setText(trackManager.getCurrentTrack().getName());
        lblCurrentTrackArtist.setText(trackManager.getCurrentTrack().getArtist());
        lblCurrentTrackAlbum.setText(trackManager.getCurrentTrack().getAlbum());
        lblCurrentTrackYear.setText(trackManager.getCurrentTrack().getYear());

        queuedTracksTable.setOnMouseClicked(this::onQueuedTracksTableClicked);
    }

    private void onCurrentTrackChanged(Observable observable)
    {
        lblCurrentTrackName.setText(trackManager.getCurrentTrack().getName());
        lblCurrentTrackArtist.setText(trackManager.getCurrentTrack().getArtist());
        lblCurrentTrackAlbum.setText(trackManager.getCurrentTrack().getAlbum());
        lblCurrentTrackYear.setText(trackManager.getCurrentTrack().getYear());
    }

    private void trackQueueChanged(Observable observable)
    {
        queuedTracksTable.setItems(FXCollections.observableArrayList(AppGlobal.getTrackManagerInstance().getQueuedTracks()));
    }

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

    public QueueType getQueueType()
    {
        return this.queueType;
    }
}
