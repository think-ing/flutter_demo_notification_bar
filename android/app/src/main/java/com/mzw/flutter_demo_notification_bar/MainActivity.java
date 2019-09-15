package com.mzw.flutter_demo_notification_bar;


import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.lang.Integer;


public class MainActivity extends FlutterActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    new MethodChannel(getFlutterView(), "notification_bar.flutter.io/notificationBar").setMethodCallHandler(
            new MethodCallHandler() {
              @Override
              public void onMethodCall(MethodCall call, Result result) {
                // TODO
                if (call.method.equals("content")) {
                  //解析参数
                  String contentTitle = call.argument("contentTitle");
                  String contentText = call.argument("contentText");

                  sendChatMsg(contentTitle,contentText);
                  if (true) {
                    result.success("success");
                  } else {
                    result.error("error", "failure", null);
                  }
                } else {
                  result.notImplemented();
                }
              }
            }
    );
    initNotificationManager();
  }


  // =========================== 通知 =============================
  // 创建两个通知渠道  8.0新特性，不允许使用 NotificationCompat.Builder
  private void initNotificationManager(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      String channelId = "chat";
      String channelName = "聊天消息";
      int importance = NotificationManager.IMPORTANCE_HIGH;
      createNotificationChannel(channelId, channelName, importance);
    }
  }

  @TargetApi(Build.VERSION_CODES.O)
  private void createNotificationChannel(String channelId, String channelName, int importance) {
    NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
    channel.setShowBadge(true);//开启桌面角标
    channel.setBypassDnd(true);    //设置绕过免打扰模式
    channel.canBypassDnd();       //检测是否绕过免打扰模式
    channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);//设置在锁屏界面上显示这条通知

    //是否需要振动提示
    channel.enableVibration(true);
    //振动模式
    // channel.setVibrationPattern(new long[]{100, 200});

    //是否需要呼吸灯提示
    channel.enableLights(true);
    //呼吸灯颜色
    // channel.setLightColor(color.cyan);

    NotificationManager notificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE);
    notificationManager.createNotificationChannel(channel);
  }
  public void sendChatMsg(String contentTitle,String contentText) {
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    //检查权限是否开启
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = manager.getNotificationChannel("chat");
      if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
        startActivity(intent);
        Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
      }
    }

    Notification notification = new NotificationCompat.Builder(this, "chat")
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
            .setAutoCancel(true)
            .setNumber(19)//角标显示数字
            .build();
    manager.notify(1, notification);
  }
}
