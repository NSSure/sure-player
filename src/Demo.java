import Models.Track;
import Utilities.TrackManager;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Demo
{
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException
    {
        TrackManager trackManager = new TrackManager();
        trackManager.initialize();

        Track sampleTrack = trackManager.getSampleTrack();

        int totalFramesRead = 0;

        File file = new File(sampleTrack.getPath());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        int bytesPerFrame = audioInputStream.getFormat().getFrameSize();

        if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
            // If an audio file does not specify the frame size we can ready any
            // amount of bytes that we want.
            bytesPerFrame = 1;
        }

        // Set an arbitrary buffer size of 1024 frames.
        int numBytes = 1024 * bytesPerFrame;
        byte[] audioBytes = new byte[numBytes];

        int numOfBytesRead = 0;
        int numFramesRead = 0;

        // Try to read the bytes from the file.
        while((numOfBytesRead = audioInputStream.read(audioBytes)) != -1) {
            // Calculate the number of frames actually needed.
            numFramesRead = numOfBytesRead / bytesPerFrame;
            totalFramesRead += numFramesRead;

            // Here do something with the audio data that's now in audioBytes.
        }
    }
}
