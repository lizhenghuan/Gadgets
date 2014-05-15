package cn.robotium.app;

import java.util.ArrayList;
import java.util.List;

import cn.robotium.ITestAidl;
import cn.robotium.ITestAidl.Stub;
import cn.robotium.client.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class AidlTestClient extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Button btn = null;
	private Button btn2 = null;
	private ITestAidl iTestAidl = null;
	private ServiceConnection connection = new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iTestAidl = ITestAidl.Stub.asInterface(service);
			System.out.println("Bind Success:"+iTestAidl);
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			iTestAidl = null;
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btn = (Button)findViewById(R.id.Button01);
        btn2 = (Button) findViewById(R.id.Button02); 
        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }
    
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		if (viewId == btn.getId()){
			Intent service = new Intent(ITestAidl.class.getName());
			bindService(service, connection, BIND_AUTO_CREATE);
		} else if (viewId == btn2.getId()) {
			try {
				iTestAidl.setWifiDisabled();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(connection);
	}
}