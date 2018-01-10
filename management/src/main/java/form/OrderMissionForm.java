package form;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class OrderMissionForm {
	
	private String missionTitle;
	
	private String missionContent;


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

}
