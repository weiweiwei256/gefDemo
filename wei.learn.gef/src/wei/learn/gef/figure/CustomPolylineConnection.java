package wei.learn.gef.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

public class CustomPolylineConnection extends PolylineConnection {
	private static final int OFFSET = 2;
	private Point originStartPoint;
	private Point originEndPoint;

	@Override
	protected void outlineShape(Graphics g) {
		PointList points = getPoints();
		if (points.size() <= 2) {
			super.outlineShape(g);
		} else if (points.size() > 2)// 存在拐点的情况
		{
			PointList shoulderPoints = new PointList();
			shoulderPoints.addPoint(points.getFirstPoint());
			for (int i = 1; i < points.size() - 1; i++)// 遍历除两端的点,添加肩膀
			{
				Point forwardPoint = points.getPoint(i - 1);
				Point anglePoint = points.getPoint(i);
				Point nextPoint = points.getPoint(i + 1);
				if (forwardPoint.x == anglePoint.x) // 竖直线
				{
					if (forwardPoint.y < anglePoint.y) // 向下
					{
						shoulderPoints.addPoint(anglePoint.x, anglePoint.y
								- OFFSET);
					} else if (forwardPoint.y > anglePoint.y) // 向上
					{
						shoulderPoints.addPoint(anglePoint.x, anglePoint.y
								+ OFFSET);
					}

					// anglePoint 和 nextPoint是水平线
					if (anglePoint.x < nextPoint.x) { // 向右
						shoulderPoints.addPoint(anglePoint.x + OFFSET,
								anglePoint.y);
					} else if (anglePoint.x > nextPoint.x) // 向左
					{
						shoulderPoints.addPoint(anglePoint.x - OFFSET,
								anglePoint.y);
					}
				} else if (forwardPoint.y == anglePoint.y) // 水平线
				{
					if (forwardPoint.x < anglePoint.x) // 向右
					{
						shoulderPoints.addPoint(anglePoint.x - OFFSET,
								anglePoint.y);
					} else if (forwardPoint.x > anglePoint.x) // 向左
					{
						shoulderPoints.addPoint(anglePoint.x + OFFSET,
								anglePoint.y);
					}

					// anglePoint 和 nextPoint是竖直线
					if (anglePoint.y < nextPoint.y) { // 向下
						shoulderPoints.addPoint(anglePoint.x, anglePoint.y
								+ OFFSET);
					} else if (anglePoint.y > nextPoint.y) { // 向上
						shoulderPoints.addPoint(anglePoint.x, anglePoint.y
								- OFFSET);
					}
				}
			}
			shoulderPoints.addPoint(points.getLastPoint());
			g.drawPolyline(shoulderPoints);
		}
	}

	public Point getOriginStartPoint() {
		return originStartPoint;
	}

	public void setOriginStartPoint(Point originStartPoint) {
		this.originStartPoint = originStartPoint;
	}

	public Point getOriginEndPoint() {
		return originEndPoint;
	}

	public void setOriginEndPoint(Point originEndPoint) {
		this.originEndPoint = originEndPoint;
	}
}
