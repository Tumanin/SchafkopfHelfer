package com.applicatum.schafkopfhelfer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.adapters.UsersDynamicAdapter;
import com.applicatum.schafkopfhelfer.models.Player;
import com.applicatum.schafkopfhelfer.utils.PlayersList;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticsFragment extends Fragment {

    private static final String TAG = "StatisticsFragment";

    View mRootView;
    MainActivity activity;
    LineChartView chartView;
    private DynamicGridView gridView;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (MainActivity) getActivity();
        mRootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        chartView = (LineChartView) mRootView.findViewById(R.id.chart);
        gridView = (DynamicGridView) mRootView.findViewById(R.id.usersGrid);
        setChartLines();
        setGridView();
        return mRootView;
    }

    private void setChartLines(){

        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 0));
        values.add(new PointValue(1, 10));
        values.add(new PointValue(2, 10));
        values.add(new PointValue(3, 20));


        Line line = new Line(values).setColor(Color.BLUE).setCubic(false);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        List<PointValue> values2 = new ArrayList<PointValue>();
        values2.add(new PointValue(0, 0));
        values2.add(new PointValue(1, 0));
        values2.add(new PointValue(2, -10));
        values2.add(new PointValue(3, 10));


        Line line2 = new Line(values2).setColor(Color.RED).setCubic(false);
        lines.add(line2);

        List<PointValue> values3 = new ArrayList<PointValue>();
        values3.add(new PointValue(0, 10));
        values3.add(new PointValue(1, 20));
        values3.add(new PointValue(2, 10));
        values3.add(new PointValue(3, 0));


        Line line3 = new Line(values3).setColor(Color.GREEN).setCubic(false);
        lines.add(line3);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis();
        List<AxisValue> axisValues = new ArrayList<>();
        axisValues.add(new AxisValue(0));
        axisValues.add(new AxisValue(1));
        axisValues.add(new AxisValue(2));
        axisValues.add(new AxisValue(3));
        axisX.setValues(axisValues);

        Axis axisY = new Axis().setHasLines(true);
        /*
        if (hasAxesNames) {
            axisX.setName("Axis X");
            axisY.setName("Axis Y");
        }
        */
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chartView.setLineChartData(data);
    }

    private void setGridView(){

        List<Player> players = new ArrayList<>();
        players.addAll(PlayersList.getInstance().getList());

        UsersDynamicAdapter usersDynamicAdapter = new UsersDynamicAdapter(activity, players, 3);
        gridView.setAdapter(usersDynamicAdapter);
//        add callback to stop edit mode if needed
        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
        {
            @Override
            public void onActionDrop()
            {
                gridView.stopEditMode();
            }
        });
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(activity, "OnClick!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
