package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_call);

        final Request currentRequest =  getIntent().getParcelableExtra("EXTRA_REQUEST");

        TextView header = (TextView) findViewById(R.id.request_header);
        String header_text = "Can you help " + currentRequest.getName() + "?";
        header.setText(header_text);

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(currentRequest.getName());

        Button requestAccepted = (Button) findViewById(R.id.accept_request);
        requestAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Requests").child(currentRequest.getKey());
                database.removeValue();
                onButtonShowPopupWindowClick(v);

                Intent intent = new Intent(v.getContext(), VolunteerActivity.class);
                startActivity(intent);
                try {
                    Thread.sleep(6000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Button nextTime = (Button) findViewById(R.id.next_time);
        nextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VolunteerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
