package wei.learn.gef.router;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;

public class MyBendpointConnectionRouter extends AbstractRouter {
	private Map constraints = new HashMap(11);

	private static final PrecisionPoint A_POINT = new PrecisionPoint();

	/**
	 * Gets the constraint for the given {@link Connection}.
	 * 
	 * @param connection
	 *            The connection whose constraint we are retrieving
	 * @return The constraint
	 */
	public Object getConstraint(Connection connection) {
		return constraints.get(connection);
	}

	/**
	 * Removes the given connection from the map of constraints.
	 * 
	 * @param connection
	 *            The connection to remove
	 */
	public void remove(Connection connection) {
		constraints.remove(connection);
	}

	/**
	 * Routes the {@link Connection}. Expects the constraint to be a List of
	 * {@link org.eclipse.draw2d.Bendpoint Bendpoints}.
	 * 
	 * @param conn
	 *            The connection to route
	 */
	public void route(Connection conn) {
		PointList points = conn.getPoints();
		for (int i = 0; i < points.size(); i++) {
			System.err.println("point " + i + ":" + points.getPoint(i));

		}
		points.removeAllPoints();

		List bendpoints = (List) getConstraint(conn);
//		System.err.println(bendpoints);
		if (bendpoints == null)
			bendpoints = Collections.EMPTY_LIST;

		Point ref1, ref2;

		if (bendpoints.isEmpty()) {
			ref1 = conn.getTargetAnchor().getReferencePoint();
			ref2 = conn.getSourceAnchor().getReferencePoint();
		} else {
			ref1 = new Point(((Bendpoint) bendpoints.get(0)).getLocation());
			conn.translateToAbsolute(ref1);
			ref2 = new Point(
					((Bendpoint) bendpoints.get(bendpoints.size() - 1))
							.getLocation());
			conn.translateToAbsolute(ref2);
		}

		A_POINT.setLocation(conn.getSourceAnchor().getLocation(ref1));
		conn.translateToRelative(A_POINT);
		points.addPoint(A_POINT);

		for (int i = 0; i < bendpoints.size(); i++) {
			Bendpoint bp = (Bendpoint) bendpoints.get(i);
			points.addPoint(bp.getLocation());
		}

		A_POINT.setLocation(conn.getTargetAnchor().getLocation(ref2));
		conn.translateToRelative(A_POINT);
		points.addPoint(A_POINT);
		conn.setPoints(points);
	}

	/**
	 * Sets the constraint for the given {@link Connection}.
	 * 
	 * @param connection
	 *            The connection whose constraint we are setting
	 * @param constraint
	 *            The constraint
	 */
	public void setConstraint(Connection connection, Object constraint) {
		constraints.put(connection, constraint);
	}

}
