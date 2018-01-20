package dao;

import model.order.SetupProvider;
import utils.ResultData;

import java.util.Map;

public interface SetupProviderDao {
    ResultData insert(SetupProvider provider);

    ResultData query(Map<String, Object> condition);

    ResultData update(SetupProvider provider);

    ResultData delete(String providerId);
}
