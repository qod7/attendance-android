package com.passion.attendance;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.passion.attendance.Models.Attendance;
import com.passion.attendance.Models.Event;
import com.passion.attendance.Models.Message;
import com.passion.attendance.Models.Staff;

import org.inf.nepalicalendar.NepaliCalendar;
import org.inf.nepalicalendar.NepaliDate;
import org.inf.nepalicalendar.NepaliDateException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    CalendarFragment mCalendarView;
    View mCalendarContainer;
    int mChildCount, mCurrentChild = -1;
    ViewGroup mOverViewView;
    StaticListView mEventListView;
    ScrollView mOverViewScrollView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    DatabaseHandler mDatabaseHandler;

    Integer mSelectedDay, mSelectedMonth, mSelectedYear;
    private View HeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mEventListView = (StaticListView) findViewById(R.id.events_list);

        mOverViewView = (ViewGroup) findViewById(R.id.overview_container);
        mOverViewScrollView = (ScrollView) findViewById(R.id.overview_scrollview);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        List<Event> fileList = new ArrayList<>();

        // for (int i = 0; i < 10; i++) fileList.add(new Event());

        EventListAdapter e = new EventListAdapter(this, fileList);

        mEventListView.setAdapter(e);

        mChildCount = mOverViewView.getChildCount();
        mCurrentChild = 0;

        mCalendarView = new CalendarFragment();
        mCalendarContainer = findViewById(R.id.calendar_container);

        Calendar today = Calendar.getInstance();

        mSelectedDay = today.get(Calendar.DAY_OF_MONTH);
        mSelectedMonth = today.get(Calendar.MONTH);
        mSelectedYear = today.get(Calendar.YEAR);
        InitializeCalendar();

        mEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("LISTVIEW", "Item " + i + " pressed");
                Toast.makeText(OverviewActivity.this, "Item " + i, Toast.LENGTH_SHORT).show();
            }
        });

        mEventListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        // Allow ScrollView to intercept touch events.
                        mOverViewScrollView.requestDisallowInterceptTouchEvent(false);
                        return false;
//                        break;

                    default:
                        // Disallow ScrollView to intercept touch events.
                        mOverViewScrollView.requestDisallowInterceptTouchEvent(true);
                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
//                        break;

                }


            }
        });

        mCalendarView.setCalendarListener(new CalendarListener() {
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

                mSelectedMonth = Selected.get(Calendar.MONTH);
                mSelectedYear = Selected.get(Calendar.YEAR);
                mSelectedDay = Selected.get(Calendar.DAY_OF_MONTH);

                Integer[] tempDate = {mSelectedYear, mSelectedMonth, mSelectedDay};

                try {
                    NepaliDate selectedNepaliDate = NepaliCalendar.convertGregorianToNepaliDate(date),
                            currentNepaliDate = mCalendarView.getCalendarAdapter().getSelectedNepaliDate();
                    if (selectedNepaliDate.getMonthNumber() == currentNepaliDate.getMonthNumber()) {
                        int y = (int) mCalendarContainer.getY();
                        mOverViewScrollView.smoothScrollTo(0, y);
                        Log.i("SCROLL", "Scrolled to " + y);
                    }
                } catch (NepaliDateException e1) {
                    e1.printStackTrace();
                }

                Toast.makeText(OverviewActivity.this, "Date Selected", Toast.LENGTH_SHORT).show();

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the layout
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        loadData();
    }

    private void loadData() {
        if (mDatabaseHandler == null)
            mDatabaseHandler = new DatabaseHandler(this);

        mSwipeRefreshLayout.setRefreshing(true);

        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        Staff staff = mDatabaseHandler.retrieveStaff();
        if (staff.getId() == -1)
            headers.add("get_staff");
        else {

            // Update the list of events
            headers.add("get_events");
            ArrayList<Event> Events = mDatabaseHandler.retrieveEvents();
            if (Events.isEmpty())
                values.add("-1");
            else
                values.add(String.valueOf(Events.get(Events.size() - 1).getId()));

            // Update the list of messages
            headers.add("get_messages");
            ArrayList<Message> Messages = mDatabaseHandler.retrieveMessages();
            if (Messages.isEmpty())
                values.add("-1");
            else
                values.add(String.valueOf(Messages.get(Messages.size() - 1).getId()));

            // Update the list of messages
            headers.add("get_attendances");
            ArrayList<Attendance> Attendances = mDatabaseHandler.retrieveAttendances();
            if (Attendances.isEmpty())
                values.add("-1");
            else
                values.add(String.valueOf(Attendances.get(Attendances.size() - 1).getId()));

            // TODO Insert code for retrieving shifts
            /*
                Some code here
             */


        }

    }

    private void InitializeCalendar() {
//        Bundle args = new Bundle();
//        args.putInt(CalendarFragment.START_DAY, 1);
//        args.putInt(CalendarFragment.START_MONTH, 1);
//        args.putInt(CalendarFragment.START_YEAR, 2072);
//        args.putBoolean(CalendarFragment.START_WITH_NEPALI_DATE, true);

//        mCalendarView.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_container, mCalendarView);
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
//        Boolean ScrollCondition = (mCurrentChild == 0 && direction != 1) ||
//                (mCurrentChild == mChildCount - 1 && direction != -1) || (mCurrentChild > 0 && mCurrentChild < mChildCount - 1);
//
//        if (ScrollCondition && !animating) {
//            animating = true;
//            mCurrentChild -= direction;
//            View PreviousChildView;
//            if (direction == -1) {
//                PreviousChildView = mOverViewView.getChildAt(mCurrentChild - 1);
//            } else {
//                PreviousChildView = mOverViewView.getChildAt(mCurrentChild);
//            }
//
//            final float oldY = mOverViewView.getY(),
//                    newY = oldY + PreviousChildView.getHeight() * direction;
//
//            String dir = direction == -1?"UP":"DOWN";
//
//            Log.i("Animation Values", "Child Height: " + PreviousChildView.getHeight());
//            Log.i("Animation Values", "oldY = " + oldY + ", newY = " + newY + ", direction = " + dir);
//
//            ObjectAnimator animator = ObjectAnimator.ofFloat(mOverViewView, "y", oldY, newY);
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
