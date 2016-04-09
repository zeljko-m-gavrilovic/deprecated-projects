package rs.os.contactbook.model.contact;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("person")
public class Person extends Contact {
	private static final long serialVersionUID = 760092404270688671L;

	private String firstName;
	private String middleName;
	private String lastName;
	private String title;
	private String jmbg;
	private Date dateOfBirth;

	public Person() {
		type = Type.Person;
	}

	@Column(name = "first_name", /*nullable = false,*/ length = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name", /*nullable = false,*/ length = 50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "jmbg", /*nullable = false,*/ length = 50)
	public String getJmbg() {
		return this.jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	@Column(name = "middle_name")
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Transient
	public String getName() {
		return title + " " + firstName + ' ' + lastName;
	}

	@Override
	public String toString() {
		return "Person [dateOfBirth=" + dateOfBirth + ", firstName="
				+ firstName + ", jmbg=" + jmbg + ", lastName=" + lastName
				+ ", middleName=" + middleName + ", title=" + title + "]";
	}

}