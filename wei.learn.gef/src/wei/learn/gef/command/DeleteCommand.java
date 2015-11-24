package wei.learn.gef.command;


import org.eclipse.gef.commands.Command;

import wei.learn.gef.model.ContentsModel;
import wei.learn.gef.model.HelloModel;

public class DeleteCommand extends Command {
	private ContentsModel contentsModel;
	private HelloModel helloModel;
	@Override
	public void execute() {
		contentsModel.removeChild(helloModel);
	}
	public void setContentsModel(Object model)
	{
		contentsModel =(ContentsModel)model;
	}
	public void setHelloModel(Object model)
	{
		helloModel = (HelloModel) model;
	}
	@Override
	public void undo() {
		contentsModel.addChild(helloModel);
	}
}
