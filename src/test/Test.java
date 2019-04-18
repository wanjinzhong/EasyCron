import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.neil.easycron.plugin.bo.JobRunningResult;
import com.neil.easycron.plugin.service.EasyJobService;

public class Test {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String path = "file:C:\\Users\\neilw\\.easycron\\plugins\\sh-1.0-SNAPSHOT.jar";
        URL url1 = new URL(path);
        URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()
                                                                                     .getContextClassLoader());
        Class<?> serviceImpl = myClassLoader1.loadClass("com.neil.easycron.plugin.sh.ShServiceImpl");
        EasyJobService service = (EasyJobService) serviceImpl.newInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("1", "2");
        map.put("a", "b");
        JobRunningResult result = service.serve(map);
        System.out.println(result.getRunningStatus().name());
        System.out.println(result.getMessage());
    }
}
