package com.zhiyy;


import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;

import dji.common.mission.waypoint.Waypoint;

public class PositionUtil {

    private static double pi = 3.1415926535897932384626;
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;


    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     * */
    static LatLng gcj_To_Gps84(double lat, double lon) {
        LatLng gps = transform(lat, lon);
        double lontitude = lon * 2 - gps.longitude;
        double latitude = lat * 2 - gps.latitude;
        return new LatLng(latitude, lontitude);
    }

    private static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    private static LatLng transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new LatLng(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLng(mgLat, mgLon);
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    static LatLng coordinateTransform(LatLng sourceLatLng, Context context){
        CoordinateConverter converter  = new CoordinateConverter(context);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(sourceLatLng);
        // 执行转换操作
        return converter.convert();
    }

    static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public static double get3Ddistance(Waypoint waypoint1, Waypoint waypoint2){
        double distance3D;
        double distance2D = get2Ddistance(waypoint1,waypoint2);
        double altitude1,altitude2;
        double altitudeDifference;
        altitude1 = waypoint1.altitude;
        altitude2 = waypoint2.altitude;
        altitudeDifference = altitude1 - altitude2;
        distance3D = Math.sqrt(distance2D * distance2D +
                altitudeDifference * altitudeDifference);
        return distance3D;
    }

    public static double get2Ddistance(Waypoint waypoint1, Waypoint waypoint2){
        double distance;
        LatLng latLng1,latLng2;
        latLng1 = new LatLng(waypoint1.coordinate.getLatitude(),waypoint1.coordinate.getLongitude());
        latLng2 = new LatLng(waypoint2.coordinate.getLatitude(),waypoint2.coordinate.getLongitude());
        distance = Util.getDistance(latLng1,latLng2);

        return distance;
    }


}
