import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.neil.easycron.plugin.bo.JobRunningResult;
import com.neil.easycron.plugin.service.EasyJobService;

public class Test {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String path = "file:C:\\Users\\neilw\\Desktop\\sh-1.0-SNAPSHOT.jar";
        URL url1 = new URL(path);
        URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()
                                                                                     .getContextClassLoader());
        Class<?> serviceImpl = myClassLoader1.loadClass("com.neil.easycron.plugin.sh.ShServiceImpl");
        EasyJobService service = (EasyJobService) serviceImpl.newInstance();
        JobRunningResult result = service.serve();
        System.out.println(result.getRunningStatus().name());
        System.out.println(result.getMessage());
    }
}
