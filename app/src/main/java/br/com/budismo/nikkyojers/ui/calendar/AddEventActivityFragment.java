package br.com.budismo.nikkyojers.ui.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Event;
import br.com.budismo.nikkyojers.data.FirebaseDatabaseHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEventActivityFragment extends Fragment {

    private boolean mEnablePost = false;

    private Switch mSwitchAllDay;
    private TextView mStartDateView;
    private TextView mStartTimeView;
    private TextView mEndDateView;
    private TextView mEndTimeView;
    private EditText mEditTitleView;
    private EditText mEditLocationView;
    private EditText mEditDescriptionView;

    final SimpleDateFormat sdfDate = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault());
    final SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    private FirebaseDatabaseHelper firebaseDatabaseHelper;

    public AddEventActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_event, container, false);
        mSwitchAllDay = rootView.findViewById(R.id.switch_allday);
        mStartDateView = rootView.findViewById(R.id.tv_datestart);
        mStartTimeView = rootView.findViewById(R.id.tv_timestart);
        mEndDateView = rootView.findViewById(R.id.tv_dateend);
        mEndTimeView = rootView.findViewById(R.id.tv_timeend);
        mEditTitleView = rootView.findViewById(R.id.edit_event_title);
        mEditLocationView = rootView.findViewById(R.id.edit_location);
        mEditDescriptionView = rootView.findViewById(R.id.edit_event_description);

        Calendar today = Calendar.getInstance();
        final int todayDay = today.get(Calendar.DAY_OF_MONTH);
        final int todayMonth = today.get(Calendar.MONTH);
        final int todayYear = today.get(Calendar.YEAR);

        mSwitchAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)  {
                    mStartTimeView.setVisibility(View.INVISIBLE);
                    mEndTimeView.setVisibility(View.INVISIBLE);
                } else {
                    mStartTimeView.setVisibility(View.VISIBLE);
                    mEndTimeView.setVisibility(View.VISIBLE);
                }
            }
        });


        Date todayDate = new Date();

        mStartDateView.setText(sdfDate.format(todayDate));
        mStartDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dateSet = Calendar.getInstance();
                        dateSet.set(Calendar.YEAR, year);
                        dateSet.set(Calendar.MONTH, month);
                        dateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mStartDateView.setText(sdfDate.format(dateSet.getTime()));
                    }
                }, todayYear, todayMonth, todayDay).show();
            }
        });

        final Calendar nextHour = Calendar.getInstance();
        nextHour.add(Calendar.HOUR_OF_DAY, 1);
        nextHour.set(Calendar.MINUTE, 0);
        mStartTimeView.setText(sdfTime.format(nextHour.getTime()));
        mStartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(rootView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar timeSet = Calendar.getInstance();
                        timeSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        timeSet.set(Calendar.MINUTE, minute);
                        mStartTimeView.setText(sdfTime.format(timeSet.getTime()));
                    }
                }, nextHour.get(Calendar.HOUR_OF_DAY), nextHour.get(Calendar.MINUTE), false).show();
            }
        });

        mEndDateView.setText(sdfDate.format(todayDate));
        mEndDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dateSet = Calendar.getInstance();
                        dateSet.set(Calendar.YEAR, year);
                        dateSet.set(Calendar.MONTH, month);
                        dateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mEndDateView.setText(sdfDate.format(dateSet.getTime()));
                    }
                }, todayYear, todayMonth, todayDay).show();
            }
        });

        nextHour.add(Calendar.HOUR_OF_DAY, 1);
        mEndTimeView.setText(sdfTime.format(nextHour.getTime()));
        mEndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(rootView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar timeSet = Calendar.getInstance();
                        timeSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        timeSet.set(Calendar.MINUTE, minute);
                        mEndTimeView.setText(sdfTime.format(timeSet.getTime()));
                    }
                }, nextHour.get(Calendar.HOUR_OF_DAY), nextHour.get(Calendar.MINUTE), false).show();
            }
        });

        mEditTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mEnablePost = charSequence.toString().trim().length() > 0;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_event, menu);
        menu.findItem(R.id.action_publish).setEnabled(mEnablePost);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_publish) {
            Event event = getNewEventFromView();
            if (event != null && validateInput(event)) {
                createEvent(event);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private Event getNewEventFromView() {
        Event newEvent = new Event();
        newEvent.title = mEditTitleView.getText().toString();
        newEvent.description = mEditDescriptionView.getText().toString();
        newEvent.allDay = mSwitchAllDay.isChecked();
        newEvent.location = mEditLocationView.getText().toString();
        newEvent.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Calendar startDate = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        try {
            startDate.setTime(sdfDate.parse((String) mStartDateView.getText()));
            endDate.setTime(sdfDate.parse((String) mEndDateView.getText()));
            if (!newEvent.allDay) {
                startTime.setTime(sdfTime.parse((String) mStartTimeView.getText()));
                startDate.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
                startDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
                endTime.setTime(sdfTime.parse((String) mEndTimeView.getText()));
                endDate.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
                endDate.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
            }

            newEvent.startDate = startDate.getTimeInMillis();
            newEvent.endDate = endDate.getTimeInMillis();

        } catch (ParseException e) {
            Snackbar.make(mStartDateView, getString(R.string.addevent_date_error), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }

        return newEvent;
    }

    private boolean validateInput(Event event) {
        if (TextUtils.isEmpty(event.title)) {
            Snackbar.make(mEditTitleView, getString(R.string.addevent_title_error), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (event.startDate >= event.endDate){
            Snackbar.make(mStartDateView, getString(R.string.addevent_date_notbefore), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createEvent(Event event) {
        firebaseDatabaseHelper.createNewEvent(event, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getContext(), databaseError.getCode() + ":" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Posted!", Toast.LENGTH_LONG).show();
                }
                getActivity().finish();
            }
        });

    }

}
