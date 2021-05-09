package ua.kpi.comsys.IV8109.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ua.kpi.comsys.IV8109.R;

import android.widget.CompoundButton;
import android.widget.Switch;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final Switch switch1 = (Switch) root.findViewById(R.id.switch1);
        switch1.setChecked(false);
        switch1.setText("Графік");

       View graph = root.findViewById(R.id.line);
       View chart = root.findViewById(R.id.pie);
       displayGraph(graph, chart);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch1.setText("Діаграма");
                    displayChart(chart, graph);
                } else {
                    switch1.setText("Графік");
                    displayGraph(graph, chart);

                }
            }
        });

        return root;
    }

    public void displayChart(View visible, View invisible) {
        visible.setVisibility(View.VISIBLE);
        invisible.setVisibility(View.INVISIBLE);
    }

    public void displayGraph(View visible, View invisible) {
        visible.setVisibility(View.VISIBLE);
        invisible.setVisibility(View.INVISIBLE);
    }
}