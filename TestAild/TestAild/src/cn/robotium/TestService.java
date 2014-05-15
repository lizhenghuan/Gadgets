package cn.robotium;

import cn.robotium.ITestAidl.Stub;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

public class TestService extends Service {

	class testBinder extends ITestAidl.Stub {
		private Context context;
		private WifiManager mWifiManager;

		public testBinder(Context context) {
			this.context = context;
			mWifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
		}

		public boolean setWifiDisabled() {
			return mWifiManager.setWifiEnabled(false);
		}

		public boolean setWifiEnabled() {
			return mWifiManager.setWifiEnabled(true);
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return new testBinder(this);
	}
}
