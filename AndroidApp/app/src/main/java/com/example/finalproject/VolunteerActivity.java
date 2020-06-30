package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Iterator;

public class VolunteerActivity extends AppCompatActivity implements MyAdapter.OnRequestListener {
    private static final String TAG = "VolunteerActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Request> myDataset;

    public VolunteerActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        myDataset = new ArrayList<Request>();
        subscribeToTopic();
        getDatabaseEntries();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new MyAdapter(VolunteerActivity.this, myDataset, this);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Subscribe the volunteer to the "requests" topic such that he or she
     * will receive notification when the elderly opens a request
     */
    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("requests")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                    }
                });
    }

    private void getDatabaseEntries() {
        // Get a reference to our requests
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference ref = database.getReference("/Requests"); // Old
        DatabaseReference ref = database.getReference().child("Requests"); // New

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.hasChildren()) {
                    createEntryAndEnterToArray(dataSnapshot);
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey){
                Log.d(TAG, "onChildChanged: Called with" + dataSnapshot.getValue());
                Request changedRequest = dataSnapshot.getValue(Request.class);
              
                updateRequest(changedRequest);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Request removedRequest = (Request) dataSnapshot.getValue(Request.class);
                Log.d(TAG, "The requested to delete is " + removedRequest);
                if (removeFromDataset(removedRequest) == 1) {
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void createEntryAndEnterToArray(DataSnapshot ds) {
        Request newRequest = ds.getValue(Request.class);
        myDataset.add(newRequest);
    }

    /**
     * Returns 1 if succeeded, 0 otherwise
     */
    public int removeFromDataset(Request toRemove) {
        Iterator<Request> itr = myDataset.iterator();
        while (itr.hasNext()) {
            Request request = itr.next();

            if (request.compareTo(toRemove)) {
                itr.remove();
                return 1;
            }
        }
        return 0;
    }

    private void updateRequest(Request toUpdate) {
        Iterator<Request> itr = myDataset.iterator();
        while (itr.hasNext()) {
            Request request = itr.next();

            if (request.compareTo(toUpdate)) { 
                Log.d(TAG, "updateRequest: Found request to change");
                request.setName(toUpdate.getName());
                request.setDomain(toUpdate.getDomain());
                request.setPhoneNumber(toUpdate.getPhoneNumber());
                break;
            }
        }
    }

    @Override
    public void OnRequestClick(int position) {
        Log.d(TAG, "OnRequestClick: clicked with position " + position);
        Intent intent = new Intent(this, vRequestPage.class);
        intent.putExtra("EXTRA_REQUEST", myDataset.get(position));
        startActivity(intent);
    }
}
