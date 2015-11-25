package wei.learn.gef.policy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import wei.learn.gef.command.CreateBendpointCommand;
import wei.learn.gef.command.DeleteBendpointCommand;
import wei.learn.gef.command.MoveBendpointCommand;
// ide: BendpointPolicy
public class CustomBendpointEditPolicy extends BendpointEditPolicy
{
    
    @Override
    protected Command getCreateBendpointCommand(BendpointRequest request)
    {
        Point point = request.getLocation();
        getConnection().translateToRelative(point);
        
        CreateBendpointCommand command = new CreateBendpointCommand();
        command.setLocation(point);
        command.setConnection(getHost().getModel());
        command.setIndex(request.getIndex());
        return command;
    }

    @Override
    protected Command getDeleteBendpointCommand(BendpointRequest request)
    {
        DeleteBendpointCommand command=new DeleteBendpointCommand();
        command.setConnection(getHost().getModel());
        command.setIndex(request.getIndex());
        return command;
    }

    @Override
    protected Command getMoveBendpointCommand(BendpointRequest request)
    {
        Point point = request.getLocation();
        getConnection().translateToRelative(point);
        
        MoveBendpointCommand command = new MoveBendpointCommand();
        command.setNewLocation(point);
        command.setConnection(getHost().getModel());
        command.setIndex(request.getIndex());
        return command;
    }
    
}
