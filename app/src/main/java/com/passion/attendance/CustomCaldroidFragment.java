package com.passion.attendance;

import android.os.AsyncTask;
import android.util.Log;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CustomCaldroidFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // Auto-generated method stub
        return new CustomCaldroidGridAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

    @Override
    public void refreshView() {
        // If2 month and year is not yet initialized, refreshView doesn't do
        // anything
        if (month == -1 || year == -1) {
            return;
        }

        refreshMonthTitleTextView();

        // Refresh the date grid views
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (final CaldroidGridAdapter adapter : datePagerAdapters) {
                    // Reset caldroid data
                    adapter.setCaldroidData(getCaldroidData());
                    Log.i("CALENDAR", "Data Loaded");

                    // Reset extra data
                    adapter.setExtraData(extraData);
                    Log.i("CALENDAR", "Extra Data Loaded");

                    // Update today variable
                    adapter.updateToday();
                    Log.i("CALENDAR", "Date Upated");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            Log.i("CALENDAR", "DataSet Change Notified");
                        }
                    });

                }
                return null;
            }
        }.execute();
    }
}
