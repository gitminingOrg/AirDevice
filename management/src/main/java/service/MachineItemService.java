package service;

import model.order.MachineItem;
import pagination.DataTableParam;
import utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public interface MachineItemService {
    public ResultData fetch(Map<String, Object> condition);

    public ResultData fetch(DataTableParam param);

    public ResultData create(MachineItem machineItem);

    public ResultData update(MachineItem machineItem);

    public ResultData updateBatch(List<MachineItem> machineItems);
}
