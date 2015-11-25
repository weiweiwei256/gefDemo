package wei.learn.gef.helper;

import galaxy.ide.configurable.editor.gef.router.AStarConnectionRouter2;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class Utility
{
    private static ConnectionRouter router = null;

    public static ConnectionRouter getRouter()
    {
        router = new AStarConnectionRouter2(Utility.getCurrentEditor(), 20,
                AStarConnectionRouter2.FLOYD_SIMPLIFY
//                |AStarConnectionRouter2.FLOYD
//                        | AStarConnectionRouter2.TEST
//                        | AStarConnectionRouter2.SHOWPOOL
                        | AStarConnectionRouter2.FOUR_DIR);
        return router;
//    	return new BendpointConnectionRouter();
    }

    public static IEditorPart getCurrentEditor()
    {

        return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().getActiveEditor();

    }
}
