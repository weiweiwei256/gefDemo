package wei.learn.gef.command;

import org.eclipse.gef.commands.Command;

import wei.learn.gef.model.HelloModel;

public class DirectEditCommand extends Command {
	private String oldText,newText;
	private HelloModel helloModel;
	
	@Override
	public void execute() {
		oldText = helloModel.getText();
		helloModel.setText(newText);
	}


	public void setText(String text) {
		this.newText = text;
	}

	public void setModel(Object model) {
		this.helloModel = (HelloModel) model;
	}
	
	@Override
	public void undo() {
		helloModel.setText(oldText);
	}
	
}
