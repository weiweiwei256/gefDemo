package galaxy.ide.configurable.editor.gef.router;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.swt.graphics.Color;

/**
 * 这个类是用于标记预读池中的不同点的难度信息
 * @author runner
 *
 */
public class PoolHandler extends SquareHandle
{
    public ANodeLevel level = ANodeLevel.EASY;

    public void setOwner(GraphicalEditPart editpart)
    {
        super.setOwner(editpart);
    }

    @Override
    protected DragTracker createDragTracker()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void validate()
    {
        if (isValid())
            return;
        // getLocator().relocate(this);
        // super.validate();
    }

    protected Color getFillColor()
    {
        ANodeLevel localLevel = getLevel();
        Color color = null;
        if (localLevel.equals(ANodeLevel.EASY))
        {
            color = ColorConstants.lightBlue;
        } else if (localLevel.equals(ANodeLevel.NORMAL))
        {
            color = ColorConstants.yellow;
        } else if (localLevel.equals(ANodeLevel.HARD))
        {
            color = ColorConstants.darkBlue;
        } else if (localLevel.equals(ANodeLevel.DEAD))
        {
            color = ColorConstants.black;
        }
        return color;
    }

    public void paintFigure(Graphics g)
    {
        Rectangle r = getBounds();
        r.shrink(1, 1);
        try
        {
            g.setAlpha(130);
            g.setBackgroundColor(getFillColor());
            g.fillRectangle(r.x, r.y, r.width, r.height);
            g.setForegroundColor(getBorderColor());
            g.drawRectangle(r.x, r.y, r.width, r.height);
        } finally
        {
            // We don't really own rect 'r', so fix it.
            r.expand(1, 1);
        }
    }

    public ANodeLevel getLevel()
    {
        return level;
    }
}
