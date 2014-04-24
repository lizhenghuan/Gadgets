package cn.robotium.traffictest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.TrafficStats;
import android.os.Environment;

public class CalcTraffic {
	public CalcTraffic() {
	}
	
	public long getMoblieTraffic(){
		TrafficStats stats =  new TrafficStats();
		return stats.getMobileRxBytes() + stats.getMobileTxBytes();
		
	}
	
	public long getAllTraffic(){
		TrafficStats stats =  new TrafficStats();
		return  stats.getTotalRxBytes()+ stats.getTotalTxBytes();
	}
	
	public String calc(boolean onlyMobile){
		String content = "";
		DecimalFormat fomart = new DecimalFormat();
		fomart.setMaximumFractionDigits(2);
		fomart.setMinimumFractionDigits(2);
		if (onlyMobile) {
			return content ="Moblie：" +  fomart.format((getMoblieTraffic()/1024)/1024) + "MB";
		} else {
			return content ="Moblie：" +  fomart.format((getMoblieTraffic()/1024)/1024) + "MB" + "    " + 
					"All：" +  fomart.format((getAllTraffic()/1024)/1024) + "MB";
		}
	}
	
	/**
	 * 生成脚本文件,如test.sh或 test.bat
	 */
	public void createFile(boolean onlyMobile){
		try {
			System.out.println("测试数据写入...");
			String SDCardRoot;
			SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
			File file = new File(SDCardRoot + File.separator + "test.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file, true);
			String newline = System.getProperty("line.separator");;
			byte[] bytes = calc(onlyMobile).getBytes();
			out.write(newline.getBytes());
			out.write(getCurrentTimeForFile().getBytes());
			out.write("    ".getBytes());
			out.write(bytes);
			out.flush();  
			out.close(); 
			System.out.println("test脚本写入完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentTimeForFile(){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH-mm-ss");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间        
		String str = formatter.format(curDate); 
		return str;
	}
	

	
}
