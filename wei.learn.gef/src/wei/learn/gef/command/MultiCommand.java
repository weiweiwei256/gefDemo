package wei.learn.gef.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

public class MultiCommand extends Command {
	private List<Command> cmds;

	@Override
	public void execute() {
		for(Command cmd:cmds)
		{
			cmd.execute();
		}
	}

	@Override
	public boolean canExecute() {
		for(Command cmd:cmds)
		{
			if(!cmd.canExecute())
			{
				return false;
			}
		}
		return true;
	}
	public void setCommands(List<Command> cmds)
	{
		this.cmds = cmds;
	}

	@Override
	public void undo() {
		for(Command cmd:cmds)
		{
		 cmd.undo();
		}
	}
}
