package form;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class OrderMissionForm {
	
	private String missionTitle;
	
	private String missionContent;

	private String missionChannel;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime missionDate;

	private String missionInstallType;

	private String codeId;

	private String filePath;

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

	public String getMissionChannel() {
		return missionChannel;
	}

	public void setMissionChannel(String missionChannel) {
		this.missionChannel = missionChannel;
	}

	public LocalDateTime getMissionDate() {
		return missionDate;
	}

	public void setMissionDate(LocalDateTime missionDate) {
		this.missionDate = missionDate;
	}

	public String getMissionInstallType() {
		return missionInstallType;
	}

	public void setMissionInstallType(String missionInstallType) {
		this.missionInstallType = missionInstallType;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
