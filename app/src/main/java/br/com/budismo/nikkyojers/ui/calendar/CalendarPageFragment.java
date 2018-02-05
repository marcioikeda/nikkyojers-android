package br.com.budismo.nikkyojers.ui.calendar;

/**
 * Created by marcioikeda on 04/02/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Event;

import static br.com.budismo.nikkyojers.ui.calendar.EventDetailActivity.ARG_EVENT_KEY;

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

    private SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private CalendarAdapter mCalendarAdapter;
    private Calendar startMonth;
    private Calendar endMonth;

    private TextView mTvNoEvents;
    private ProgressBar mProgressBar;

    //Firebase Database
    private DatabaseReference mEventsDatabaseReference;
    private Query mQueryMonth;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mChildSingleEventListener;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMonth = Calendar.getInstance();
        startMonth.set(Calendar.YEAR, getArguments().getInt(ARG_YEAR));
        startMonth.set(Calendar.MONTH, getArguments().getInt(ARG_MONTH));
        startMonth.set(Calendar.DAY_OF_MONTH, 1);
        startMonth.set(Calendar.HOUR_OF_DAY, 0);
        startMonth.set(Calendar.MINUTE, 0);
        startMonth.set(Calendar.SECOND, 0);
        startMonth.set(Calendar.MILLISECOND, 0);
        Log.d("CALENDAR", "MONTH: " + getArguments().getInt(ARG_MONTH) + " startMonth: " + startMonth.getTimeInMillis());
        endMonth = Calendar.getInstance();
        endMonth.set(Calendar.YEAR, getArguments().getInt(ARG_YEAR));
        endMonth.set(Calendar.MONTH, getArguments().getInt(ARG_MONTH));
        endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        endMonth.set(Calendar.HOUR_OF_DAY, endMonth.getActualMaximum(Calendar.HOUR_OF_DAY));
        endMonth.set(Calendar.MINUTE, endMonth.getActualMaximum(Calendar.MINUTE));
        endMonth.set(Calendar.SECOND, endMonth.getActualMaximum(Calendar.SECOND));
        endMonth.set(Calendar.MILLISECOND, endMonth.getActualMaximum(Calendar.MILLISECOND));
        Log.d("CALENDAR", "MONTH: " + getArguments().getInt(ARG_MONTH) + " endMonth: " + endMonth.getTimeInMillis());
        mEventsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("events");
        mQueryMonth = mEventsDatabaseReference.orderByChild("startDate").startAt(startMonth.getTimeInMillis()).endAt(endMonth.getTimeInMillis());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_page, container, false);
        TextView tvMonth= rootView.findViewById(R.id.tv_calendar_month);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_calendar);
        mTvNoEvents = rootView.findViewById(R.id.tv_calendar_noevent);


        tvMonth.setText(sdfMonth.format(startMonth.getTime()));
        mCalendarAdapter = new CalendarAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(mCalendarAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        mCalendarAdapter.clear();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null && mChildSingleEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        mProgressBar.setVisibility(View.GONE);
                        mTvNoEvents.setVisibility(View.GONE);
                        event.key = dataSnapshot.getKey();
                        mCalendarAdapter.addNewEvent(event);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChildSingleEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        mProgressBar.setVisibility(View.GONE);
                        mTvNoEvents.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mQueryMonth.addChildEventListener(mChildEventListener);
            mQueryMonth.addListenerForSingleValueEvent(mChildSingleEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null && mQueryMonth != null) {
            mQueryMonth.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
        if (mChildSingleEventListener != null && mQueryMonth != null) {
            mQueryMonth.removeEventListener(mChildSingleEventListener);
            mChildSingleEventListener = null;
        }
    }

    @Override
    public void onEventClicked(String key) {
        //Toast.makeText(getActivity(), "Event: " + key, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(ARG_EVENT_KEY, key);
        getActivity().startActivity(intent);
    }
}
