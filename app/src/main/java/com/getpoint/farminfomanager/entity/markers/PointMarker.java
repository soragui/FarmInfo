package com.getpoint.farminfomanager.entity.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.utils.MarkerWithText;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

/**
 * Created by Station on 2016/8/3.
 */
public abstract class PointMarker {

    private final MissionItemProxy mMarkerOrigin;

    public static PointMarker newInstance(MissionItemProxy origin) {

        PointMarker pointMarker;
        switch (origin.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                pointMarker = new FramePointMarker(origin);
                break;
            case BYPASSPOINT:
            case FORWAEDPOINT:
            case CLIMBPOINT:
                pointMarker = new DangerPointMarker(origin);
                break;
            default:
                return null;
        }

        return pointMarker;
    }

    public PointMarker(MissionItemProxy origin) {
        mMarkerOrigin = origin;
    }

    public float getAnchorU() {
        return 0.5f;
    }

    public float getAnchorV() {
        return 0.5f;
    }

    public boolean isDraggable() {
        return true;
    }

    public boolean isVisible() {
        return true;
    }

    public LatLong getPosition() {
        return mMarkerOrigin.getPointInfo().getPosition().getLatLong();
    }

    public void setPosition(LatLong coord) {
        LatLong coordinate = mMarkerOrigin.getPointInfo().getPosition().getLatLong();
        coordinate.setLatitude(coord.getLatitude());
        coordinate.setLongitude(coord.getLongitude());
    }

    public Bitmap getIcon(Resources res) {

        final MissionProxy missionProxy = mMarkerOrigin.getMission();

        return MarkerWithText.getMarkerWithTextAndDetail(getIconResource(),
                Integer.toString(missionProxy.getOrder(mMarkerOrigin)), null, res);
    }

    protected abstract int getIconResource();

}