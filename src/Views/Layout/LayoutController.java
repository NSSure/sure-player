package Views.Layout;

import Icons.ExtendedIconNode;
import Icons.PlaybackIcons;
import Models.Playlist;
import Views.Playlist.Directory.PlaylistDirectoryController;
import Views.Playlist.PlaylistController;
import javafx.beans.Observable;
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
    private TrackManager trackManager;



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

    private BooleanProperty isTogglePlaybackDisabled = new SimpleBooleanProperty(true);

    private PlaybackIcons playbackIcons;

    private String currentCenterNode;

    public LayoutController()
    {
        // Creates the singleton for the TrackManager.
        AppGlobal.init();
        trackManager = AppGlobal.getTrackManagerInstance();

        IconFontFX.register(FontAwesome.getIconFont());

        playbackIcons = new PlaybackIcons();
    }

    public void initialize()
    {
        configure();
        toTrackList(null);
    }

    private void configure()
    {
        // Set Playback control icons.
        lblTogglePlayback.setGraphic(playbackIcons.getPlayCircleIcon());
        lblStepBackward.setGraphic(playbackIcons.getStepBackwardIcon());
        lblStepForward.setGraphic(playbackIcons.getStepForwardIcon());
        lblSoundLevel.setGraphic(playbackIcons.getDefaultVolumeIcon());
        lblTrackView.setGraphic(playbackIcons.getQueuedViewIcon());
        lblShuffle.setGraphic(playbackIcons.getShuffleIcon());

        trackManager.mediaPlayerProperty().addListener(this::mediaPlayerInitialized);

        // Bind the disabled property of the play/pause icon to only be enable when a track is selected.
        lblTogglePlayback.disableProperty().bind(isTogglePlaybackDisabled);
    }

    private FXMLLoader loadCenterPane(String resourcePath)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent centerNode = loader.load();
            sceneBase.setCenter(centerNode);

            return loader;
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
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

    private void onTrackSelected()
    {
        isTogglePlaybackDisabled.set(false);
    }

    private void onPlaylistCreationAborted()
    {
        toTrackList(null);
    }

    private void onPlaylistCreationFinished()
    {
        toTrackList(null);
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
        FXMLLoader loader = loadCenterPane("/Views/Tracks/Tracks.fxml");
        currentCenterNode = "tracks";

        if(loader != null)
        {
            TracksController tracksController = loader.getController();
            tracksController.onTrackSelected = new EventHandler(this::onTrackSelected);
        }
    }

    @FXML
    public void onPreviousClicked(MouseEvent event)
    {
        Track queuedPreviousTrack = trackManager.getPrevious();

        if(queuedPreviousTrack != null)
        {
            trackManager.toggleTrack(queuedPreviousTrack);
        }
    }

    @FXML
    public void onStartClicked(MouseEvent event)
    {
        Track currentTrack = trackManager.getQueuedTrack();
        trackManager.toggleTrack(currentTrack);
    }

    @FXML
    public void onNextClicked(MouseEvent event)
    {
        Track nextTrack = trackManager.getNext();

        if(nextTrack != null)
        {
            trackManager.toggleTrack(nextTrack);
        }
    }

    @FXML
    public void onTrackViewClicked()
    {
        if(currentCenterNode.compareToIgnoreCase("tracks") == 0)
        {
            lblTrackView.setGraphic(playbackIcons.getTrackViewIcon());
            FXMLLoader loader = loadCenterPane("/Views/Queue/queue.fxml");
            currentCenterNode = "queue";
        }
        else if (currentCenterNode.compareToIgnoreCase("queue") == 0)
        {
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
