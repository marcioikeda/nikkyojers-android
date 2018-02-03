package br.com.budismo.nikkyojers.ui.calendar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.budismo.nikkyojers.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEventActivityFragment extends Fragment {

    public AddEventActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }
}
