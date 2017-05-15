package dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import bean.UserAction;

@Repository
public class DeviceStatusDao extends BaseDaoImpl{
	public List<UserAction> getUserActions(String userID){
		return sqlSession.selectList("userAction.selectUserActions", userID);
	}
	
	public boolean insertUserAction(UserAction userAction){
		return sqlSession.insert("userAction.insertUserAction", userAction) > 0;
	}
}
