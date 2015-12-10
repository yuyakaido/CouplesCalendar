package com.yuyakaido.android.couplescalendar.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.text.format.DateUtils;
import android.widget.Toast;
import com.yuyakaido.android.couplescalendar.model.CouplesCalendarEvent;
import com.yuyakaido.android.couplescalendar.model.Theme;
import com.yuyakaido.android.couplescalendar.ui.CouplesCalendarFragment;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        CouplesCalendarFragment.OnMonthChangeListener,
        CouplesCalendarFragment.OnDateClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CouplesCalendarFragment fragment = CouplesCalendarFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_fragment_container, fragment);
        transaction.commit();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fragment.setEvents(getDummyEvents());
            }
        });
    }

    @Override
    public void onMonthChange(Date month) {
        int flags = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_MONTH_DAY;
        setTitle(DateUtils.formatDateTime(this, month.getTime(), flags));
    }

    @Override
    public void onDateClick(Date date) {
        int flags = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        String str = DateUtils.formatDateTime(this, date.getTime(), flags);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private List<CouplesCalendarEvent> getDummyEvents() {
        List<CouplesCalendarEvent> couplesCalendarEvents = new ArrayList<>();

        SampleEvent sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-01T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-01T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.RED.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-02T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-02T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.ORANGE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-03T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-03T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.YELLOW.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-04T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-04T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.GREEN.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-05T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-05T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.TIFFANY_BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-08T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-08T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.LIGHT_BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-09T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-09T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-10T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-10T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.PURPLE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-11T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-11T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.PINK.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-06-12T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-06-12T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.NAVY_BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-04T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-04T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.RED.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-05T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-05T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.RED.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-05T01:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-05T02:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.ORANGE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-06T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-06T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.RED.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-06T01:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-06T02:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.ORANGE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-06T02:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-06T03:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.YELLOW.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-01T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-01T01:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.LIGHT_BLUE.getEventColorId()));
        sampleEvent.setRecurrenceRule("FREQ=WEEKLY");
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-02T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-02T01:00:00.000Z").toDate());
        sampleEvent.setRecurrenceRule("FREQ=WEEKLY;INTERVAL=2");
        sampleEvent.setEventColor(getResources().getColor(Theme.RED.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-10T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-12T00:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.LIGHT_BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-12T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-14T00:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.TIFFANY_BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-24T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-26T00:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.LIGHT_BLUE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-25T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-27T00:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.GREEN.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        sampleEvent = new SampleEvent();
        sampleEvent.setStartAt(new DateTime("2015-08-26T00:00:00.000Z").toDate());
        sampleEvent.setEndAt(new DateTime("2015-08-28T00:00:00.000Z").toDate());
        sampleEvent.setEventColor(getResources().getColor(Theme.ORANGE.getEventColorId()));
        couplesCalendarEvents.add(sampleEvent);

        return couplesCalendarEvents;
    }

}
