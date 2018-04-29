package Utilities;

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

public class TrackManager
{
    private LinkedList<Track> tracks;
    private LinkedList<Track> unshuffledSet;
    private boolean isShuffle = false;

    public void initialize()
    {
        try
        {
            String path = "Storage/LocalTrackSource.json";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            // FxGson sample code source: https://stackoverflow.com/questions/32794500/serialize-javafx-model-with-gson
            Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

            Track[] trackSource = gson.fromJson(bufferedReader, Track[].class);

            for(Track track : trackSource)
            {
                System.out.println("Track: " + track.getName());
            }

            tracks = new LinkedList<>(Arrays.asList(trackSource));
            setQueuedTracks(new LinkedList<>(tracks));

            setSelectedTrack(tracks.get(0));
            setCurrentTrack(tracks.get(0));
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void toggleTrack(Track selectedTrack)
    {
        try
        {
            if(getMediaPlayer() == null)
            {
                if(selectedTrack != null)
                {
                    configureMedia(selectedTrack);
                }
                else
                {
                    configureMedia(getCurrentTrack());
                }
            }

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

    public void configureMedia(Track track)
    {
        Media media = new Media(Paths.get(track.getPath()).toUri().toString());

        if(mediaPlayer.get() != null)
        {
            if(mediaPlayer.get().getStatus() == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.get().stop();
            }
        }

        mediaPlayer.set(new MediaPlayer(media));

        setCurrentTrack(track);
        setCurrentTrackIndex(tracks.indexOf(getCurrentTrack()));
    }

    public void previous()
    {
        if((getCurrentTrackIndex() - 1) < 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Invalid Action");
            alert.setHeaderText(null);
            alert.setContentText("You are at the beginner of the track queue.  You are unable to go to a previous track");
            alert.showAndWait();
        }
        else
        {
            configureMedia(getQueuedTracks().get(getCurrentTrackIndex() - 1));
            toggleTrack(null);
        }
    }

    public void next()
    {
        if((getCurrentTrackIndex() + 1) > (getQueuedTracks().size() - 1))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Invalid Action");
            alert.setHeaderText(null);
            alert.setContentText("Reached the end of the track queue");
            alert.showAndWait();
        }
        else
        {
            configureMedia(getQueuedTracks().get(getCurrentTrackIndex() + 1));
            toggleTrack(null);
        }
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean isShuffle)
    {
        this.isShuffle = isShuffle;

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
