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

    private boolean hasPendingTrack;
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
            setQueuedTrack(tracks.get(0));
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void toggleTrack(Track queuedTrack)
    {
        if(hasPendingTrack && getQueuedTrack() != null)
        {
            if(getQueuedTrack() != null)
            {
                queuedTrack = getQueuedTrack();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setHeaderText("No Track Selected");
                alert.setContentText("Please select a track before hitting play.");
                alert.showAndWait();

                return;
            }
        }

        try
        {
            Media media = new Media(Paths.get(queuedTrack.getPath()).toUri().toString());

            if(hasPendingTrack || getQueuedTrack().getName().compareToIgnoreCase(queuedTrack.getName()) != 0)
            {
                if(mediaPlayer.get() != null)
                {
                    mediaPlayer.get().stop();

                }

                mediaPlayer.set(new MediaPlayer(media));

                setQueuedTrack(queuedTrack);
                setCurrentTrackIndex(tracks.indexOf(getQueuedTrack()));

                hasPendingTrack = false;
            }

            MediaPlayer.Status playbackStatus = mediaPlayer.get().getStatus();

            switch (playbackStatus)
            {
                case PLAYING:
                    mediaPlayer.get().pause();
                    break;
                case STOPPED:
                case UNKNOWN:
                case PAUSED:
                    mediaPlayer.get().play();
                    break;
                default:
                    break;
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

    public void pendTrack(Track track)
    {
        setQueuedTrack(track);
        hasPendingTrack = true;
    }

    public Track getPrevious()
    {
        int previousIndex = getCurrentTrackIndex() - 1;
        return getQueuedTracks().get(previousIndex);
    }

    public Track getNext()
    {
        int nextIndex = getCurrentTrackIndex() + 1;
        return getQueuedTracks().get(nextIndex);
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

            setQueuedTracks(shuffledList);
        }
        else
        {
            setQueuedTracks(tracks);
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

    private ObjectProperty<Track> queuedTrack = new SimpleObjectProperty<>();

    public ObjectProperty queuedTrackProperty()
    {
        return queuedTrack;
    }

    public void setQueuedTrack(Track currentTrack) {
        this.queuedTrack.set(currentTrack);
    }

    public Track getQueuedTrack() {
        return queuedTrack.get();
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
