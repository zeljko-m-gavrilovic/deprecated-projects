package rs.os.contactbook.model.contact;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("organization")
public class Organization extends Contact {
	private static final long serialVersionUID = -1848539719578306372L;

	private String pib;

	public Organization() {
		type = Type.Organization;
	}

	public String getPib() {
		return pib;
	}

	public void setPib(String pib) {
		this.pib = pib;
	}

	@Override
	public String toString() {
		return "Organization [name=" + name + ", pib=" + pib + "]";
	}

}