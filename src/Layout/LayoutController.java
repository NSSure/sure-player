package Layout;

import EventSystem.EventHandler;
import javafx.fxml.FXML;

import Models.Track;
import Utilities.TrackManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

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
    private Label lblTotalDuration;

    public LayoutController()
    {
        TrackManager.onTrackStarted = new EventHandler(this::onTrackStarted);
        TrackManager.onElapsedTimeChanged = new EventHandler(this::onElapseTimeChanged);
    }

    public void initialize()
    {
        try
        {
            TrackManager.initialize();

            Parent defaultCenterNode = FXMLLoader.load(getClass().getResource("/Default/Default.fxml"));
            sceneBase.setCenter(defaultCenterNode);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    // Events

    public void onTrackStarted()
    {

    }

    public void onElapseTimeChanged()
    {
        playbackProgress.setValue(TrackManager.getCurrentTime());
    }

    // FXML Functions

    @FXML
    public void toPlaylist(MouseEvent event)
    {
        try
        {
            Parent node = FXMLLoader.load(getClass().getResource("/Playlist/Playlist.fxml"));
            sceneBase.setCenter(node);
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void toTrackList(MouseEvent event)
    {
        try
        {
            Parent node = FXMLLoader.load(getClass().getResource("/Default/Default.fxml"));
            sceneBase.setCenter(node);
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void onPreviousClicked(MouseEvent event)
    {
        Track queuedPreviousTrack = TrackManager.getPrevious();

        if(queuedPreviousTrack != null)
        {
            TrackManager.toggleTrack(queuedPreviousTrack);
        }
    }

    @FXML
    public void onStartClicked(MouseEvent event)
    {
        Track currentTrack = TrackManager.getCurrentTrack();
        TrackManager.toggleTrack(currentTrack);
    }

    @FXML
    public void onNextClicked(MouseEvent event)
    {
        Track nextTrack = TrackManager.getNext();

        if(nextTrack != null)
        {
            TrackManager.toggleTrack(nextTrack);
        }
    }
}
