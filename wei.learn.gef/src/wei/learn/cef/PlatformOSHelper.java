package wei.learn.cef;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
 
 public class PlatformOSHelper extends OSHelper
 {
   protected void initJNIPathInternal()
   {        
     Bundle bundle = Platform.getBundle("wei.learn.gef");
     Path path = new Path("/os/win32/x86");
     URL url = FileLocator.find(bundle, path, null);
     try {
       String dir = new File(FileLocator.toFileURL(url).getFile()).getAbsolutePath();
					System.out.println("dir================"  + dir);
       System.setProperty("cef.path", dir);
    } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }
