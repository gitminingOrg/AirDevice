package dao;

import model.order.GuoMaiOrder;
import utils.ResultData;

import java.util.List;

/**
 * Created by hushe on 2017/12/23.
 */
public interface OrderBatchDao {
    ResultData insert(List<GuoMaiOrder> order);
}
