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
public class SpecialHandler extends SquareHandle
{
    public void setOwner(GraphicalEditPart editpart)
    {
        super.setOwner(editpart);
    }

    @Override
    protected DragTracker createDragTracker()
    {
        return null;
    }

    public void validate()
    {
        if (isValid())
            return;
    }

    protected Color getFillColor()
    {
        return ColorConstants.darkGray;
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
}
