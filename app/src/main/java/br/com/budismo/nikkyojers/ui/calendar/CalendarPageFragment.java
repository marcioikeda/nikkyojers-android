package br.com.budismo.nikkyojers.ui.calendar;

/**
 * Created by marcioikeda on 04/02/18.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.budismo.nikkyojers.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CalendarPageFragment extends Fragment implements CalendarAdapter.CalendarEventListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";

    private SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
    private ProgressBar mProgressBar;
    private CalendarAdapter mCalendarAdapter;

    public CalendarPageFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CalendarPageFragment newInstance(int year, int month) {
        CalendarPageFragment fragment = new CalendarPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }

    public static CalendarPageFragment newInstance(int position) {
        CalendarPageFragment fragment = new CalendarPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MONTH, position);
        Calendar calendar = Calendar.getInstance();
        args.putInt(ARG_YEAR, calendar.get(Calendar.YEAR));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_page, container, false);
        TextView tvMonth= rootView.findViewById(R.id.tv_calendar_month);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_calendar);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getArguments().getInt(ARG_YEAR));
        calendar.set(Calendar.MONTH, getArguments().getInt(ARG_MONTH));
        tvMonth.setText(sdfMonth.format(calendar.getTime()));

        mCalendarAdapter = new CalendarAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(mCalendarAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        //detachDatabaseReadListener();
        mCalendarAdapter.clear();
    }

    @Override
    public void onEventClicked(String key) {

    }
}
