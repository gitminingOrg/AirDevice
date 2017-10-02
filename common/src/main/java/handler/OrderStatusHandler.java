package handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.order.OrderStatus;

public class OrderStatusHandler extends BaseTypeHandler<OrderStatus>{
	private Class<OrderStatus> status;
	
	private final OrderStatus[] enums;
	
    private Logger logger = LoggerFactory.getLogger(OrderStatusHandler.class);
	
    public OrderStatusHandler(Class<OrderStatus> status) {
        if (status == null) {
            throw new IllegalArgumentException("参数status不能为空");
        }
        this.status = status;
        enums = status.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(status.getSimpleName() + "不是一个枚举类型");
        }
    }
    
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, OrderStatus status, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, status.getCode());
	}

	@Override
	public OrderStatus getNullableResult(ResultSet rs, String c) throws SQLException {
		int i = rs.getInt(c);
        if (rs.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
	}

	@Override
	public OrderStatus getNullableResult(ResultSet rs, int i) throws SQLException {
		int index = rs.getInt(i);
        if (rs.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
	}

	@Override
	public OrderStatus getNullableResult(CallableStatement cs, int i) throws SQLException {
		int index = cs.getInt(i);
        if (cs.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
	}
	
	private OrderStatus locateEnumStatus(int code) {
        for (OrderStatus status : enums) {
            if (status.getCode() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.status.getSimpleName());
    }
}
