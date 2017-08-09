package wechat.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import dao.BaseDaoImpl;
@Repository
public class WechatDao extends BaseDaoImpl{
	public String getOpenID(String userID){
		List<String> openIDs = sqlSession.selectList("userVip.selectOpenID", userID);
		if(openIDs == null || openIDs.size() == 0){
			return null;
		}else{
			return openIDs.get(0);
		}
	}
}
