package dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import bean.QRCode;

@Repository
public class DeviceAttributeDao extends BaseDaoImpl{
	public QRCode getQRCode(String codeValue){
		if(codeValue == null){
			return null;
		}
		List<QRCode> qrcodes = sqlSession.selectList("deviceAttribute.getOne", codeValue);
		if(qrcodes == null || qrcodes.size() == 0){
			return null;
		}else {
			return qrcodes.get(0);
		}
	}
	
	public boolean occupyQRCode(String codeValue){
		if(codeValue == null){
			return false;
		}else{
			return sqlSession.update("deviceAttribute.occupyQRCode", codeValue) >= 0;
		}
	}
}
