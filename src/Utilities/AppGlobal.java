package Utilities;

import Views.Layout.LayoutController;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Defines global properties, methods, and functionality for the app.
 *
 * @author Nick Gordon
 * @since 4/10/2018
 */
public class AppGlobal
{
    private static boolean initStarted = false;

    /**
     * Initializes any necessary classes on application startup.
     */
    public static void init(LayoutController controller)
    {
        // If we haven't started the init process we can begin.
        if(!initStarted)
        {
            initStarted = true;
            getTrackManagerInstance().initialize();
            setLayoutController(controller);
        }
    }

    // Application singletons.

    private static TrackManager trackManager;

    /**
     * The singleton instance of the TrackManager class.
     * @return Singleton TrackManager instance.
     */
    public static TrackManager getTrackManagerInstance()
    {
        if(trackManager == null)
        {
            trackManager = new TrackManager();
        }

        return trackManager;
    }

    // Application variables

    private static Stage currentStage;

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
    }

    private static LayoutController layoutController;

    public static LayoutController getLayoutController() {
        return layoutController;
    }

    public static void setLayoutController(LayoutController controller) {
        layoutController = controller;
    }

    private static double stageX;

    public static double getStageX() {
        return stageX;
    }

    public static void setStageX(double x) {
        stageX = x;
    }

    private static double stageY;

    public static double getStageY() {
        return stageY;
    }

    public static void setStageY(double y) {
        stageY = y;
    }

    // Application functions

    public static void shutdownApplication()
    {
        Platform.exit();
        System.exit(0);
    }
}
