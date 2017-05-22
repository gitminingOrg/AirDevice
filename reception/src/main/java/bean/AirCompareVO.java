package bean;

import java.util.List;

public class AirCompareVO {
	List<String> dates;
	List<Integer> insides;
	List<Integer> outsides;
	public List<String> getDates() {
		return dates;
	}
	public void setDates(List<String> dates) {
		this.dates = dates;
	}
	public List<Integer> getInsides() {
		return insides;
	}
	public void setInsides(List<Integer> insides) {
		this.insides = insides;
	}
	public List<Integer> getOutsides() {
		return outsides;
	}
	public void setOutsides(List<Integer> outsides) {
		this.outsides = outsides;
	}
	
	
}
