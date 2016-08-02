package wei.learn.gef.helper;

import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import wei.learn.gef.router.MyBendpointConnectionRouter;
import wei.learn.gef.ui.DiagramEditor;

public class Utility {
	private static ConnectionRouter router = null;

	public static ConnectionRouter getRouter(DiagramEditor editor) {
		// router = new AStarConnectionRouter2(editor, 20,
		// AStarConnectionRouter2.FLOYD_SIMPLIFY
		// // |AStarConnectionRouter2.FLOYD
		// // | AStarConnectionRouter2.TEST
		// // | AStarConnectionRouter2.SHOWPOOL
		// | AStarConnectionRouter2.FOUR_DIR);
		// return router;
		return new MyBendpointConnectionRouter();
		// return new ManhattanConnectionRouter();
		// return new BendpointConnectionRouter();
	}

	public static IEditorPart getCurrentEditor() {

		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

	}
	public static final Cursor BENDPOINT_VERTICAL_MOVE_CURSOR = new Cursor(null, SWT.CURSOR_SIZEN);
	public static final Cursor BENDPOINT_HORIZONTAL_MOVE_CURSOR=new Cursor(null,SWT.CURSOR_SIZEE);
	public static final Cursor BENDPOINT_NORTHEAST_MOVE_CURSOR=new Cursor(null,SWT.CURSOR_SIZENESW);
	public static final Cursor BENDPOINT_NORTHWEST_MOVE_CURSOR=new Cursor(null,SWT.CURSOR_SIZENWSE);
	public static final Cursor BENDPOINT_ALL_MOVE_CURSOR=new Cursor(null,SWT.CURSOR_SIZEALL);
}
