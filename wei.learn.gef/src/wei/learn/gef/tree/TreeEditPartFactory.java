package wei.learn.gef.tree;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import wei.learn.gef.model.ContentsModel;
import wei.learn.gef.model.HelloModel;

public class TreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if(model instanceof ContentsModel)
		{
			editPart = new ContentsTreeEditPart();
		}
		else if(model instanceof HelloModel)
		{
			editPart = new HelloTreeEditPart();
		}
		if(editPart!=null)
		{
			editPart.setModel(model);
		}
		return editPart;
	}

}
