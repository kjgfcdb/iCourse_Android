package buaa.icourse;
import android.os.Environment;

import java.io.File;

class FileUtils {
    private String path = Environment.getExternalStorageDirectory().getPath()+"/Download";

    FileUtils() {
        File file = new File(path);
        /**
         *如果文件夹不存在就创建
         */
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    File createFile(String FileName) {
        return new File(path, FileName);
    }
}
