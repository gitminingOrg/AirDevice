package service;

import model.order.MachineItem;
import utils.ResultData;

import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public interface MachineItemService {
    public ResultData fetch(Map<String, Object> condition);

    public ResultData create(MachineItem machineItem);

    public ResultData update(MachineItem machineItem);
}
