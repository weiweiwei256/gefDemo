package wei.learn.cef;



import org.eclipse.core.runtime.Platform;

import wei.learn.gef.Activator;

public class PlatformUtil
{
  public static final PlatformOs platform;
  private static boolean IS_WINDOWS_XP = false;

  static
  {
    String env = System.getProperty("os.name").toLowerCase();
    PlatformOs tempPlatform;
    if (Activator.getDefault() != null)
    {
      String os = Platform.getOS();
      if (os.equals("win32"))
      {
        tempPlatform = PlatformOs.WINDOWS;
      }
      else if (os.equals("macosx"))
      {
        tempPlatform = PlatformOs.MAC;
      }
      else
      {
        tempPlatform = PlatformOs.LINUX;
      }
      if ("windows xp".equals(env)) {
        IS_WINDOWS_XP = true;
      }

    }
    else
    {
      if (env.indexOf("win") != -1)
      {
        tempPlatform = PlatformOs.WINDOWS;
      }
      else if (env.startsWith("mac os"))
      {
        tempPlatform = PlatformOs.MAC;
      }
      else
      {
        tempPlatform = PlatformOs.LINUX;
      }
      if ("windows xp".equals(env)) {
        IS_WINDOWS_XP = true;
      }
    }
    platform = tempPlatform;
  }

  public static boolean isWindows()
  {
    return (platform == PlatformOs.WINDOWS);
  }

  public static boolean isWindowsXP() {
    return IS_WINDOWS_XP;
  }

  public static boolean isMac() {
    return (platform == PlatformOs.MAC);
  }

  public static double getDPIScale()
  {
    String dpi = System.getProperty("dpi-scale");
    if (dpi != null)
      try {
        return Double.parseDouble(dpi);
      }
      catch (Exception localException) {
      }
    return 1.0D;
  }

  public static enum PlatformOs
  {
    WINDOWS, MAC, LINUX;
  }
}