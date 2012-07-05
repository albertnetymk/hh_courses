package foundation;

import java.util.Date;

public class Timespace {

	private Date date;
	private String position;
	private String uby;

	public Timespace(Date date, String position) {
		this.date = date;
		this.position = position;
		this.uby = "";
	}

	public Timespace(Date date, String position, String uby) {
		this.date = date;
		this.position = position;
		this.uby = uby;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Internationalization.dateFormat.format(date) + " ");
		sb.append(position + " " + uby);
		return sb.toString();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUby() {
		return uby;
	}

	public void setUby(String uby) {
		this.uby = uby;
	}

}
