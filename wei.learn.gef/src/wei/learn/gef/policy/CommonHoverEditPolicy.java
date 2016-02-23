package wei.learn.gef.policy;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.swt.widgets.Display;

/**
 * 
 * @author caiyu
 * 
 */
public abstract class CommonHoverEditPolicy extends GraphicalEditPolicy {
	public static final String ROLE = "CommonLineAssistantEditPolicy";

	protected IFigure handleLayer;
	/* the List of handles */
	protected List<Handle> handles;

	/* the Hover Listener */
	private MouseMotionListener hoverListener;

	/* listener for every handle */
	private MouseMotionListener handleListener;

	private LayoutListener removeListener;

	private Timer timer;

	private boolean hovering = false;

	protected int TIME_INTERVAL = 1000;

	public void activate() {
		super.activate();
		addHoverListener();
		addHandleListener();
	}

	private void addHandleListener() {
		handleListener = new MouseMotionListener() {

			public void mouseDragged(MouseEvent me) {
			}

			public void mouseEntered(MouseEvent me) {

			}

			public void mouseExited(MouseEvent me) {
				cancelTimer();
				startTimer();
			}

			public void mouseHover(MouseEvent me) {
			}

			public void mouseMoved(MouseEvent me) {

			}
		};
	}

	protected void addHoverListener() {
		final IFigure figure = getHostFigure();
		hoverListener = new MouseMotionListener() {
			public void mouseDragged(MouseEvent me) {
			}

			public void mouseEntered(MouseEvent me) {

			}

			public void mouseExited(MouseEvent me) {
				startTimer();
			}

			public void mouseHover(MouseEvent me) {
				cancelTimer();
				addHoverHandles();
				hovering = true;
			}

			public void mouseMoved(MouseEvent me) {
			}
		};
		/**
		 * 被删除的时候清除监听
		 */
		removeListener = new LayoutListener() {
			@Override
			public void invalidate(IFigure container) {
			}

			@Override
			public boolean layout(IFigure container) {
				return false;
			}

			@Override
			public void postLayout(IFigure container) {
			}

			@Override
			public void remove(IFigure child) {
				if (child == figure) {
					clearHoverHandles();
					cancelTimer();
					figure.getParent().removeLayoutListener(removeListener);
				}
			}

			@Override
			public void setConstraint(IFigure child, Object constraint) {
			}
		};

		if (figure != null) {
			figure.addMouseMotionListener(hoverListener);
			if (figure.getParent() != null)
				figure.getParent().addLayoutListener(removeListener);
		}
	}

	/**
	 * Adds the handles to the handle layer.
	 */
	protected void addHoverHandles() {
		clearHoverHandles();
		handleLayer = getLayer(LayerConstants.HANDLE_LAYER);
		handles = createHoverHandles();
		for (int i = 0; i < handles.size(); i++) {
			IFigure handle = (IFigure) handles.get(i);
			handleLayer.add(handle);
			handle.addMouseMotionListener(handleListener);
		}
	}

	protected void clearHoverHandles() {
		if (handles == null)
			return;
		if (handleLayer == null)
			handleLayer = getLayer(LayerConstants.HANDLE_LAYER);
		for (int i = 0; i < handles.size(); i++) {
			IFigure handle = (IFigure) handles.get(i);
			try {
				handleLayer.remove(handle);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (handleListener != null)
				handle.removeMouseMotionListener(handleListener);
		}
		handles = null;
	}

	protected void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	protected void startTimer() {
		if (!hovering)
			return;

		if (timer != null) {
			timer.cancel();
		}

		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			public void run() {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						clearHoverHandles();
					}
				});
			}
		}, TIME_INTERVAL);
	}

	protected abstract List<Handle> createHoverHandles();
}