package com.getpoint.farminfomanager.fragment.Mission;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.StationPointMarker;
import com.getpoint.farminfomanager.entity.points.StationPoint;
import com.getpoint.farminfomanager.utils.adapters.IndexAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/22.
 */

public class BSPointFragment extends PointDetailFragment {

    private static final String TAG = "BSPoint";

    private TextView pointIndex;
    private Button getBSPoint;
    private Button delBSPoint;

    private List<String> pointNum = new ArrayList<>();
    private MissionItemProxy currBSP;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_frame;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final Context context = getActivity().getApplicationContext();


        pointIndex = (TextView) view.findViewById(R.id.WaypointIndex);
        pointIndex.setText(String.valueOf(missionProxy.getCurrentFrameNumber()));

        getBSPoint = (Button) view.findViewById(R.id.getPointBtn);
        getBSPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddNew()) {
                    StationPoint stationPoint = new StationPoint(
                            getNewCoord(), getAltitude());

                    /**
                     *   把当前点添加到任务中去
                     */
                    MissionItemProxy newItem = new MissionItemProxy(missionProxy, stationPoint);
                    missionProxy.addItem(newItem);

                    /**
                     *  在地图上产生当前点的标志
                     */
                    StationPointMarker pointMarker = (StationPointMarker) newItem.getMarker();
                    pointMarker.setMarkerNum(missionProxy.getOrder(newItem));
                    mapFragment.updateMarker(pointMarker);

                    updatePointNum(missionProxy);

                    setPointIndex(missionProxy.getCurrentBaseNumber());
                    updatePointNum(missionProxy);
                } else {
                    updateCurrentBSP();
                }
            }
        });

        delBSPoint = (Button) view.findViewById(R.id.delPointBtn);
        delBSPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currBSP != null) {
                    missionProxy.getBaseStationProxies().remove(currBSP);
                    updatePointNum(missionProxy);
                    setPointIndex(missionProxy.getCurrentBaseNumber());
                    mapFragment.updateInfoFromMission(missionProxy);
                    addNew = true;
                }

            }
        });

        final SpinnerSelfSelect mFPIndexSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerFPIndex);
        mFPIndexSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                setPointIndex(position + 1);

                if (position < missionProxy.getBaseStationProxies().size()) {
                    addNew = false;
                    currBSP = missionProxy.getBaseStationProxies().get(position);
                } else {
                    addNew = true;
                    currBSP = null;
                }

            }
        });

        /**
         *  设置 mFPIndexSel 的适配器
         */
        final IndexAdapter p = new IndexAdapter(getActivity(), pointNum);
        mFPIndexSel.setAdapter(p);

        return view;
    }

    /**
     * 更新适配器 list 的信息
     *
     * @param m 任务
     */
    public void updatePointNum(MissionProxy m) {

        pointNum.clear();

        int length = m.getBaseStationProxies().size() + 1;

        for (int i = 0; i < length; i++) {
            pointNum.add(String.valueOf(i + 1));
        }
    }

    /**
     * 更新当前点的信息
     */
    public void updateCurrentBSP() {

        currBSP.getPointInfo().getPosition().setAltitude(getAltitude());
        currBSP.getPointInfo().getPosition().setLatitude(gps.lat);
        currBSP.getPointInfo().getPosition().setLongitude(gps.lon);

        addNew = true;
    }

    public float getAltitude() {

        return gps.alt - appPref.getBaseStationHeight();

    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(index));
    }

    public void clearVar() {

        currBSP = null;
        pointNum.clear();
        setPointIndex(1);

    }

}
