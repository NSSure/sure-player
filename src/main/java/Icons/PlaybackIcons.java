package Icons;

import javafx.scene.paint.Color;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconNode;

/**
 * Create on 4/12/2018 by Nick Gordon
 */
public class PlaybackIcons
{
    private IconNode defaultVolumeIcon;

    private IconNode mutedVolumeIcon;

    private IconNode playCircleIcon;

    private IconNode stepForwardIcon;

    private IconNode stepBackwardIcon;

    public PlaybackIcons()
    {
        defaultVolumeIcon = new IconNode(FontAwesome.VOLUME_DOWN);
        defaultVolumeIcon.setIconSize(25);
        defaultVolumeIcon.setFill(Color.valueOf("#A7A7A7"));

        mutedVolumeIcon = new IconNode(FontAwesome.VOLUME_DOWN);
        mutedVolumeIcon.setIconSize(25);
        mutedVolumeIcon.setFill(Color.valueOf("#A7A7A7"));

        playCircleIcon = new IconNode(FontAwesome.PLAY_CIRCLE_O);
        playCircleIcon.setIconSize(35);
        playCircleIcon.setFill(Color.valueOf("#A7A7A7"));

        stepForwardIcon = new IconNode(FontAwesome.STEP_FORWARD);
        stepForwardIcon.setIconSize(20);
        stepForwardIcon.setFill(Color.valueOf("#A7A7A7"));

        stepBackwardIcon = new IconNode(FontAwesome.STEP_BACKWARD);
        stepBackwardIcon.setIconSize(20);
        stepBackwardIcon.setFill(Color.valueOf("#A7A7A7"));
    }

    public IconNode getDefaultVolumeIcon() {
        return defaultVolumeIcon;
    }

    public void setDefaultVolumeIcon(IconNode defaultVolumeIcon) {
        this.defaultVolumeIcon = defaultVolumeIcon;
    }

    public IconNode getMutedVolumeIcon() {
        return mutedVolumeIcon;
    }

    public void setMutedVolumeIcon(IconNode mutedVolumeIcon) {
        this.mutedVolumeIcon = mutedVolumeIcon;
    }

    public IconNode getPlayCircleIcon() {
        return playCircleIcon;
    }

    public void setPlayCircleIcon(IconNode playCircleIcon) {
        this.playCircleIcon = playCircleIcon;
    }

    public IconNode getStepForwardIcon() {
        return stepForwardIcon;
    }

    public void setStepForwardIcon(IconNode stepForwardIcon) {
        this.stepForwardIcon = stepForwardIcon;
    }

    public IconNode getStepBackwardIcon() {
        return stepBackwardIcon;
    }

    public void setStepBackwardIcon(IconNode stepBackwardIcon) {
        this.stepBackwardIcon = stepBackwardIcon;
    }
}
