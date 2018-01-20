package service.impl;

import dao.MachineItemDao;
import model.order.MachineItem;
import org.springframework.beans.factory.annotation.Autowired;
import service.MachineItemService;
import utils.ResultData;

import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public class MachineItemServiceImpl implements MachineItemService{

    @Autowired
    private MachineItemDao machineItemDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineItemDao.query(condition);
    }

    @Override
    public ResultData create(MachineItem machineItem) {
        return machineItemDao.insert(machineItem);
    }

    @Override
    public ResultData update(MachineItem machineItem) {
        return machineItemDao.update(machineItem);
    }
}
