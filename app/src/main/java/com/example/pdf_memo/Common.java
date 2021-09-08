package com.example.pdf_memo;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Common {


    public static String getAppPath(Context context)
    {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator
        );

        if (!dir.exists())
            dir.mkdir();
        return dir.getPath() + File.separator ;

    }

}
