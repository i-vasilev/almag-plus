package com.elamed.almag.data.Calendar;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.elamed.almag.R;
import com.elamed.almag.data.UpdaterData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Calendar> calendars;
    public AssetManager assetmanager;

    public interface onClickListener {
        void onVariantClick(Calendar calendar);
    }

    private onClickListener listener;

    public CalendarAdapter(Context context, List<Calendar> calendars) {
        this.inflater = LayoutInflater.from(context);
        this.calendars = calendars;
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_calendar, parent, false);
        Log.e("MENU IN ADAPTER TITLE", "someMess");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarAdapter.ViewHolder holder, int position) {
        final Calendar calendar = calendars.get(position);
        holder.nameView.setText(calendar.getTimetable().getName());
        holder.imageView.setImageBitmap(ImageViaAssets(calendar.getTimetable().getImage()));
        holder.calendarView = calendar;
        holder.position = position;
        fillChart(holder, position);
    }

    public void fillChart(CalendarAdapter.ViewHolder holder, int position) {
        final Calendar calendar = calendars.get(position);
        holder.lineChart.resetTracking();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int k = 0; k < 2; k++) {
            if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onVariantClick(calendar);
                    }
                });
            }
            ArrayList<Entry> values = new ArrayList<>();
            List<Integer> rates = new ArrayList<>();
            int color;
            String name = "Оценка до";
            if (k == 0) {
                rates = calendar.getRatesAfter();
                color = holder.colorAfter;
                name = "Оценка после";
            } else {
                rates = calendar.getRatesBefore();
                color = holder.colorBefore;
            }
            for (int i = 0; i < rates.size(); i++) {
                double val = rates.get(i);
                values.add(new Entry(TimeUnit.MILLISECONDS.toHours(calendar.dates.get(i).getTime()), (float) val));
            }

            LineDataSet d = new LineDataSet(values, name);
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);

        }
        LineData data = new LineData(dataSets);
        holder.lineChart.setData(data);
        holder.lineChart.invalidate();
        XAxis xAxis = holder.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    public Bitmap ImageViaAssets(String fileName) {

        InputStream is = null;
        try {

            is = assetmanager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return calendars.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        final HorizontalCalendar horizontalCalendar;
        final ImageView imageView;
        RatingBar ratingBarBefore;
        RatingBar ratingBarAfter;
        LineChart lineChart;
        Calendar calendarView;
        final int colorBefore;
        final int colorAfter;
        int position;
        int selectedPosition = -1;

        ViewHolder(View view) {
            super(view);
            /* start before 1 month from now */
            java.util.Calendar startDate = java.util.Calendar.getInstance();
            startDate.add(java.util.Calendar.MONTH, -1);
            /* end after 1 month from now */
            java.util.Calendar endDate = java.util.Calendar.getInstance();
            endDate.add(java.util.Calendar.MONTH, 1);

            lineChart = view.findViewById(R.id.lineChart);

            lineChart.setDrawGridBackground(false);
            lineChart.getDescription().setEnabled(false);
            lineChart.setDrawBorders(false);

            lineChart.getAxisLeft().setEnabled(false);
            lineChart.getAxisRight().setDrawAxisLine(false);
            lineChart.getAxisRight().setDrawGridLines(false);
            lineChart.getXAxis().setDrawAxisLine(false);
            lineChart.getXAxis().setDrawGridLines(false);

            // enable touch gestures
            lineChart.setTouchEnabled(true);

            // enable scaling and dragging
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            lineChart.setPinchZoom(false);


            ratingBarBefore = view.findViewById(R.id.ratingBarBefore);
            ratingBarBefore.setNumStars(5);
            ratingBarBefore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (selectedPosition != -1) {
                        UpdaterData.setRating(selectedPosition, (int) rating,
                                calendarView.getTimetable().getId(), false);
                        UpdaterData.selectAllDataFromDB();
                        CalendarAdapter.this.calendars = UpdaterData.calendars;
                        CalendarAdapter.this.fillChart(ViewHolder.this, position);
                    }
                }
            });

            ratingBarAfter = view.findViewById(R.id.ratingBarAfter);
            ratingBarAfter.setNumStars(5);
            ratingBarAfter.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (selectedPosition != -1) {
                        UpdaterData.setRating(selectedPosition, (int) rating,
                                calendarView.getTimetable().getId(), true);
                        UpdaterData.selectAllDataFromDB();
                        CalendarAdapter.this.calendars = UpdaterData.calendars;
                        CalendarAdapter.this.fillChart(ViewHolder.this, position);
                    }
                }
            });
            nameView = view.findViewById(R.id.name);

            colorBefore = view.getResources().getColor(R.color.rateBefore);
            colorAfter = view.getResources().getColor(R.color.mainColor);

            imageView = view.findViewById(R.id.imageMan);

            horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView2)
                    .range(startDate, endDate)
                    .datesNumberOnScreen(7)
                    .configure()
                    .formatTopText("MMM")
                    .formatMiddleText("dd")
                    .formatBottomText("EEE")
                    .showTopText(true)
                    .showBottomText(true)
                    .textColor(Color.LTGRAY, Color.WHITE)
                    .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                    .end()
                    .defaultSelectedDate(java.util.Calendar.getInstance())
                    .addEvents(new CalendarEventsPredicate() {
                        @Override
                        public List<CalendarEvent> events(java.util.Calendar date) {
                            List<CalendarEvent> events = new ArrayList<>();
                            int i = 0;
                            for (Date d :
                                    calendarView.getDates()) {
                                java.util.Calendar now = java.util.Calendar.getInstance();
                                java.util.Calendar c = java.util.Calendar.getInstance();
                                c.setTimeInMillis(d.getTime());
                                i++;


                                if (date.get(java.util.Calendar.DAY_OF_YEAR) == c.get(java.util.Calendar.DAY_OF_YEAR)) {
                                    events.add(new CalendarEvent(Color.rgb(255, 255, 255), "event"));
                                }
                                if (c.get(java.util.Calendar.DAY_OF_YEAR) == now.get(java.util.Calendar.DAY_OF_YEAR)) {
                                    selectedPosition = i - 1;
                                    ratingBarAfter.setRating(calendarView.ratesAfter.get(selectedPosition));
                                    ratingBarBefore.setRating(calendarView.ratesBefore.get(selectedPosition));
                                }
                            }

                            return events;
                        }
                    })
                    .build();

            horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                @Override
                public void onDateSelected(java.util.Calendar date, int position) {
                    boolean isSelectedFromList = false;
                    for (int i = 0; i < calendarView.getDates().size(); i++) {
                        java.util.Calendar c = java.util.Calendar.getInstance();
                        c.setTimeInMillis(calendarView.dates.get(i).getTime());

                        if (date.get(java.util.Calendar.DAY_OF_YEAR) == c.get(java.util.Calendar.DAY_OF_YEAR)) {
                            selectedPosition = i;
                            ratingBarAfter.setRating(calendarView.ratesAfter.get(i));
                            ratingBarBefore.setRating(calendarView.ratesBefore.get(i));
                            isSelectedFromList = true;
                            break;
                        }
                    }

                    selectedPosition = isSelectedFromList ? selectedPosition : -1;
                    ratingBarBefore.setEnabled(isSelectedFromList);
                    ratingBarAfter.setEnabled(isSelectedFromList);
                    if (!isSelectedFromList) {
                        ratingBarBefore.setRating(0);
                        ratingBarAfter.setRating(0);
                    }
                }
            });
        }
    }
}
