package wei.learn.gef.editpart;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import wei.learn.gef.model.ContentsModel;

public class ContentsEditPart extends AbstractGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		Layer figure = new Layer();
		figure.setLayoutManager(new XYLayout());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
	@Override
	protected List<Object> getModelChildren() {
		return ((ContentsModel)getModel()).getChildren();
	}
}
