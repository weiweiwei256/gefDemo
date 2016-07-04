package wei.learn.gef.helper;

import galaxy.ide.configurable.editor.gef.router.AStarConnectionRouter2;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import wei.learn.gef.ui.DiagramEditor;

public class Utility
{
    private static ConnectionRouter router = null;

    public static ConnectionRouter getRouter(DiagramEditor editor)
    {
//        router = new AStarConnectionRouter2(editor, 20,
//                AStarConnectionRouter2.FLOYD_SIMPLIFY
////                |AStarConnectionRouter2.FLOYD
////                        | AStarConnectionRouter2.TEST
////                        | AStarConnectionRouter2.SHOWPOOL
//                        | AStarConnectionRouter2.FOUR_DIR);
//        return router;
//    	return new ManhattanConnectionRouter();
    	return new BendpointConnectionRouter();
    }

    public static IEditorPart getCurrentEditor()
    {

        return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().getActiveEditor();

    }
}
