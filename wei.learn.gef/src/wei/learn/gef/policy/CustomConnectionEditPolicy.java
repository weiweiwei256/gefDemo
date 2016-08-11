package wei.learn.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import wei.learn.gef.command.DeleteConnectionCommand;

public class CustomConnectionEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		DeleteConnectionCommand command = new DeleteConnectionCommand();
		command.setConnectionModel(getHost().getModel());
		return command;
	}

}
