package Utilities;

import Helpers.Constants;
import Models.Track;
import com.google.gson.Gson;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.hildan.fxgson.FxGson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Manage functionality for playing, pausing, skipping, and queueing tracks.
 *
 * @author Nick Gordon
 * @since 3/27/2018
 */
public class TrackManager
{
    public Track SampleTrack;

    public Track getSampleTrack() {
        return this.SampleTrack;
    }

    // A full list of all the tracks in the LocalTrackSource.json file.
    private LinkedList<Track> tracks;

    // Used when the user enables shuffle play.  Contains the original unshuffle version of the queued tracks.
    private LinkedList<Track> unshuffledSet;

    // Tells if shuffle mode has been enabled by the user.
    private boolean isShuffle = false;

    /**
     * Called during the application startup.  Handles reading the tracks from the local storage
     * and storing them locally in this class.
     */
    public void initialize()
    {
        try
        {
            String path = "Storage/LocalTrackSource.json";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            // FxGson sample code source: https://stackoverflow.com/questions/32794500/serialize-javafx-model-with-gson
            Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

            // Read all the tracks into an array.
            Track[] trackSource = gson.fromJson(bufferedReader, Track[].class);

            for(Track track : trackSource)
            {
                System.out.println("Track: " + track.getName());
            }

            // Set the queued tracks, selected track, current track, and the full track listing.
            tracks = new LinkedList<>(Arrays.asList(trackSource));
            setQueuedTracks(new LinkedList<>(tracks));

            setSelectedTrack(tracks.get(0));
            setCurrentTrack(tracks.get(0));

            this.SampleTrack = tracks.get(0);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Starts and stops the playback of a track.
     * @param selectedTrack The track to toggle playback for.
     */
    public void toggleTrack(Track selectedTrack)
    {
        try
        {
            // Check if a media play is already created.
            if(getMediaPlayer() == null)
            {
                // If a media player has been created and we have a new selected track we need to configure the media
                // player for the selected track.
                if(selectedTrack != null)
                {
                    configureMedia(selectedTrack);
                }
//                else
//                {
//                    configureMedia(getCurrentTrack());
//                }
            }

            // If we playing the track we need to stop. Otherwise we can start playback.
            if(getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
            {
                getMediaPlayer().pause();
            }
            else if (getMediaPlayer().getStatus() == MediaPlayer.Status.PAUSED || getMediaPlayer().getStatus() == MediaPlayer.Status.UNKNOWN)
            {
                getMediaPlayer().play();
            }
        }
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setHeaderText("Failed to Play Track");
            alert.setContentText(ex.getMessage() + " in " + ex.getClass());
            alert.showAndWait();
        }
    }

    /**
     * Handles the creation of the MediaPlayer and sets the current track.
     * @param track The track to configure the MediaPlayer for.
     */
    public void configureMedia(Track track)
    {
        // Create a media object for the given track.
        Media media = new Media(Paths.get(track.getPath()).toUri().toString());

        // If the MediaPlayer is already configured for another track we might need to stop playback of that track.
        if(mediaPlayer.get() != null)
        {
            if(mediaPlayer.get().getStatus() == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.get().stop();
            }
        }

        // Configure the MediaPlayer for the new pieces of media.
        mediaPlayer.set(new MediaPlayer(media));

        setCurrentTrack(track);
        setCurrentTrackIndex(tracks.indexOf(getCurrentTrack()));
    }

    /**
     * Changes the current playback track to the previous track in the queue.  Will loop back to the end of the queued tracks.
     */
    public void previous()
    {
        // Get the index of the last queued track.
        int index = getQueuedTracks().size() - 1;

        // If we haven't reached the beginning of the queued track then we can get the new index otherwise we are setting the new current track
        // as the last track in the queue.
        if((getQueuedTracks().indexOf(getCurrentTrack()) - 1) >= 0)
        {
            index = getQueuedTracks().indexOf(getCurrentTrack()) - 1;
        }

        configureMedia(getQueuedTracks().get(index));
        toggleTrack(null);
        enforceProperCenterLayoutPane();
    }

    /**
     * Changes the current playback track to the next track in the queue.  Will loop back to the end of the queued tracks.
     */
    public void next()
    {
        // Set the index of the first queued track (always zero obviously).
        int index = 0;

        // If we haven't reached the end of the queued track then we can get get the new index otherwise we are setting the new current track
        // as the first track is the queue.
        if((getQueuedTracks().indexOf(getCurrentTrack()) + 1) <= (getQueuedTracks().size() - 1))
        {
            index = getQueuedTracks().indexOf(getCurrentTrack()) + 1;
        }

        configureMedia(getQueuedTracks().get(index));
        toggleTrack(null);
        enforceProperCenterLayoutPane();
    }

    /**
     * Enable or disable the track managers shuffle mode.
     * @param isShuffle
     */
    public void setShuffle(boolean isShuffle)
    {
        this.isShuffle = isShuffle;

        // If the user is enabling shuffle mode we need to shuffle the queued tracks linked list.
        if(this.isShuffle)
        {
            LinkedList<Track> shuffledList = new LinkedList<>(getQueuedTracks());
            Collections.shuffle(shuffledList);

            unshuffledSet = new LinkedList<>(getQueuedTracks());
            setQueuedTracks(shuffledList);
        }
        else
        {
            setQueuedTracks(unshuffledSet);
        }
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    // Private internal track manager functions.

    private void enforceProperCenterLayoutPane() {
        if (AppGlobal.isMp3(getCurrentTrack().getName()) && NavigationService.getCurrentFxmlPath().compareToIgnoreCase(Constants.videoPlaybackFxmlPath) == 0)
        {
            NavigationService.loadFxml(Constants.tracksFxmlPath);
        }
        else if (AppGlobal.isMp4(getCurrentTrack().getName()) && NavigationService.getCurrentFxmlPath().compareToIgnoreCase(Constants.videoPlaybackFxmlPath) != 0) {
            NavigationService.loadFxml(Constants.videoPlaybackFxmlPath);
        }
        else {
            // Do nothing we don't need to update the layout controller's center pane.
        }
    }

    // Bindable properties (Basically getters and setter).

    public LinkedList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(LinkedList<Track> tracks) {
        this.tracks = tracks;
    }

    private ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();

    public ObjectProperty mediaPlayerProperty()
    {
        return mediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer.get();
    }

    private ObjectProperty<Track> currentTrack = new SimpleObjectProperty<>();

    public ObjectProperty<Track> currentTrackProperty()
    {
        return currentTrack;
    }

    public Track getCurrentTrack() {
        return currentTrack.get();
    }

    public void setCurrentTrack(Track track)
    {
        currentTrack.set(track);
    }

    private IntegerProperty currentTrackIndex = new SimpleIntegerProperty();

    public IntegerProperty currentTrackIndexProperty()
    {
        return currentTrackIndex;
    }

    public int getCurrentTrackIndex() {
        return currentTrackIndex.get();
    }

    public void setCurrentTrackIndex(int index)
    {
        currentTrackIndex.set(index);
    }

    private ObjectProperty<Track> selectedTrack = new SimpleObjectProperty<>();

    public ObjectProperty<Track> selectedTrackProperty()
    {
        return selectedTrack;
    }

    public Track getSelectedTrack() {
        return selectedTrack.get();
    }

    public void setSelectedTrack(Track track)
    {
        selectedTrack.set(track);
    }

    private ObjectProperty<LinkedList<Track>> queuedTracks = new SimpleObjectProperty<>();

    public ObjectProperty<LinkedList<Track>> queuedTracksProperty()
    {
        return queuedTracks;
    }

    public void setQueuedTracks(LinkedList<Track> queuedTracks) {
        this.queuedTracks.set(queuedTracks);
    }

    public LinkedList<Track> getQueuedTracks() {
        return queuedTracks.get();
    }
}
