package device.service;

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
}