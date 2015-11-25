package wei.learn.gef.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

public class ArrowConnectionEditPart extends CustomAbstractConnectionEditPart
{
    protected IFigure createFigure()
    {
        // 还是多义线连接
        PolylineConnection connection = (PolylineConnection) super.createFigure();
        // 不过这里加上了修饰
        connection.setTargetDecoration(new PolygonDecoration());
        return connection;
    }

}
