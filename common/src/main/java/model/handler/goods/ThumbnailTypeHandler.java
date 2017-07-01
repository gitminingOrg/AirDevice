package model.handler.goods;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.goods.ThumbnailType;

public class ThumbnailTypeHandler extends BaseTypeHandler<ThumbnailType>{
	private Logger logger = LoggerFactory.getLogger(ThumbnailTypeHandler.class);
	
	private Class<ThumbnailType> status;
	
	private final ThumbnailType[] enums;
	
	public ThumbnailTypeHandler(Class<ThumbnailType> status) {
		if(status == null) {
			throw new IllegalArgumentException("argument status cannot be null");
		}
		this.status = status;
		enums = status.getEnumConstants();
		if (enums == null) {
			throw new IllegalArgumentException(status.getSimpleName() + "does not match the type of enumeration");
		}
	}
	
	@Override
	public ThumbnailType getNullableResult(ResultSet arg0, String arg1) throws SQLException {
		int i = arg0.getInt(arg1);
		if (arg0.wasNull()) {
			return null;
		} else {
			return locateEnumStatus(i);
		}
	}

	@Override
	public ThumbnailType getNullableResult(ResultSet arg0, int arg1) throws SQLException {
		int index = arg0.getInt(arg1);
		if (arg0.wasNull()) {
			return null;
		} else {
			return locateEnumStatus(index);
		}
	}

	@Override
	public ThumbnailType getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
		int index = arg0.getInt(arg1);
		if (arg0.wasNull()) {
			return null;
		} else {
			return locateEnumStatus(index);
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, ThumbnailType arg2, JdbcType arg3)
			throws SQLException {
		arg0.setInt(arg1, arg2.getCode());
	}
	
	private ThumbnailType locateEnumStatus(int code) {
		for (ThumbnailType status : enums) {
			if (status.getCode() == Integer.valueOf(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException(
				"unknow enumeration type: " + code + ", please check " + this.status.getSimpleName());
	}
}
