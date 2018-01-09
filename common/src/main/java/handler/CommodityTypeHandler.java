package handler;

import model.order.CommodityType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hushe on 2018/1/8.
 */
public class CommodityTypeHandler extends BaseTypeHandler<CommodityType> {
    private Class<CommodityType> type;

    private final CommodityType[] enums;

    private Logger logger = LoggerFactory.getLogger(CommodityTypeHandler.class);

    public CommodityTypeHandler(Class<CommodityType> type) {
        if (type == null) {
            throw new IllegalArgumentException("参数type不能为空");
        }
        this.type = type;
        enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + "不是一个枚举类型");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CommodityType type, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, type.getCode());
    }

    @Override
    public CommodityType getNullableResult(ResultSet rs, String c) throws SQLException {
        int i = rs.getInt(c);
        if (rs.wasNull()) {
            return null;
        } else {
            return locateEnumType(i);
        }
    }

    @Override
    public CommodityType getNullableResult(ResultSet rs, int i) throws SQLException {
        int index = rs.getInt(i);
        if (rs.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    @Override
    public CommodityType getNullableResult(CallableStatement cs, int i) throws SQLException {
        int index = cs.getInt(i);
        if (cs.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    private CommodityType locateEnumType(int code) {
        for (CommodityType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }

}
