package com.passion.attendance;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.passion.attendance.Models.Event;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    CalendarFragment CalendarView;
    View CalendarContainer;
    int ChildCount, CurrentChild = -1;
    ViewGroup OverViewView;
    StaticListView EventListView;
    ScrollView OverViewScrollView;

    Integer SelectedDay, SelectedMonth, SelectedYear;
    private View HeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        EventListView = (StaticListView) findViewById(R.id.events_list);

        OverViewView = (ViewGroup) findViewById(R.id.overview_container);
        OverViewScrollView = (ScrollView) findViewById(R.id.overview_scrollview);

        List<Event> fileList = new ArrayList<>();

        // for (int i = 0; i < 10; i++) fileList.add(new Event());

        EventListAdapter e = new EventListAdapter(this, fileList);

        EventListView.setAdapter(e);

        ChildCount = OverViewView.getChildCount();
        CurrentChild = 0;

        CalendarView = new CalendarFragment();
        CalendarContainer = findViewById(R.id.calendar_container);

        Calendar today = Calendar.getInstance();

        SelectedDay = today.get(Calendar.DAY_OF_MONTH);
        SelectedMonth = today.get(Calendar.MONTH);
        SelectedYear = today.get(Calendar.YEAR);
        InitializeCalendar();

        EventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("LISTVIEW", "Item " + i + " pressed");
                Toast.makeText(OverviewActivity.this, "Item " + i, Toast.LENGTH_SHORT).show();
            }
        });

        EventListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        // Allow ScrollView to intercept touch events.
                        OverViewScrollView.requestDisallowInterceptTouchEvent(false);
                        return false;
//                        break;

                    default:
                        // Disallow ScrollView to intercept touch events.
                        OverViewScrollView.requestDisallowInterceptTouchEvent(true);
                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
//                        break;

                }


            }
        });

        CalendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onViewCreated() {
                Toast.makeText(OverviewActivity.this, "View Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                Toast.makeText(OverviewActivity.this, "Month Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectDate(Date date, View view) {
                Calendar Selected = Calendar.getInstance();
                Selected.setTime(date);

                SelectedMonth = Selected.get(Calendar.MONTH);
                SelectedYear = Selected.get(Calendar.YEAR);
                SelectedDay = Selected.get(Calendar.DAY_OF_MONTH);

                Integer[] tempDate = {SelectedYear, SelectedMonth, SelectedDay};

                try {
                    NepaliDate selectedNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(date),
                            currentNepaliDate = CalendarView.getCalendarAdapter().getSelectedNepaliDate();
                    if (selectedNepaliDate.getMonthNumber() == currentNepaliDate.getMonthNumber()) {
                        int y = (int) CalendarContainer.getY();
                        OverViewScrollView.smoothScrollTo(0, y);
                        Log.i("SCROLL", "Scrolled to " + y);
                    }
                } catch (NepaliDateException e1) {
                    e1.printStackTrace();
                }

                Toast.makeText(OverviewActivity.this, "Date Selected", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void InitializeCalendar() {
//        Bundle args = new Bundle();
//        args.putInt(CalendarFragment.START_DAY, 1);
//        args.putInt(CalendarFragment.START_MONTH, 1);
//        args.putInt(CalendarFragment.START_YEAR, 2072);
//        args.putBoolean(CalendarFragment.START_WITH_NEPALI_DATE, true);

//        CalendarView.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_container, CalendarView);
        t.commit();
    }

//    @Override
//    public void onClick(View view) {
//        Toast.makeText(this, view.toString(), Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
////        this.mGestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }

//    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//        private static final String DEBUG_TAG = "Gestures";
//
//        @Override
//        public boolean onFling(MotionEvent event1, MotionEvent event2,
//                               float velocityX, float velocityY) {
//            Log.d(DEBUG_TAG, "onFling: " + velocityY);
//            Scroll(velocityY);
//            return true;
//        }
//    }
//
//    Boolean animating = false;
//
//    private void Scroll(float direction) {
//        direction = (int) Math.signum(direction);
//
//        Boolean ScrollCondition = (CurrentChild == 0 && direction != 1) ||
//                (CurrentChild == ChildCount - 1 && direction != -1) || (CurrentChild > 0 && CurrentChild < ChildCount - 1);
//
//        if (ScrollCondition && !animating) {
//            animating = true;
//            CurrentChild -= direction;
//            View PreviousChildView;
//            if (direction == -1) {
//                PreviousChildView = OverViewView.getChildAt(CurrentChild - 1);
//            } else {
//                PreviousChildView = OverViewView.getChildAt(CurrentChild);
//            }
//
//            final float oldY = OverViewView.getY(),
//                    newY = oldY + PreviousChildView.getHeight() * direction;
//
//            String dir = direction == -1?"UP":"DOWN";
//
//            Log.i("Animation Values", "Child Height: " + PreviousChildView.getHeight());
//            Log.i("Animation Values", "oldY = " + oldY + ", newY = " + newY + ", direction = " + dir);
//
//            ObjectAnimator animator = ObjectAnimator.ofFloat(OverViewView, "y", oldY, newY);
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    animating = false;
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//                }
//            });
//            animator.setDuration((long) Math.abs(oldY - newY));
//            animator.start();
//        }
//    }
}
