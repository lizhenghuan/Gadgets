package cn.robotium.traffictest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	private EditText editText;
	private Button button;
	private Button button1;
	private CheckBox checkBox;
	private String text;
	
	private boolean onlyMobile = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		button1 = (Button) findViewById(R.id.button2);
		button.setOnClickListener(this);
		button1.setOnClickListener(this);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					onlyMobile = isChecked;
				}else {
					onlyMobile = isChecked;
				}

			}
		});;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			System.out.println(onlyMobile + "==========");
			editText.setText("");
			CalcTraffic calcTraffic = new CalcTraffic();
			editText.setText(calcTraffic.calc(onlyMobile));
			break;
		case R.id.button2:
			 Intent service=new Intent(this, MyService.class);
			 this.startService(service);
			break;
		default:
			break;
		}
	}
	

	

	

}
