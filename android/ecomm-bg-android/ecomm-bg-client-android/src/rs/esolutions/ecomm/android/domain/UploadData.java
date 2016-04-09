package rs.esolutions.ecomm.android.domain;



/**
 * UploadData is a placeholder for data we need to upload to server.
 * 
 * @author Gavrilovici
 *
 */
public class UploadData {
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_LATITUDE = "latitude";
	public static final String PROPERTY_LONGITUDE = "longitude";
	public static final String PROPERTY_COMMENT = "comment";
	public static final String PROPERTY_PHOTO = "photo";
	public static final String PROPERTY_CITY = "city";
	
	private String id;
	private String latitude;
	private String longitude;
	private String comment;
	private String city;
	private byte[] content;
	
	// plain getter/setter methods
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
}