package rs.esolutions.ecomm.android.domain;

/**
 * Placeholder class for registration data
 * 
 * @author Gavrilovici
 *
 */
public class RegistrationData {
	
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_SURNAME = "surname";
	public static final String PROPERTY_PHONE_NUMBER = "phoneNumber";
	public static final String PROPERTY_IMSI = "imsi";
	public static final String PROPERTY_UPLOAD_KEY= "uploadKey";
	
	private String name; 
	private String surname;
	private String phoneNumber;
	private String imsi;
	private String city;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}