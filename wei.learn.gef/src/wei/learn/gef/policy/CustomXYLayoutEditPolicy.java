package wei.learn.gef.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import wei.learn.gef.command.ChangeConstraintCommand;
import wei.learn.gef.command.CreateCommand;
import wei.learn.gef.model.HelloModel;

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

	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		HelloModel model = (HelloModel) child.getModel();
		Rectangle newCons = (Rectangle) constraint;
		ChangeConstraintCommand consCmd = new ChangeConstraintCommand();
		consCmd.setModel(model);
		consCmd.setConstraint(newCons);
		return consCmd;
	};
}
