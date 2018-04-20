package Queue;

import javafx.fxml.FXML;

import javafx.beans.Observable;
import javafx.collections.ObservableList;

import Models.Track;
import Utilities.AppGlobal;
import Utilities.TrackManager;
import javafx.collections.FXCollections;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;

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

    @FXML
    private Track currentTrack;

    private TrackManager trackManager;

    public QueueController()
    {
        trackManager = AppGlobal.getTrackManagerInstance();
    }

    public void initialize()
    {
        queuedTracksTable.setItems(FXCollections.observableArrayList(AppGlobal.getTrackManagerInstance().getQueuedTracks()));

        this.currentTrack = trackManager.getQueuedTrack();

        trackManager.queuedTrackProperty().addListener(this::onCurrentTrackChanged);
        trackManager.queuedTracksProperty().addListener(this::trackQueueChanged);

        lblCurrentTrackName.textProperty().bind(currentTrack.nameProperty());
        lblCurrentTrackArtist.textProperty().bind(currentTrack.artistProperty());
        lblCurrentTrackAlbum.textProperty().bind(currentTrack.albumProperty());
        lblCurrentTrackYear.textProperty().bind(currentTrack.yearProperty());
    }

    private void onCurrentTrackChanged(Observable observable)
    {
        currentTrack = trackManager.getQueuedTrack();
    }

    private void trackQueueChanged(Observable observable)
    {
        queuedTracksTable.setItems(FXCollections.observableArrayList(AppGlobal.getTrackManagerInstance().getQueuedTracks()));
    }
}
