package ns.mobile.mtreportfrag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainInfoFragment extends Fragment {

    private Button btnNeeds, btnWants;
    private OnFilterSelectedListener filterListener;

    public interface OnFilterSelectedListener {
        void onFilterSelected(String category);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFilterSelectedListener) {
            filterListener = (OnFilterSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFilterSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_info, container, false);

        btnNeeds = view.findViewById(R.id.btNeeds);
        btnWants = view.findViewById(R.id.btWants);

        btnNeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterListener != null) {
                    filterListener.onFilterSelected("Need");
                }
            }
        });

        btnWants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterListener != null) {
                    filterListener.onFilterSelected("Want");
                }
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filterListener = null;
    }
}



