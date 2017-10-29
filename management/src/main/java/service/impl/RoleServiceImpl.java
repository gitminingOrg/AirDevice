package service.impl;

import dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.RoleService;
import utils.ResultData;

import java.util.Map;


@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDao roleDao;

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = roleDao.query(condition);

        return result;
    }
}
