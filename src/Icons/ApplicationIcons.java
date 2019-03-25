package Icons;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconNode;

/**
 * Wrapper object that contains all of the page icons.
 *
 * @author Nick Gordon
 * @since 4/12/2018
 */
public class ApplicationIcons
{
    private ExtendedIconNode defaultVolumeIcon;

    private ExtendedIconNode mutedVolumeIcon;

    private ExtendedIconNode playCircleIcon;

    private ExtendedIconNode pauseCircleIcon;

    private ExtendedIconNode stepForwardIcon;

    private ExtendedIconNode stepBackwardIcon;

    private ExtendedIconNode shuffleIcon;

    private ExtendedIconNode queuedViewIcon;

    private ExtendedIconNode trackViewIcon;

    private ExtendedIconNode closeIcon;

    private ExtendedIconNode maximizeIcon;

    private ExtendedIconNode minimizeIcon;

    private static Color defaultColor = Color.valueOf("#A7A7A7");
    private static Color activeColor = Color.valueOf("#1FA67A");

    public ApplicationIcons()
    {
        // Set the default values for all of the page icons.

        defaultVolumeIcon = new ExtendedIconNode(FontAwesome.VOLUME_DOWN);
        defaultVolumeIcon.setIconSize(25);
        defaultVolumeIcon.setFill(Color.valueOf("#A7A7A7"));

        mutedVolumeIcon = new ExtendedIconNode(FontAwesome.VOLUME_DOWN);
        mutedVolumeIcon.setIconSize(25);
        mutedVolumeIcon.setFill(Color.valueOf("#A7A7A7"));

        playCircleIcon = new ExtendedIconNode(FontAwesome.PLAY_CIRCLE_O);
        playCircleIcon.setIconSize(35);
        playCircleIcon.setFill(Color.valueOf("#A7A7A7"));


        pauseCircleIcon = new ExtendedIconNode(FontAwesome.PAUSE_CIRCLE_O);
        pauseCircleIcon.setIconSize(35);
        pauseCircleIcon.setFill(Color.valueOf("#A7A7A7"));

        stepForwardIcon = new ExtendedIconNode(FontAwesome.STEP_FORWARD);
        stepForwardIcon.setIconSize(20);
        stepForwardIcon.setFill(Color.valueOf("#A7A7A7"));

        stepBackwardIcon = new ExtendedIconNode(FontAwesome.STEP_BACKWARD);
        stepBackwardIcon.setIconSize(20);
        stepBackwardIcon.setFill(Color.valueOf("#A7A7A7"));

        shuffleIcon = new ExtendedIconNode(FontAwesome.EXCHANGE);
        shuffleIcon.setIconSize(20);
        shuffleIcon.setFill(Color.valueOf("#A7A7A7"));

        queuedViewIcon = new ExtendedIconNode(FontAwesome.LIST_OL);
        queuedViewIcon.setIconSize(20);
        queuedViewIcon.setFill(Color.valueOf("#A7A7A7"));

        trackViewIcon = new ExtendedIconNode(FontAwesome.LIST);
        trackViewIcon.setIconSize(20);
        trackViewIcon.setFill(Color.valueOf("#A7A7A7"));

        closeIcon = new ExtendedIconNode(FontAwesome.WINDOW_CLOSE);
        closeIcon.setIconSize(16);
        closeIcon.setFill(Color.valueOf("#DD4F42"));

        minimizeIcon = new ExtendedIconNode(FontAwesome.WINDOW_MINIMIZE);
        minimizeIcon.setIconSize(16);
        minimizeIcon.setFill(Color.valueOf("#A7A7A7"));

        maximizeIcon = new ExtendedIconNode(FontAwesome.WINDOW_RESTORE);
        maximizeIcon.setIconSize(16);
        maximizeIcon.setFill(Color.valueOf("#A7A7A7"));
    }

    public IconNode getDefaultVolumeIcon() {
        return defaultVolumeIcon;
    }

    public void setDefaultVolumeIcon(ExtendedIconNode defaultVolumeIcon) {
        this.defaultVolumeIcon = defaultVolumeIcon;
    }

    public IconNode getMutedVolumeIcon() {
        return mutedVolumeIcon;
    }

    public void setMutedVolumeIcon(ExtendedIconNode mutedVolumeIcon) {
        this.mutedVolumeIcon = mutedVolumeIcon;
    }

    public ExtendedIconNode getPlayCircleIcon() {
        return playCircleIcon;
    }

    public void setPlayCircleIcon(ExtendedIconNode playCircleIcon) {
        this.playCircleIcon = playCircleIcon;
    }

    public ExtendedIconNode getPauseCircleIcon() {
        return pauseCircleIcon;
    }

    public void setPauseCircleIcon(ExtendedIconNode pauseCircleIcon) {
        this.pauseCircleIcon = pauseCircleIcon;
    }

    public ExtendedIconNode getStepForwardIcon() {

        return stepForwardIcon;
    }

    public ExtendedIconNode getShuffleIcon() {
        return shuffleIcon;
    }

    public ExtendedIconNode getTrackViewIcon() {
        return trackViewIcon;
    }

    public void setTrackViewIcon(ExtendedIconNode trackViewIcon) {
        this.trackViewIcon = trackViewIcon;
    }

    public void setShuffleIcon(ExtendedIconNode shuffleIcon) {
        this.shuffleIcon = shuffleIcon;

    }

    public void setStepForwardIcon(ExtendedIconNode stepForwardIcon) {
        this.stepForwardIcon = stepForwardIcon;
    }

    public ExtendedIconNode getStepBackwardIcon() {
        return stepBackwardIcon;
    }

    public void setStepBackwardIcon(ExtendedIconNode stepBackwardIcon) {
        this.stepBackwardIcon = stepBackwardIcon;
    }

    public ExtendedIconNode getQueuedViewIcon() {
        return queuedViewIcon;
    }

    public void setQueuedViewIcon(ExtendedIconNode queuedViewIcon) {
        this.queuedViewIcon = queuedViewIcon;
    }

    public ExtendedIconNode getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(ExtendedIconNode closeIcon) {
        this.closeIcon = closeIcon;
    }

    public ExtendedIconNode getMinimizeIcon() {
        return minimizeIcon;
    }

    public void setMinimizeIcon(ExtendedIconNode minimizeIcon) {
        this.minimizeIcon = minimizeIcon;
    }

    public ExtendedIconNode getMaximizeIcon() {
        return maximizeIcon;
    }

    public void setMaximizeIcon(ExtendedIconNode maximizeIcon) {
        this.maximizeIcon = maximizeIcon;
    }
}
