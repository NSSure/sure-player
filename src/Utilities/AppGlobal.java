package Utilities;

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
    public static void init()
    {
        // If we haven't started the init process we can begin.
        if(!initStarted)
        {
            initStarted = true;
            getTrackManagerInstance().initialize();
        }
    }

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
}
