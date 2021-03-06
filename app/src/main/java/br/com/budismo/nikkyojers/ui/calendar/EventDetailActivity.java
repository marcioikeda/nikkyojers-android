package br.com.budismo.nikkyojers.ui.calendar;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Event;
import br.com.budismo.nikkyojers.data.FirebaseDatabaseHelper;
import br.com.budismo.nikkyojers.data.User;
import br.com.budismo.nikkyojers.util.Util;

public class EventDetailActivity extends AppCompatActivity {

    public static final String ARG_EVENT_KEY = "param_event_key";
    private String mEventKey;

    //Firebase
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mEventListener;

    //Views
    private TextView mTvDate;
    private TextView mTvTitle;
    private TextView mTvUserOwner;
    private TextView mTvDescription;
    private TextView mTvStartDate;
    private TextView mTvEndDate;
    private TextView mTvLocation;
    private ImageView mIvLocation;

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("MMM\ndd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEventKey = getIntent().getStringExtra(ARG_EVENT_KEY);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("events").child(mEventKey);
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        mTvDate= findViewById(R.id.tv_event_date);
        mTvTitle= findViewById(R.id.tv_event_title);
        mTvStartDate= findViewById(R.id.tv_event_start_date);
        mTvEndDate= findViewById(R.id.tv_event_end_date);
        mTvLocation= findViewById(R.id.tv_location);
        mTvDescription= findViewById(R.id.tv_event_description);
        mTvUserOwner = findViewById(R.id.tv_user_owner);
        mIvLocation = findViewById(R.id.iv_location);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Util.isConnected(this)) {
            Snackbar.make(mTvDate, getString(R.string.snack_disconnected), Snackbar.LENGTH_SHORT).show();
        }
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mEventListener == null) {
            mEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(EventDetailActivity.this, getString(R.string.eventdetail_notfound), Toast.LENGTH_SHORT).show();
                    } else {
                        Event event = dataSnapshot.getValue(Event.class);
                        if (event != null) {
                            mTvTitle.setText(event.title);
                            mTvDescription.setText(event.description);
                            if (!TextUtils.isEmpty(event.location)) {
                                mTvLocation.setText(event.location);
                            } else {
                                mIvLocation.setVisibility(View.INVISIBLE);
                            }

                            if (event.uid != null) {
                                mUserDatabaseReference.child(event.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user != null) {
                                            mTvUserOwner.setText(String.format(getString(R.string.event_owner), user.name));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }


                            Calendar startDate = Calendar.getInstance();
                            Calendar endDate = Calendar.getInstance();
                            startDate.setTimeInMillis(event.startDate);
                            endDate.setTimeInMillis(event.endDate);

                            mTvDate.setText(sdfDate.format(startDate.getTime()));
                            mTvStartDate.setText(DateFormat.getDateTimeInstance().format(startDate.getTime()));
                            mTvEndDate.setText(DateFormat.getDateTimeInstance().format(endDate.getTime()));
                        } else {
                            Toast.makeText(EventDetailActivity.this, getString(R.string.eventdetail_notfound), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabaseReference.addValueEventListener(mEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mEventListener != null) {
            mDatabaseReference.removeEventListener(mEventListener);
            mEventListener = null;
        }
    }

}
