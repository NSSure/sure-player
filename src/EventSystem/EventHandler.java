package EventSystem;

/**
 * Create on 4/10/2018 by Nick Gordon
 */
public class EventHandler
{
    private Runnable action;

    public EventHandler(Runnable action)
    {
        this.action = action;
    }

    public boolean canExecute()
    {
        if(action == null)
        {
            return false;
        }

        return true;
    }


    public void execute()
    {
        action.run();
    }
}
