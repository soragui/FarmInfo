package com.getpoint.farminfomanager.utils.file;

import android.os.Environment;
import android.util.Log;

import com.getpoint.farminfomanager.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DirectoryPath {

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedInputStream in = null;
        BufferedReader inBr = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            in = new BufferedInputStream(p.getInputStream());
            inBr = new BufferedReader(new InputStreamReader(in));


            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                Log.i("getSDCardPath", lineStr);
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    Log.e("getSDCardPath", "命令执行失败!");
                }
            }
        } catch (Exception e) {
            Log.e("getSDCardPath", e.toString());
            //return Environment.getExternalStorageDirectory().getPath();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inBr != null) {
                    inBr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    static public String getPath2() {
        String sdcard_path = null;
        String sd_default = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        Log.d("text", sd_default);
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("text", sdcard_path);
        return sdcard_path;
    }

    public static void initWorkDir() {

        String root;

        if (isExternalStorageAvailable())
            root = Environment.getExternalStorageDirectory().getPath();
        else
            root = getPath2();

        final File newDir = new File(root, "/ZCHK");
        if (newDir.exists()) {
            return;
        } else {
            final boolean result = newDir.mkdir();
            if (result) {
                return;
            } else {
                return;
            }
        }

    }

    public static String getFarmInfoPath() {
        String root;
        if (isExternalStorageAvailable())
            root = Environment.getExternalStorageDirectory().getPath();
        else
            root = getPath2();
        return (root + "/ZCHK/");
    }

    /**
     * Storage folder for Parameters
     */
    static public String getParametersPath() {
        return getFarmInfoPath() + "/Parameters/";
    }

    /**
     * Storage folder for mission files
     */
    static public String getWaypointsPath() {
        return getFarmInfoPath() + "/Waypoints/";
    }

    /**
     * Folder where telemetry log files are stored
     */
    static public File getTLogPath() {
        File f = new File(getFarmInfoPath() + "/Logs/");
        f.mkdirs();
        return f;
    }

    /**
     * After tlogs are uploaded they get moved to this directory
     */
    static public File getSentPath() {
        File f = new File(getTLogPath() + "/Sent/");
        f.mkdirs();
        return f;
    }

    /**
     * Storage folder for user map tiles
     */
    static public String getMapsPath() {
        return getFarmInfoPath() + "/Maps/";
    }

    /**
     * Storage folder for user camera description files
     */
    public static String getCameraInfoPath() {
        return getFarmInfoPath() + "/CameraInfo/";
    }

    /**
     * Storage folder for stacktraces
     */
    public static String getLogCatPath() {
        return getFarmInfoPath() + "/LogCat/";
    }

    /**
     * Storage folder for SRTM data
     */
    static public String getSrtmPath() {
        return getFarmInfoPath() + "/Srtm/";
    }

}
