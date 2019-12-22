package com.nes.smsmassanger;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.core.app.NotificationCompat;

public class SmsReceiver extends BroadcastReceiver {

    int messageId = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
// Минимальные проверки
        if (intent == null || intent.getAction() == null) return;

        Bundle bundle = intent.getExtras();
// Получаем сообщения
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
            }
            else
            {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
        }
        String smsFromPhone = messages[0].getDisplayOriginatingAddress();
        StringBuilder body = new StringBuilder();
        for (int i = 1; i < messages.length; i++) {
            body.append(messages[i].getMessageBody());
        }
        String bodyText = body.toString();
        makeNote(context, smsFromPhone, bodyText);

    }

    // Вывод уведомления в строке состояния
    private void makeNote(Context context, String addressFrom, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(String.format("Receiver:Sms [%s]", addressFrom))
                .setContentText(message);
        Intent resultIntent = new Intent(context, SmsReceiver.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

}