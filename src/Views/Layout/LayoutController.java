package Views.Layout;

import Enums.QueueType;
import Icons.ExtendedIconNode;
import Icons.PlaybackIcons;
import Models.Playlist;
import Views.Playlist.Directory.PlaylistDirectoryController;
import Views.Playlist.PlaylistController;
import Views.Queue.QueueController;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import Views.Tracks.TracksController;
import EventSystem.EventHandler;
import Utilities.AppGlobal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import Models.Track;
import Utilities.TrackManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconFontFX;

import java.io.IOException;
/**
 * Created on 4/8/2018 by Nick Gordon
 */
public class LayoutController
{
    @FXML
    private BorderPane sceneBase;

    @FXML
    private Slider playbackProgress;

    @FXML
    private Label lblCurrentTimeElapsed;

    @FXML
    private Label lblTrackDuration;

    @FXML
    private Label lblTogglePlayback;

    @FXML
    private Label lblStepBackward;

    @FXML
    private Label lblStepForward;

    @FXML
    private Label lblTrackView;

    @FXML
    private Label lblShuffle;

    @FXML
    private Label lblSoundLevel;

    @FXML
    private Label lblCurrentTrack;

    @FXML
    private Label lblCurrentArtist;

    @FXML
    private Track currentTrack;

    private TrackManager trackManager;

    private PlaybackIcons playbackIcons;

    private Parent tracksNode;
    private Parent queueNode;

    private TracksController tracksController;
    private QueueController queueController;

    private String currentCenterNode;

    public LayoutController()
    {
        // Creates the singleton for the TrackManager.
        AppGlobal.init();
        trackManager = AppGlobal.getTrackManagerInstance();

        IconFontFX.register(FontAwesome.getIconFont());

        playbackIcons = new PlaybackIcons();
    }

    public void initialize() throws IOException
    {
        FXMLLoader tracksLoader = new FXMLLoader(getClass().getResource("/Views/Tracks/Tracks.fxml"));
        tracksNode = tracksLoader.load();
        tracksController = tracksLoader.getController();
        tracksController.setParentController(this);

        FXMLLoader queueLoader = new FXMLLoader(getClass().getResource("/Views/Queue/Queue.fxml"));
        queueNode = queueLoader.load();
        queueController = queueLoader.getController();
        queueController.configureQueue(QueueType.TRACKS, trackManager.getCurrentTrack());

        configure();
        toTrackList(null);
    }

    private void configure()
    {
        this.currentTrack = trackManager.getCurrentTrack();

        // Set Playback control icons.
        lblTogglePlayback.setGraphic(playbackIcons.getPlayCircleIcon());
        lblStepBackward.setGraphic(playbackIcons.getStepBackwardIcon());
        lblStepForward.setGraphic(playbackIcons.getStepForwardIcon());
        lblSoundLevel.setGraphic(playbackIcons.getDefaultVolumeIcon());
        lblTrackView.setGraphic(playbackIcons.getQueuedViewIcon());
        lblShuffle.setGraphic(playbackIcons.getShuffleIcon());

        lblCurrentTrack.setText(this.currentTrack.getName());
        lblCurrentArtist.setText(this.currentTrack.getArtist());

        trackManager.mediaPlayerProperty().addListener(this::mediaPlayerInitialized);
        trackManager.currentTrackProperty().addListener(this::onCurrentTrackChanged);
    }

    private void onCurrentTrackChanged(Observable observable)
    {
        this.currentTrack = trackManager.getCurrentTrack();

        lblCurrentTrack.setText(this.currentTrack.getName());
        lblCurrentArtist.setText(this.currentTrack.getArtist());
    }

    private void mediaPlayerInitialized(Observable observable)
    {
        trackManager.getMediaPlayer().currentTimeProperty().addListener(this::onElapseTimeChanged);
        trackManager.getMediaPlayer().setOnReady(this::mediaPlayerStartupFinished);
        trackManager.getMediaPlayer().setOnPlaying(this::onMediaPlaybackStarted);
        trackManager.getMediaPlayer().setOnPaused(this::onMediaPlaybackPaused);
    }

    private void mediaPlayerStartupFinished()
    {
        double totalDurationInSeconds = trackManager.getMediaPlayer().getTotalDuration().toSeconds();

        int totalSeconds = (int)totalDurationInSeconds;

        //int hours = totalSecs / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        lblTrackDuration.setText(String.format("%02d:%02d", minutes, seconds));
        playbackProgress.setMax(totalDurationInSeconds);
    }

    private void onMediaPlaybackStarted()
    {
        lblTogglePlayback.setGraphic(playbackIcons.getPauseCircleIcon());
    }

    private void onMediaPlaybackPaused()
    {
        lblTogglePlayback.setGraphic(playbackIcons.getPlayCircleIcon());
    }

    private void onElapseTimeChanged(Observable observable)
    {
        double elapsedTime = trackManager.getMediaPlayer().getCurrentTime().toSeconds();

        int totalSeconds = (int)elapsedTime;

        //int hours = totalSecs / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        lblCurrentTimeElapsed.setText(String.format("%02d:%02d", minutes, seconds));
        playbackProgress.setValue(elapsedTime);
    }

    private void onPlaylistCreationAborted()
    {
        toTrackList(null);
    }

    private void onPlaylistCreationFinished()
    {
        toTrackList(null);
    }

    private  FXMLLoader loadCenterPane(String resourcePath)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent centerNode = loader.load();
            sceneBase.setCenter((Parent)centerNode);

            return loader;
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    private void loadCenterPane(Parent centerNode)
    {
        sceneBase.setCenter(centerNode);
    }

    // FXML Functions

    @FXML
    public void toPlaylist(ActionEvent event)
    {
        FXMLLoader loader = loadCenterPane("/Views/Playlist/Playlist.fxml");
        currentCenterNode = "playlist";

        if(loader != null)
        {
            PlaylistController playlistController = loader.getController();
            playlistController.onPlaylistCreationFinished = new EventHandler(this::onPlaylistCreationFinished);
            playlistController.onPlaylistCreationAborted = new EventHandler(this::onPlaylistCreationAborted);
        }
    }

    @FXML
    public void toTrackList(MouseEvent event)
    {
        loadCenterPane(tracksNode);
        currentCenterNode = "tracks";
    }

    public void toQueue(QueueType queueType, Track track)
    {
        queueController.configureQueue(queueType, track);
        loadCenterPane(queueNode);
        currentCenterNode = "queue";
        lblTrackView.setGraphic(playbackIcons.getQueuedViewIcon());
    }


    @FXML
    public void onPreviousClicked(MouseEvent event)
    {
        trackManager.previous();
    }

    @FXML
    public void onStartClicked(MouseEvent event)
    {
        trackManager.toggleTrack(null);
    }

    @FXML
    public void onNextClicked(MouseEvent event)
    {
        trackManager.next();
    }

    @FXML
    public void onTrackViewClicked()
    {
        if(currentCenterNode.compareToIgnoreCase("tracks") == 0)
        {
            lblTrackView.setGraphic(playbackIcons.getTrackViewIcon());
            toQueue(queueController.getQueueType(), trackManager.getSelectedTrack());
        }
        else if (currentCenterNode.compareToIgnoreCase("queue") == 0)
        {
            trackManager.setQueuedTracks(trackManager.getTracks());
            lblTrackView.setGraphic(playbackIcons.getQueuedViewIcon());
            toTrackList(null);
        }
    }

    @FXML
    public void onShuffleClicked(MouseEvent event)
    {
        trackManager.setShuffle(!trackManager.isShuffle());
        ((ExtendedIconNode)lblShuffle.getGraphic()).toggleActiveState();
    }

    @FXML
    public void onSoundLevelIconClicked(MouseEvent event)
    {
        if(lblSoundLevel.getGraphic() == playbackIcons.getDefaultVolumeIcon())
        {
            lblSoundLevel.setGraphic(playbackIcons.getMutedVolumeIcon());
        }
        else
        {
            lblSoundLevel.setGraphic(playbackIcons.getDefaultVolumeIcon());
        }
    }
}
