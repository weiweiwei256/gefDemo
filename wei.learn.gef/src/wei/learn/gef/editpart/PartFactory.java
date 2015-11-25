package wei.learn.gef.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import wei.learn.gef.model.ContentsModel;
import wei.learn.gef.model.HelloModel;
import wei.learn.gef.model.LineConnectionModel;

//ide:GraphicalControlFactory
public class PartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// get EditPart for model element
		EditPart part = getPartForElement(model);
		// store model element in editPart
		part.setModel(model);
		return part;
	}

	private EditPart getPartForElement(Object model) {
		if (model instanceof HelloModel) {
			return new HelloEditorPart();
		} else if (model instanceof ContentsModel) {
			return new ContentsEditPart();
		} else if(model instanceof LineConnectionModel)
		{
			return new LineConnectionEditPart();
		}
		throw new RuntimeException("Can't create part for model element:"
				+ ((model != null) ? model.getClass().getName() : "null"));
	}

}
