package com.nes.smsmassanger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10;
    Button btnSend;
    String telnum = "4661221";
    TextView sendedMsg;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = findViewById(R.id.send);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForSensSmsPermission();
            }
        });
    }

    void sendSms() {
        final EditText editText = findViewById(R.id.edit_text);
        TextView sendedMsg = findViewById(R.id.msg_send);


        msg = editText.getText().toString();
        SmsManager.getDefault().sendTextMessage(telnum, null, msg, null, null);

        sendedMsg.setText(msg);
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
