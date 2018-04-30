package Icons;

import javafx.scene.paint.Color;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconNode;

/**
 * Extends the IconNode class with extra functionality for swap the active icon color.
 *
 * @author Nick Gordon
 * @since 4/19/2018
 */
public class ExtendedIconNode extends IconNode
{
    private boolean active = false;

    public ExtendedIconNode(FontAwesome icon)
    {
        super(icon);
    }

    /**
     * Swaps the active state color for the icon node.
     */
    public void toggleActiveState()
    {
        if(active)
        {
            active = false;
            setFill(Color.valueOf("#A7A7A7"));
        }
        else
        {
            active = true;
            setFill(Color.valueOf("#6EC274"));
        }
    }
}
