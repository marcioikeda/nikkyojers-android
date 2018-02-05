package br.com.budismo.nikkyojers.ui.calendar;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Event;

/**
 * Created by marcioikeda on 04/02/18.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.EventViewHolder>{

    private List<Event> mEvents = new ArrayList<>();
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd\nEEE", Locale.getDefault());
    private SimpleDateFormat sdfTime = new SimpleDateFormat("h:mma", Locale.getDefault());
    //private SimpleDateFormat sdfTimeDate = new SimpleDateFormat("h:mma (dd)", Locale.US);
    private CalendarEventListener mListener;

    public interface CalendarEventListener {
        void onEventClicked(String key);
    }

    public CalendarAdapter(CalendarEventListener listener) {
        mListener = listener;
    }

    public void addNewEvent(Event event) {
        mEvents.add(event);
        notifyItemInserted(mEvents.size() - 1);
    }

    public void clear() {
        mEvents = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        holder.tvEventTitle.setText(event.title);

        Date startDate = new Date(event.startDate);
        Date endDate = new Date(event.endDate);
        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(startDate);
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(endDate);

        holder.tvEventStartDate.setText(sdfDate.format(startDate));
        //holder.tvEventEndDate.setText(sdfDate.format(endDate));

        if (startDateCal.get(Calendar.DAY_OF_MONTH) != endDateCal.get(Calendar.DAY_OF_MONTH)) {
            //holder.tvEventEndDate.setVisibility(View.GONE);
            holder.tvEventStartTime.setVisibility(View.GONE);
            holder.tvEventEndTime.setVisibility(View.GONE);
        }

        if (event.allDay) {
            holder.tvEventStartTime.setVisibility(View.GONE);
            holder.tvEventEndTime.setVisibility(View.GONE);
        } else {
            //holder.tvEventTime.setText(String.format(formatTimeToUse, sdfTimeToUse.format(new Date(event.startDate)), sdfTimeToUse.format(new Date(event.endDate)))) ;
            holder.tvEventStartTime.setText(sdfTime.format(new Date(event.startDate)));
            holder.tvEventEndTime.setText(sdfTime.format(new Date(event.endDate)));

        }
        holder.cardEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEventClicked(event.key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEventTitle;
        private TextView tvEventStartDate;
        private TextView tvEventStartTime;
        private TextView tvEventEndTime;
        private CardView cardEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvEventTitle = itemView.findViewById(R.id.tv_event_title);
            tvEventStartDate = itemView.findViewById(R.id.tv_event_start_date);
            tvEventStartTime = itemView.findViewById(R.id.tv_event_start_time);
            tvEventEndTime = itemView.findViewById(R.id.tv_event_end_time);
            cardEvent = itemView.findViewById(R.id.card_event);
        }
    }

}
