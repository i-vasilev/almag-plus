package com.elamed.almag.data.Timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.elamed.almag.R;
import com.elamed.almag.data.UpdaterData;
import com.elamed.almag.view.DetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimetableAdapter extends ArrayAdapter<Timetable> {
    private LayoutInflater inflater;
    private int layout;
    private List<Timetable> timetables;
    public boolean isCheckedByPosition = false;
    private List<CheckBox> checkBoxes = new ArrayList<>();

    public TimetableAdapter(Context context, int layout, List<Timetable> timetables) {
        super(context, layout, timetables);
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
        this.timetables = timetables;
    }

    public void setChecked(int i) {
        for (int j = 0; j < checkBoxes.size(); j++) {
            if (i == j) {
                checkBoxes.get(j).setChecked(true);
            } else {
                checkBoxes.get(j).setChecked(false);
            }
        }
    }

    public void setChecked(CompoundButton checkBox) {
        if (!isCheckedByPosition) {
            for (int j = 0; j < checkBoxes.size(); j++) {
                if (checkBox == checkBoxes.get(j)) {
                    checkBoxes.get(j).setChecked(true);
                    DetailsActivity.position = j;
                } else {
                    checkBoxes.get(j).setChecked(false);
                }
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        checkBoxes.clear();
    }

    public View getView(int position, final View convertView, final ViewGroup parent) {
        final View view = inflater.inflate(this.layout, parent, false);

        final Timetable timetable = timetables.get(position);

        TextView nameView = view.findViewById(R.id.name);
        TextView durationOfProcedureView = view.findViewById(R.id.description);
        TextView timeView = view.findViewById(R.id.time);
        TextView durationOfTreatmentView = view.findViewById(R.id.dureationOfTreatment);
        CheckBox include = view.findViewById(R.id.include);
        checkBoxes.add(include);
        TextView interval = view.findViewById(R.id.frequency);
        include.setOnCheckedChangeListener((buttonView, isChecked) -> setChecked(buttonView));

        nameView.setText(timetable.getName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timetable.getTime());
        calendar = timetable.getRemindBefore().addTime(calendar);
        timeView.setText(getTimeString(calendar));
        durationOfProcedureView.setText("Напомнить за " + timetable.getRemindBefore().toString());
        durationOfTreatmentView.setText(String.format(view.getResources().getString(R.string.durationOfTreatment), timetable.getCountMadeProcedures(), timetable.getDurationOfTreatment()));
        interval.setText(String.format(view.getResources().getString(R.string.every_day), timetable.getInterval()));

        return view;
    }

    private String getTimeString(Calendar time) {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");

        return fmt.format(time.getTime());
    }
}
