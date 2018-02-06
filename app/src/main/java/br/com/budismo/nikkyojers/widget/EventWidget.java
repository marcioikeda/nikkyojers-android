package br.com.budismo.nikkyojers.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Event;
import br.com.budismo.nikkyojers.ui.calendar.EventDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class EventWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.event_widget);

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("events");
        Calendar now = Calendar.getInstance();
        Query query = mDatabaseReference.orderByChild("startDate").startAt(now.getTimeInMillis()).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Iterator<DataSnapshot> t = dataSnapshot.getChildren().iterator();
                    if (t.hasNext()) {
                        DataSnapshot data = t.next();
                        Event event = data.getValue(Event.class);
                        if (event != null) {
                            views.setTextViewText(R.id.widget_text, event.title);
                            SimpleDateFormat sdf = new SimpleDateFormat("d MMM", Locale.getDefault());
                            views.setTextViewText(R.id.widget_date, sdf.format(new Date(event.startDate)));
                            Intent intent = new Intent(context, EventDetailActivity.class);
                            intent.putExtra(EventDetailActivity.ARG_EVENT_KEY, data.getKey());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
                        }
                    }
                } else {
                    views.setTextViewText(R.id.widget_text, context.getString(R.string.calendar_noevent));
                }
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                views.setTextViewText(R.id.widget_text, databaseError.getMessage());
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

