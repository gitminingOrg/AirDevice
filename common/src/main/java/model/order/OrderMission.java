package model.order;

import model.Entity;

import java.sql.Timestamp;

public class OrderMission extends Entity{
	private String missionId;
	
	private String orderId;
	
	private String missionTitle;
	
	private String missionContent;
	
	private String missionRecorder;

	private String missionChannel;

	private String missionInstallType;

	private Timestamp missionDate;

	public OrderMission() {
		super();
	}
	
	public OrderMission(String orderId, String missionTitle, String missionContent, String missionRecorder) {
		this();
		this.orderId = orderId;
		this.missionTitle = missionTitle;
		this.missionContent = missionContent;
		this.missionRecorder = missionRecorder;
	}

	public OrderMission(String orderId, String missionTitle, String missionContent, String missionRecorder,
						String missionChannel, String missionInstallType, Timestamp missionDate) {
		this.orderId = orderId;
		this.missionTitle = missionTitle;
		this.missionContent = missionContent;
		this.missionRecorder = missionRecorder;
		this.missionChannel = missionChannel;
		this.missionInstallType = missionInstallType;
		this.missionDate = missionDate;
	}

	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMissionTitle() {
		return missionTitle;
	}

	public void setMissionTitle(String missionTitle) {
		this.missionTitle = missionTitle;
	}

	public String getMissionContent() {
		return missionContent;
	}

	public void setMissionContent(String missionContent) {
		this.missionContent = missionContent;
	}

	public String getMissionRecorder() {
		return missionRecorder;
	}

	public void setMissionRecorder(String missionRecorder) {
		this.missionRecorder = missionRecorder;
	}

	public String getMissionChannel() {
		return missionChannel;
	}

	public void setMissionChannel(String missionChannel) {
		this.missionChannel = missionChannel;
	}

	public String getMissionInstallType() {
		return missionInstallType;
	}

	public void setMissionInstallType(String missionInstallType) {
		this.missionInstallType = missionInstallType;
	}

	public Timestamp getMissionDate() {
		return missionDate;
	}

	public void setMissionDate(Timestamp missionDate) {
		this.missionDate = missionDate;
	}
}
