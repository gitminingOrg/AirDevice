package dao;

import java.util.List;

import model.device.DeviceChip;

public interface DeviceChipDao {
	boolean insert(DeviceChip dc);
	
	public List<String> getBindedChips();
}
