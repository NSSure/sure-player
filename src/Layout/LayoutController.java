package Layout;

import Icons.PlaybackIcons;
import Playlist.PlaylistController;
import javafx.beans.Observable;
import javafx.fxml.FXML;

import Tracks.TracksController;
import EventSystem.EventHandler;
import Utilities.AppGlobal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import Models.Track;
import Utilities.TrackManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;

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
    private Label lblSoundLevel;

    private BooleanProperty isTogglePlaybackDisabled = new SimpleBooleanProperty(true);

    private PlaybackIcons playbackIcons;

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
        lblTogglePlayback.setGraphic(playbackIcons.getPlayCircleIcon());
        lblStepBackward.setGraphic(playbackIcons.getStepBackwardIcon());
        lblStepForward.setGraphic(playbackIcons.getStepForwardIcon());

        lblSoundLevel.setGraphic(playbackIcons.getDefaultVolumeIcon());

        trackManager.currentTrackDurationProperty().addListener(this::currentMediaStartupFinished);
        trackManager.currentTimeProperty().addListener(this::onElapseTimeChanged);

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

    private void currentMediaStartupFinished(Observable observable)
    {
        double totalDurationInSeconds = trackManager.getMediaPlayer().getTotalDuration().toSeconds();

        int totalSeconds = (int)totalDurationInSeconds;

        //int hours = totalSecs / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        lblTrackDuration.setText(String.format("%02d:%02d", minutes, seconds));

        playbackProgress.setMax(totalDurationInSeconds);
    }

    private void onElapseTimeChanged(Observable observable)
    {
        double elapsedTime = trackManager.getCurrentTime();

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
    public void toPlaylist(MouseEvent event)
    {
        FXMLLoader loader = loadCenterPane("/Playlist/Playlist.fxml");

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
        FXMLLoader loader = loadCenterPane("/Tracks/Tracks.fxml");

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
        Track currentTrack = trackManager.getCurrentTrack();
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
