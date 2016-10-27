package com.getpoint.farminfomanager.utils.adapters.pointdetailadapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ChildViewHolder;

/**
 * Created by Gui Zhou on 2016/10/26.
 */

public class PointInfoViewHolder extends ChildViewHolder {

    private TextView pointNum;
    private TextView pointLongitude;
    private TextView pointLatitude;
    private TextView pointHeight;

    public PointInfoViewHolder(@NonNull View itemView) {
        super(itemView);

        pointLongitude = (TextView) itemView.findViewById(R.id.pointLongitude);
        pointLatitude = (TextView) itemView.findViewById(R.id.pointLatitude);
        pointHeight = (TextView) itemView.findViewById(R.id.pointHeight);
    }

    public void bind(@NonNull PointInfo p) {

        pointLongitude.setText(String.valueOf(p.getPosition().getLatLong().getLongitude()));
        pointLatitude.setText(String.valueOf(p.getPosition().getLatLong().getLatitude()));
        pointHeight.setText(String.valueOf(p.getPosition().getAltitude()));

    }

}