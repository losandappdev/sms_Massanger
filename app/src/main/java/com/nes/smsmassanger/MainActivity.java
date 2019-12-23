package com.nes.smsmassanger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10;
    Button btnSend;
    EditText editText;
    String telnum = "4661221";
    TextView sendedMsg;
    String msg;
    RecyclerView messageRecycler;

    ArrayList messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = findViewById(R.id.send);
        editText = findViewById(R.id.edit_text);

        messageRecycler = findViewById(R.id.message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        DataAdapter dataAdapter = new DataAdapter(this,messages);
        messageRecycler.setAdapter(dataAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
                requestForSensSmsPermission();
            }
        });
    }

    void sendSms() {


        msg = editText.getText().toString();
        SmsManager.getDefault().sendTextMessage(telnum, null, msg, null, null);

        messages.add(msg);
        editText.clearComposingText();


    }


    public void requestForSensSmsPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            }
        }

    }
}
