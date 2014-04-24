package cn.robotium.traffictest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MyService extends Service{
	
	private boolean onlyMobile = false;
	
	private Timer timer;
	
	/**
	 * 计时器每隔1分钟一次
	 */
	private static long period = 60000;
	
	/**
	 * 首次1秒后执行
	 */
	
	private static long delay = 1000;
	
	@Override
	public void onCreate() {
		super.onCreate();
		timer = new Timer();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			}
		}, delay, period); 
	}
	 

	private Handler	myHandler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int msgId = msg.what;
			switch (msgId) {
			case 1:
				CalcTraffic calcTraffic = new CalcTraffic();
				calcTraffic.createFile(onlyMobile);
				break;
			default:
				break;
			}
		}
	};

	public void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	
}
