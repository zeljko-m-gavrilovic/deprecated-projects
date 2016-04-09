package rs.os.contactbook.model.contact;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;

import rs.os.contactbook.model.address.Address;
import rs.os.contactbook.model.address.Email;
import rs.os.contactbook.model.address.Phone;
import rs.os.contactbook.model.address.Website;
import rs.os.contactbook.model.common.Celebration;
import rs.os.core.model.common.Persistable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contact_type", discriminatorType = DiscriminatorType.STRING)
public class Contact extends Persistable {
	private static final long serialVersionUID = -7997620356043828037L;

	public static String PROPERTY_NAME = "name";
	public static String PROPERTY_COMMENT = "comment";
	public static String PROPERTY_TYPE = "type";
	public static String PROPERTY_ADDRESSES = "addresses";
	public static String PROPERTY_PHONES = "phones";
	public static String PROPERTY_EMAILS = "emails";
	public static String PROPERTY_WEBSITES = "websites";
	public static String PROPERTY_CELEBRATIONS = "celebrations";

	protected String name;
	protected String comment;
	protected Type type;

	protected List<Address> addresses;
	protected List<Phone> phones;
	protected List<Email> emails;
	protected List<Website> websites;
	protected List<Celebration> celebrations;

	public enum Type {
		Contact, Person, Organization
	};
	
	public Contact() {
		addresses = new ArrayList<Address>();
		phones = new ArrayList<Phone>();
		emails = new ArrayList<Email>();
		websites = new ArrayList<Website>();
		celebrations = new ArrayList<Celebration>();
		type = Type.Contact;
	}

	@OneToMany
	@IndexColumn(name = "INDEX_COL")
	@JoinColumn(name = "contact_id", nullable = false, updatable = true)
	@Cascade(value = CascadeType.ALL)
	public List<Address> getAddresses() {
		return addresses;
	}

	public void addAddress(Address address) {
		getAddresses().add(address);
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@OneToMany
	@IndexColumn(name = "INDEX_COL")
	@JoinColumn(name = "contact_id", nullable = false)
	@Cascade(value = CascadeType.ALL)
	public List<Phone> getPhones() {
		return phones;
	}

	public void addPhone(Phone phone) {
		getPhones().add(phone);
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	@OneToMany
	@IndexColumn(name = "INDEX_COL")
	@JoinColumn(name = "contact_id", nullable = false)
	@Cascade(value = CascadeType.ALL)
	public List<Email> getEmails() {
		return emails;
	}

	public void addEmail(Email email) {
		getEmails().add(email);
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	@OneToMany()
	@IndexColumn(name = "INDEX_COL")
	@JoinColumn(name = "contact_id", nullable = false)
	@Cascade(value = CascadeType.ALL)
	public List<Website> getWebsites() {
		return websites;
	}

	public void addWebsite(Website website) {
		getWebsites().add(website);
	}

	public void setWebsites(List<Website> websites) {
		this.websites = websites;
	}

	@OneToMany()
	@IndexColumn(name = "INDEX_COL")
	@JoinColumn(name = "contact_id", nullable = false)
	@Cascade(value = CascadeType.ALL)
	public List<Celebration> getCelebrations() {
		return celebrations;
	}

	public void addCelebration(Celebration celebration) {
		getCelebrations().add(celebration);
	}

	public void setCelebrations(List<Celebration> celebrations) {
		this.celebrations = celebrations;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Contact [comment=" + comment + ", name=" + name + ", type="
				+ type + ", id=" + id + "]";
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}