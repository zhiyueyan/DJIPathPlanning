package com.zhiyy;


import android.content.Context;
import android.os.Environment;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    static String format7f(double a) {
        return String.format("%.7f", a);
    }

    static String format2f(double a) {
        return String.format("%.2f", a);
    }

    static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    static void saveInfo(String str) {
        //File sdCardDir = Environment.getExternalStorageDirectory().getPath();//获取SDCard目录
        File file = new File(Environment.getExternalStorageDirectory(), "info.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(str.getBytes());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String nulltoIntegerDefalt(String value) {
        if (!isIntValue(value)) value = "0";
        return value;
    }

    private static boolean isIntValue(String val) {
        try {
            val = val.replace(" ", "");
            Integer.parseInt(val);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    static String getDirection(float heading) {
        if (heading == 0) {
            return "North";
        } else if (heading > 0 && heading < 90) {
            return "North East  A: " + format2f(heading);
        } else if (heading == 90) {
            return "East";
        } else if (heading > 90 && heading < 180) {
            return "South East  A: " + format2f(180 - heading) ;
        } else if (heading > -90 && heading < 0) {
            return "North West  A: " + format2f(-heading) ;
        } else if (heading > -180 && heading < -90) {
            return "South West  A: " + format2f(heading + 180) ;
        } else if (heading == -90) {
            return "West";
        } else {
            return "South";
        }
    }

    static float getRotateAngle(float heading) {
        if (heading >= -180 && heading <= 0) {
            return -heading;
        } else {
            return 180 + 180 - heading;
        }
    }

    static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float getDistance(LatLng latLng1, LatLng latLng2) {
        return AMapUtils.calculateLineDistance(latLng1, latLng2);
    }


    static void uploadServer(String information, String ip, int port) {
        Socket socket;
        try {
            //创建Socket
            socket = new Socket(ip, port);
            //向服务器端发送消息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(information);
            //关闭流
            bw.close();
            //关闭Socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
