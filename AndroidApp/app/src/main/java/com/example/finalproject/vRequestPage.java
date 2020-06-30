package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class vRequestPage extends AppCompatActivity {

    private static final String TAG = "vRequestPage";
    private Request request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_request_page);
        if (getIntent().hasExtra("EXTRA_REQUEST")) {
            request = getIntent().getParcelableExtra("EXTRA_REQUEST");
            setDetails(request);

            Log.d(TAG, "The request is " + request);
        }

        Button declineBtn = (Button) findViewById(R.id.decline);
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VolunteerActivity.class);
                startActivity(intent);
            }
        });

        Button acceptBtn = (Button) findViewById(R.id.accept);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone(request.getPhoneNumber());
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(v.getContext(), PostCallActivity.class);
                intent.putExtra("EXTRA_REQUEST", request);
                startActivity(intent);
            }
        });
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
    // Decide what we want to see here, in the request that opens when the user
    // presses in the notification
    // needs to lead to vDetailsPage when the user presses on accept

    private void setDetails(Request request) {
        TextView generalDetails = (TextView) findViewById(R.id.request_details);
        String generalDetails_text = "You have a request from " + request.getName();

        generalDetails.setText(generalDetails_text);
    }
}
