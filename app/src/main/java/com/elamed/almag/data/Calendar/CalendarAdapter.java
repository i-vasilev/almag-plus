package com.elamed.almag.data.Calendar;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elamed.almag.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        //holder.horizontalCalendar.setRange();
        holder.nameView.setText(calendar.getTimetable().getName());
        holder.imageView.setImageBitmap(ImageViaAssets(calendar.getTimetable().getImage()));
        holder.calendarView = calendar;

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onVariantClick(calendar);
                }
            });
        }
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
        Calendar calendarView;

        ViewHolder(View view) {
            super(view);

            /* start before 1 month from now */
            java.util.Calendar startDate = java.util.Calendar.getInstance();
            startDate.add(java.util.Calendar.MONTH, -1);
            /* end after 1 month from now */
            java.util.Calendar endDate = java.util.Calendar.getInstance();
            endDate.add(java.util.Calendar.MONTH, 1);

            nameView = view.findViewById(R.id.name);

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
                            for (Date d :
                                    calendarView.getDates()) {
                                java.util.Calendar c = java.util.Calendar.getInstance();
                                c.setTimeInMillis(d.getTime());


                                if (date.get(java.util.Calendar.DAY_OF_YEAR) == c.get(java.util.Calendar.DAY_OF_YEAR)) {
                                    events.add(new CalendarEvent(Color.rgb(255, 255, 255), "event"));
                                }
                            }

                            return events;
                        }
                    })
                    .build();

            horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                @Override
                public void onDateSelected(java.util.Calendar date, int position) {
                }

            });
        }
    }
}
