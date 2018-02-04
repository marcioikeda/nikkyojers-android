package br.com.budismo.nikkyojers.data;

import com.google.firebase.database.Exclude;

/**
 * Created by Marcio Ikeda on 03/02/2018.
 */

public class Event {

    public String uid;
    public String title;
    public String description;
    public long startDate;
    public long endDate;
    public boolean allDay;
    public String location;
    @Exclude
    public String key;

    public Event() {

    }

}
