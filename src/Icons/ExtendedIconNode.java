package Icons;

import javafx.scene.paint.Color;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconNode;

/**
 * Created on 4/19/2018 by Nick Gordon
 */
public class ExtendedIconNode extends IconNode
{
    private boolean active = false;

    public ExtendedIconNode(FontAwesome icon)
    {
        super(icon);
    }

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
