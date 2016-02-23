package wei.learn.gef.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.handles.ConnectionHandle;

import wei.learn.gef.handle.HomunculeConnectionEndHandle;
import wei.learn.gef.handle.HomunculeConnectionStartHandle;
//对应ABIDE 的FlowWireEndpointPolicy
public class CustomConnectionEndpointEditPolicy extends
		ConnectionEndpointEditPolicy {
	//选中线之后 起始端点的handle样式
	@Override
	protected List createSelectionHandles() {
		List<ConnectionHandle> list = new ArrayList<ConnectionHandle>();
		list.add(new HomunculeConnectionEndHandle(
				(ConnectionEditPart) getHost()));
		list.add(new HomunculeConnectionStartHandle(
				(ConnectionEditPart) getHost()));
		return list;
	}
}
