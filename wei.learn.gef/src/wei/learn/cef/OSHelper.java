package wei.learn.cef;


import java.io.File;

import org.cef.CefApp;
import org.eclipse.core.runtime.FileLocator;

abstract class OSHelper {
	private static final OSHelper INSTANCE;
	private static boolean hasInitJNIPath;

	static {
		OSHelper instance = null;
		String className = OSHelper.class.getPackage().getName()
				+ ".PlatformOSHelper";

		try {
			instance = (OSHelper) Class.forName(className).newInstance();
		} catch (Exception arg5) {
			throw new RuntimeException(arg5);
		} finally {
			INSTANCE = instance;
		}

	}

	protected abstract void initJNIPathInternal();

	public static void initJNIPath() {
		if (!hasInitJNIPath) {
			hasInitJNIPath = true;

			try {
				String e = (new File(FileLocator.toFileURL(
						CefApp.class.getResource("/")).toURI()))
						.getAbsolutePath();
				System.setProperty(
						"java.class.path",
						System.getProperty("java.class.path")
								+ System.getProperty("path.separator") + e);
			} catch (Exception arg0) {
				arg0.printStackTrace();
			}

			INSTANCE.initJNIPathInternal();
		}

	}
}