package galaxy.ide.configurable.editor.gef.router;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.swt.graphics.Color;

/**
 * @author caiyu
 * @date 2014-5-14
 */
public class TestHandler extends SquareHandle {
    public boolean open;

    public void setOwner(GraphicalEditPart editpart) {
        super.setOwner(editpart);
    }

    @Override
    protected DragTracker createDragTracker() {
        // TODO Auto-generated method stub
        return null;
    }

    public void validate() {
        if (isValid())
            return;
        // getLocator().relocate(this);
        // super.validate();
    }

    protected Color getFillColor() {
        return (isOpen()) ? ColorConstants.gray : ColorConstants.green;
    }

    public void paintFigure(Graphics g) {
        Rectangle r = getBounds();
        r.shrink(1, 1);
        try {
            g.setAlpha(130);
            g.setBackgroundColor(getFillColor());
            g.fillRectangle(r.x, r.y, r.width, r.height);
            g.setForegroundColor(getBorderColor());
            g.drawRectangle(r.x, r.y, r.width, r.height);
        } finally {
            // We don't really own rect 'r', so fix it.
            r.expand(1, 1);
        }
    }

    public boolean isOpen() {
        return open;
    }
}
