package Views.Layout;

import javafx.fxml.FXML;

import Enums.QueueType;
import javafx.beans.Observable;
import jiconfont.icons.FontAwesome;

import Icons.ExtendedIconNode;
import Icons.PlaybackIcons;
import Models.Playlist;
import Views.Playlist.Directory.PlaylistDirectoryController;
import Views.Playlist.PlaylistController;
import Views.Playlist.Tracks.PlaylistTracksController;
import Views.Queue.QueueController;
import javafx.event.ActionEvent;
import Views.Tracks.TracksController;
import Utilities.AppGlobal;
import Models.Track;
import Utilities.TrackManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import jiconfont.javafx.IconFontFX;
import java.io.IOException;

/**
 * Controller for the layout.fxml page.
 *
 * @author Nick Gordon
 * @since 4/8/2018
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

    @FXML
    private PlaylistDirectoryController playlistDirectoryController;

    private String currentCenterNode;
    private String previousCenterNode;

    public LayoutController()
    {
        // Creates the singleton for the TrackManager.
        AppGlobal.init();
        trackManager = AppGlobal.getTrackManagerInstance();

        // Register the font awesome icons.
        IconFontFX.register(FontAwesome.getIconFont());

        playbackIcons = new PlaybackIcons();
    }

    /**
     * Lifecycle event for the fxml page.  Here all the controls have been initialized and can be accessed from code.
     * @throws IOException
     */
    public void initialize() throws IOException
    {
        // Load the default state of the track listing.
        FXMLLoader tracksLoader = new FXMLLoader(getClass().getResource("/Views/Tracks/Tracks.fxml"));
        tracksNode = tracksLoader.load();
        tracksController = tracksLoader.getController();
        tracksController.setParentController(this);

        // Load the default state of the queue view.
        FXMLLoader queueLoader = new FXMLLoader(getClass().getResource("/Views/Queue/Queue.fxml"));
        queueNode = queueLoader.load();
        queueController = queueLoader.getController();
        queueController.startQueue(QueueType.TRACKS, trackManager.getCurrentTrack());

        playlistDirectoryController.setParentController(this);

        // Set the UI bindings and text.
        configure();

        // Set the default center pane as the track listing.
        toTrackList(null);
    }

    /**
     * Configues the bindings, text, and graphics for the main layout view.
     */
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

    /**
     * Fired when the currentTrackProperty is the TrackManager is changed.
     * @param observable
     */
    private void onCurrentTrackChanged(Observable observable)
    {
        this.currentTrack = trackManager.getCurrentTrack();

        lblCurrentTrack.setText(this.currentTrack.getName());
        lblCurrentArtist.setText(this.currentTrack.getArtist());
    }

    /**
     * Fired when the mediaPlayerProperty is the TrackManager is changed.
     * @param observable
     */
    private void mediaPlayerInitialized(Observable observable)
    {
        // Configure actions.
        trackManager.getMediaPlayer().currentTimeProperty().addListener(this::onElapseTimeChanged);
        trackManager.getMediaPlayer().setOnReady(this::mediaPlayerStartupFinished);
        trackManager.getMediaPlayer().setOnPlaying(this::onMediaPlaybackStarted);
        trackManager.getMediaPlayer().setOnPaused(this::onMediaPlaybackPaused);
    }

    /**
     * Fired when the media player is done configuring itself and is ready for playback.
     * When that happens we have access to information about the current piece of media (time, duration, etc).
     */
    private void mediaPlayerStartupFinished()
    {
        // Get the duration of the current media.
        double totalDurationInSeconds = trackManager.getMediaPlayer().getTotalDuration().toSeconds();

        int totalSeconds = (int)totalDurationInSeconds;

        // Calculate the duration in minutes and seconds.
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        // Display the calculated duration on the UI and set the max value of the slider to the total duration.
        lblTrackDuration.setText(String.format("%02d:%02d", minutes, seconds));
        playbackProgress.setMax(totalDurationInSeconds);
    }

    /**
     * Fired when the MediaPlayer begins playback of the current media.
     */
    private void onMediaPlaybackStarted()
    {
        lblTogglePlayback.setGraphic(playbackIcons.getPauseCircleIcon());
    }

    /**
     * Fired when the MediaPlayer pauses playback of the current media.
     */
    private void onMediaPlaybackPaused()
    {
        lblTogglePlayback.setGraphic(playbackIcons.getPlayCircleIcon());
    }

    /**
     * Fired when the elapsed time of the current media playback changes.
     * We need to updates the playback progress here.
     * @param observable
     */
    private void onElapseTimeChanged(Observable observable)
    {
        double elapsedTime = trackManager.getMediaPlayer().getCurrentTime().toSeconds();

        int totalSeconds = (int)elapsedTime;

        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        lblCurrentTimeElapsed.setText(String.format("%02d:%02d", minutes, seconds));
        playbackProgress.setValue(elapsedTime);
    }

    /**
     * The child playlist creation view had the playlist creation canceled so the previous center node should be
     * shown again.
     */
    public void onPlaylistCreationAborted()
    {
        toPreviousCenterNode();
    }

    /**
     * A playlist was created so we need to tell the sidebar playlist directory to add the new playlist to the UI.
     * @param playlist
     */
    public void onPlaylistCreationFinished(Playlist playlist)
    {
        toPreviousCenterNode();
        playlistDirectoryController.addPlaylistToDirectory(playlist);
    }

    /**
     * Changes the node displayed in the BorderPanes center.
     *
     * @param resourcePath The path to the fxml file to load in the center pane.
     * @param targetCenterNode The type of node being loaded in the center pane.
     * @return The FXMLLoader that handles loading the fxml resources.
     */
    private  FXMLLoader loadCenterPane(String resourcePath, String targetCenterNode)
    {
        try
        {
            // Get the fxml resource from the path and load it into the center node.
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent centerNode = loader.load();
            sceneBase.setCenter((Parent)centerNode);

            previousCenterNode = currentCenterNode;
            currentCenterNode = targetCenterNode;

            return loader;
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    /**
     * Changes the node displayed in the BorderPanes center.
     *
     * @param centerNode The actual node object to load into the center node
     * @param targetCenterNode The type of node being loaded in the center pane.
     */
    private void loadCenterPane(Parent centerNode, String targetCenterNode)
    {
        if(currentCenterNode == null ||  currentCenterNode.compareToIgnoreCase(targetCenterNode) != 0)
        {
            sceneBase.setCenter(centerNode);
            previousCenterNode = currentCenterNode;
            currentCenterNode = targetCenterNode;
        }
    }

    // FXML Functions

    /**
     * Loads whatever the previous center node was into the BorderPane.
     */
    public void toPreviousCenterNode()
    {
        switch (previousCenterNode)
        {
            case "playlist":
                toPlaylist(null);
                break;
            case "tracks":
                toTrackList(null);
                break;
            case "queue":
                toQueue(queueController.getQueueType(), trackManager.getCurrentTrack());
                break;
            default:
                break;
        }
    }

    /**
     * Loads the playlist-tracks.fxml into the center node.
     * @param playlist Playlist to display tracks for.
     */
    public void toPlaylistTracks(Playlist playlist)
    {
        FXMLLoader loader = loadCenterPane("/Views/Playlist/Tracks/playlist-tracks.fxml", "playlist-tracks");

        if(loader != null)
        {
            // Set any necessary variables on the child controller.
            PlaylistTracksController playlistTracksController = loader.getController();
            playlistTracksController.setParentController(this);
            playlistTracksController.setPlaylist(playlist);
        }
    }

    /**
     * Loads the queue view into the center pane.
     *
     * @param queueType The type of Queue.
     * @param playlist The playlist the queue is for.
     */
    public void toQueue(QueueType queueType, Playlist playlist)
    {
        queueController.startQueue(queueType, playlist);
        loadCenterPane(queueNode, "queue");
        lblTrackView.setGraphic(playbackIcons.getTrackViewIcon());
    }

    /**
     * Loads the queue view into the center pane.
     *
     * @param queueType The type of queue.
     * @param track The track the queue is for.
     */
    public void toQueue(QueueType queueType, Track track)
    {
        queueController.startQueue(queueType, track);
        loadCenterPane(queueNode, "queue");
        lblTrackView.setGraphic(playbackIcons.getTrackViewIcon());
    }

    /**
     * Loads the playlist creation view into the center node.
     * @param event The mouse click event.
     */
    @FXML
    public void toPlaylist(ActionEvent event)
    {
        FXMLLoader loader = loadCenterPane("/Views/Playlist/Playlist.fxml", "playlist");

        if(loader != null)
        {
            // Injec the parent controller into the child.
            PlaylistController playlistController = loader.getController();
            playlistController.setParentController(this);
        }
    }

    /**
     * Loads the track list into the center node.
     * @param event The mouse click event.
     */
    @FXML
    public void toTrackList(MouseEvent event)
    {
        loadCenterPane(tracksNode, "tracks");
    }

    /**
     * Changes playback to be for the previous track in the queue.
     * @param event The mouse click event.
     */
    @FXML
    public void onPreviousClicked(MouseEvent event)
    {
        trackManager.previous();
    }

    /**
     * Toggles playback of the current piece of media.
     * @param event
     */
    @FXML
    public void onStartClicked(MouseEvent event)
    {
        trackManager.toggleTrack(null);
    }

    /**
     * Changes playback to be for the next track in the queue.
     * @param event The mouse click event.
     */
    @FXML
    public void onNextClicked(MouseEvent event)
    {
        trackManager.next();
    }

    /**
     * Handles swapping between the track view and queue view depending on what page is
     * currently loaded in the center pane.
     */
    @FXML
    public void onTrackViewClicked()
    {
        // If we are on the tracks view we need to load the queue view.
        if(currentCenterNode.compareToIgnoreCase("tracks") == 0)
        {
            lblTrackView.setGraphic(playbackIcons.getTrackViewIcon());
            toQueue(QueueType.TRACKS, trackManager.getSelectedTrack());
        }
        // If we are on the queue view we need to load the track view.
        else if (currentCenterNode.compareToIgnoreCase("queue") == 0)
        {
            trackManager.setQueuedTracks(trackManager.getTracks());
            lblTrackView.setGraphic(playbackIcons.getQueuedViewIcon());
            toTrackList(null);
        }
        // If we are on any other page then we need to swap depending on the current icon.
        else
        {
            if(lblTrackView.getGraphic() == playbackIcons.getTrackViewIcon())
            {
                lblTrackView.setGraphic(playbackIcons.getTrackViewIcon());
                toQueue(queueController.getQueueType(), trackManager.getSelectedTrack());
            }
            else
            {
                trackManager.setQueuedTracks(trackManager.getTracks());
                lblTrackView.setGraphic(playbackIcons.getQueuedViewIcon());
                toTrackList(null);
            }
        }
    }

    /**
     * Toggles shuffle mode in the track manager.
     * @param event The mouse click event.
     */
    @FXML
    public void onShuffleClicked(MouseEvent event)
    {
        trackManager.setShuffle(!trackManager.isShuffle());
        ((ExtendedIconNode)lblShuffle.getGraphic()).toggleActiveState();
    }

    /**
     * Toggles the muted state of the current piece of media.
     * @param event
     */
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
