package EventSystem;

/**
 * Allows classes to define events and have their parent classes
 * execute code when that specific event occurs.
 *
 * @author Nick Gordon
 * @since 4/10/2018
 */
public class EventHandler
{
    private Runnable action;

    public EventHandler(Runnable action)
    {
        this.action = action;
    }

    /**
     * Determines if their is an action assigned.  If their is one the action
     * can be executed otherwise the action can not be executed.
     *
     * @return A boolean that says if the action can be executed.
     */
    public boolean canExecute()
    {
        if(action == null)
        {
            return false;
        }

        return true;
    }

    /**
     * Executes the assigned action.
     */
    public void execute()
    {
        action.run();
    }
}
