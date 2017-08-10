package device.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.ReturnCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.ReceptionConstant;
import config.ReceptionConfig;
import bean.CityAqi;
import bean.DeviceAir;
import dao.DeviceInitDao;

@Service
public class DeviceInitService {
	@Autowired
	private DeviceInitDao deviceInitDao;
	
	public ReturnCode enrichHistory(String deviceID){
		//calculate start time & end time
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String endTime = sdf.format(calendar.getTime());
		
		calendar.add(Calendar.DATE, 0-ReceptionConstant.DEFAULT_TOP_DAY);
		String startTime = sdf.format(calendar.getTime());
		
		
		//select city aqi
		List<CityAqi> cityAqiList = deviceInitDao.selectRangeCityAqi(deviceID, startTime, endTime);
		// aggregate data & generate result
		List<DeviceAir> deviceInitAirs = new ArrayList<DeviceAir>();
		String lastDay = null;
		int sum = 0;
		int count = 0;
		for (CityAqi cityAqi : cityAqiList) {
			String day = cityAqi.getTime().split(" ")[0];
			if(lastDay != null && ! day.equals(lastDay)){
				if(count != 0){
					int average = sum / count;
					DeviceAir deviceAir = new DeviceAir();
					deviceAir.setDate(lastDay);
					deviceAir.setDeviceID(deviceID);
					deviceAir.setOutsideAir(average);
					deviceAir.setInsideAir(0);
					deviceInitAirs.add(deviceAir);
				}
				count = 0;
				sum = 0;
				lastDay = day;
			}else if(lastDay == null){
				lastDay = day;
				count = 0;
				sum = 0;
			}
			
			count++;
			sum+=cityAqi.getPm25();
		}
		
		if(count != 0){
			int average = sum / count;
			DeviceAir deviceAir = new DeviceAir();
			deviceAir.setDate(lastDay);
			deviceAir.setDeviceID(deviceID);
			deviceAir.setOutsideAir(average);
			deviceAir.setInsideAir(0);
			deviceInitAirs.add(deviceAir);
		}
		//insert result to table
		boolean success = deviceInitDao.insertDeviceAirList(deviceInitAirs);
		if(success){
			return ReturnCode.SUCCESS;
		}
		//return
		return ReturnCode.FAILURE;
	}
}
