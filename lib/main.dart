import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main(){
  runApp(MaterialApp(
    home: MyHome(),
  ));
}

class MyHome extends StatefulWidget {
  @override
  _MyHomeState createState() => _MyHomeState();
}

class _MyHomeState extends State<MyHome> {
  //获取到插件与原生的交互通道
  static const mNotificationBar = const MethodChannel('notification_bar.flutter.io/notificationBar');
  TextEditingController mControllera = TextEditingController();
  TextEditingController mControllerb = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text('发送通知'),),
        body: Padding(
          padding: EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment : CrossAxisAlignment.center,
            children: <Widget>[
              Text('F:/VscodeProject/flutter_demo_notification_bar'),
              SizedBox(height: 50,),
              TextField(
                controller: mControllera, //控制器，控制TextField文字   同 Android View id
                decoration: new InputDecoration(
                  labelText: 'ContentTitle',
                ),
                autofocus: false,
              ),

              TextField(
                controller: mControllerb, //控制器，控制TextField文字   同 Android View id
                decoration: new InputDecoration(
                  labelText: 'ContentText',
                ),
                autofocus: false,
              ),
              SizedBox(height: 50,),
              RaisedButton(
                child: Text('发送通知'),
                onPressed: () async {
                  String stra = mControllera.text.trim();
                  String strb = mControllerb.text.trim();
                  if(stra.isEmpty){
                    stra = '收到一条聊天消息';
                  }
                  if(strb.isEmpty){
                    strb = '晚上老地方不见不散！';
                  }
                  Map<String, String> map = { "contentTitle": stra ,"contentText": strb};
                  String result = await mNotificationBar.invokeMethod('content', map);
                  mControllera.text = '';
                  mControllerb.text = '';
                  print(result);

                },
              )
            ],
          ),
        )
    );
  }
}