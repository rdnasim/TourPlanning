package com.example.omarf.tourplanning.Fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.omarf.tourplanning.Adapter.EventAdapter;
import com.example.omarf.tourplanning.Dialog.AddEventDialog;
import com.example.omarf.tourplanning.EventDetailActivity;
import com.example.omarf.tourplanning.FirebaseHelper;
import com.example.omarf.tourplanning.Model.Event;
import com.example.omarf.tourplanning.R;

import com.example.omarf.tourplanning.databinding.FragmentEventListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {


    private static final String TAG = "event_fragment";

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  PlaceAutocompleteFragment fragment= (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getActivity(), place.getName().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {

            }
        });*/
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentEventListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_list, container, false);
        binding.addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddEventDialog dialog = new AddEventDialog();
                dialog.show(getActivity().getFragmentManager(), null);
            }
        });


        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseHelper.EVENT_REF).child(userId);
        EventAdapter adapter = new EventAdapter(reference);
        binding.eventListRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.eventListRecyclerView.setLayoutManager(manager);

        adapter.setmItemClickListener(new EventAdapter.ItemClickListener() {
            @Override
            public void onClickItem(final DatabaseReference reference) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Event event = dataSnapshot.getValue(Event.class);
                        String startDateString = event.getStartDate();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date startDate = dateFormat.parse(startDateString);
                            Date currentdate = new Date();

                            if (startDate.compareTo(currentdate) < 0) {
                                Log.i(TAG, "start date is smaller");
                                Intent intent =new Intent(getActivity(),EventDetailActivity.class);
                                intent.putExtra(EventDetailActivity.REF_TAG,reference.toString());
                                startActivity(intent);


                            }
                            else {
                                Log.i(TAG, "start date is bigger");
                                AddEventDialog editDialog=new AddEventDialog();
                                editDialog.setEvent(event,reference);
                                editDialog.show(getActivity().getFragmentManager(),null);

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return binding.getRoot();
    }

}
