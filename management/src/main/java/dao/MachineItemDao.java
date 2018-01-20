package dao;

import model.order.MachineItem;
import utils.ResultData;

import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public interface MachineItemDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(MachineItem machineItem);

    ResultData update(MachineItem machineItem);
}
