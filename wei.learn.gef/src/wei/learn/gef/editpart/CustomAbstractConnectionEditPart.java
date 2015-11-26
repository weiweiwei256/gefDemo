package wei.learn.gef.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import wei.learn.gef.helper.Utility;
import wei.learn.gef.policy.CustomConnectionEditPolicy;
import wei.learn.gef.policy.CustomConnectionEndpointEditPolicy;

public class CustomAbstractConnectionEditPart extends
		AbstractConnectionEditPart {

	@Override
	protected IFigure createFigure() {
		// 还是多义线连接
        PolylineConnection conn = (PolylineConnection) super.createFigure();
        conn.setConnectionRouter(Utility.getRouter());
        return conn;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new CustomConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new CustomConnectionEndpointEditPolicy());

	}

}