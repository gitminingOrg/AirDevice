package dao;

import model.order.MachineItem;
import pagination.DataTableParam;
import utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public interface MachineItemDao {

    ResultData query(Map<String, Object> condition);

    ResultData query(DataTableParam param);

    ResultData insert(MachineItem machineItem);

    ResultData insertBatch(List<MachineItem> machineItemList);

    ResultData update(MachineItem machineItem);

    ResultData updateBatch(List<MachineItem> machineItems);
}
