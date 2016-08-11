package wei.learn.gef.policy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import wei.learn.gef.command.ChangeConstraintCommand;
import wei.learn.gef.command.CreateBendpointCommand;
import wei.learn.gef.command.CreateCommand;
import wei.learn.gef.command.MoveBendpointCommand;
import wei.learn.gef.command.MultiCommand;
import wei.learn.gef.editpart.HelloEditorPart;
import wei.learn.gef.helper.Utility;
import wei.learn.gef.model.AbstractConnectionModel;
import wei.learn.gef.model.HelloModel;
import wei.learn.gef.ui.DiagramEditor;

public class CustomXYLayoutEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		CreateCommand command = new CreateCommand();
		// 产生创建图形的尺寸和位置
		Rectangle constraint = (Rectangle) getConstraintFor(request);
		HelloModel model = (HelloModel) request.getNewObject();
		model.setConstraint(constraint);
		command.setContentsModel(getHost().getModel());
		command.setHelloModel(model);
		return command;
	}

	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		// 数据准备
		HelloModel model = (HelloModel) child.getModel();
		Rectangle newCons = (Rectangle) constraint;
		DiagramEditor editor = (DiagramEditor) Utility.getCurrentEditor();
		HelloEditorPart helloEditPart = (HelloEditorPart) editor
				.getGraphicalViewer().getEditPartRegistry().get(model);
		IFigure helloFigure = (IFigure) helloEditPart.getFigure(); // 原有连线
		Point newStart = new Point();
		Rectangle r = new Rectangle((Rectangle) constraint);
		if (request.getType().equals(RequestConstants.REQ_MOVE_CHILDREN)) //移动没有weight和height的数据
		{
			r.width = helloFigure.getBounds().width;
			r.height = helloFigure.getBounds().height;
		}
		r.translate(-1, -1);
		r.resize(1, 1);
		helloFigure.translateToAbsolute(r);
		newStart.x = r.x + r.width / 2;
		newStart.y = r.y + r.height-10;
//		System.err.println("resize" + request.getSizeDelta());
//		System.err.println("direction:" + request.getResizeDirection());
		// if (request.getType().equals(RequestConstants.REQ_MOVE_CHILDREN)) {
		// Rectangle currentCons = model.getConstraint();
		// offset.x = newCons.x - currentCons.x;
		// offset.y = 0;
		// } else if (request.getType().equals(
		// RequestConstants.REQ_RESIZE_CHILDREN)) {
		// int direction = request.getResizeDirection();
		// if (direction == PositionConstants.EAST
		// || direction == PositionConstants.NORTH_EAST
		// || direction == PositionConstants.SOUTH_EAST) {
		// offset.x = request.getSizeDelta().width / 2;
		// } else if (direction == PositionConstants.WEST
		// || direction == PositionConstants.NORTH_WEST
		// || direction == PositionConstants.SOUTH_WEST) {
		// offset.x = -request.getSizeDelta().width / 2;
		// }
		// }

		MultiCommand mulitCmd = new MultiCommand();
		List<Command> cmds = new ArrayList<Command>();
		mulitCmd.setCommands(cmds);
		// 构建移动图源的command
		ChangeConstraintCommand consCmd = new ChangeConstraintCommand();
		consCmd.setModel(model);
		consCmd.setConstraint(newCons);
		cmds.add(consCmd);
		// 调整相关联的连线
		// 以之为源的线
		for (Object line : model.getModelSourceConnection()) {
			AbstractConnectionModel lineModel = (AbstractConnectionModel) line;
			List<Point> bendpoints = lineModel.getBendpoints();
			if (!bendpoints.isEmpty()) {
				Point nPoint = bendpoints.get(0); // 拿出第一个控制点 即:开始点的下一个点
				MoveBendpointCommand moveCmd = new MoveBendpointCommand();
				moveCmd.setConnection(lineModel);
				moveCmd.setIndex(new int[] { 0 });
				moveCmd.setNewLocation(new Point[] { new Point(newStart.x,
						nPoint.y) });
				cmds.add(moveCmd);
			} else {
//				HelloEditorPart lineEditPart = (HelloEditorPart) editor
//						.getGraphicalViewer().getEditPartRegistry().get(model);
//				Connection conn = (Connection) lineEditPart.getFigure();
//				
//				HelloEditorPart targetEditPart = (HelloEditorPart) editor
//						.getGraphicalViewer().getEditPartRegistry().get(	lineModel.getTarget());
//				IFigure targetFigure = (IFigure) targetEditPart.getFigure();
//				
//				
//				CreateBendpointCommand addCmd = new CreateBendpointCommand();
//				addCmd.setIndex(0);
//				addCmd.setConnection(conn);
//				addCmd.setLocation();
			}

		}

		return mulitCmd;
	};
}
