package Utilities;

/**
 * Created on 4/10/2018 by Nick Gordon
 */
public class AppGlobal
{
    private static boolean initStarted = false;

    public static void init()
    {
        if(!initStarted)
        {
            initStarted = true;
            getTrackManagerInstance().initialize();
        }
    }

    private static TrackManager trackManager;

    public static TrackManager getTrackManagerInstance()
    {
        if(trackManager == null)
        {
            trackManager = new TrackManager();
        }

        return trackManager;
    }
}
