package device.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bean.QRCode;
import dao.DeviceAttributeDao;

@Service
public class DeviceAttributeService {
	@Autowired
	private DeviceAttributeDao deviceAttributeDao;
	
	public boolean qrcodeExist(String deviceID){
		if(deviceID == null){
			return false;
		}else{
			QRCode qrCode = deviceAttributeDao.getQRCode(deviceID);
			if(qrCode == null){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public boolean occupyQRCode(String code){
		if(code == null){
			return false;
		}else{
			return deviceAttributeDao.occupyQRCode(code);
		}
	}
	
	public boolean checkDeviceAdvanced(String deviceID){
		if(deviceID == null){
			return false;
		}else{
			return deviceAttributeDao.isDeviceAdvanced(deviceID);
		}
	}
	
	public List<String> getDeviceComponents(String deviceID){
		if(deviceID == null){
			return new ArrayList<String>();
		}
		return deviceAttributeDao.getDeviceComponents(deviceID);
	}
	
	public int getDeviceVelocity(String deviceID){
		if(deviceID == null){
			return 500;
		}else{
			return deviceAttributeDao.getDeviceVelocity(deviceID);
		}
	}
	
	public int getDeviceMinVelocity(String deviceID) {
		if(deviceID == null){
			return 0;
		}else{
			return deviceAttributeDao.getDeviceMinVelocity(deviceID);
		}
	}
}
