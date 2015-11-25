package wei.learn.gef.action;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

public class DiagramActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		// Retarget 缩放Action
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
	}

	@Override
	protected void declareGlobalActionKeys() {
		// TODO Auto-generated method stub

	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		// 加上分割条
		toolBarManager.add(new Separator());
		// 加上缩放按钮，注意这里的缩放Action 的ID 在GEF 中已经定义了一些常数
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
		//在工具条上加上组合框(如果需要在工具条上加其他组合框的可以研究一下ZoomComboContributionItem的源码)
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
	}
}
