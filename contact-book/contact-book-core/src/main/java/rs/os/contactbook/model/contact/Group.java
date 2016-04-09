package rs.os.contactbook.model.contact;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import rs.os.core.model.common.Persistable;
import rs.os.core.model.common.Tag;

@Entity
@Table(name = "app_group")
public class Group extends Persistable {

	private static final long serialVersionUID = 1039354764064835284L;
	private String name;
	private String comment;
	private List<Tag> tags;
	private List<Contact> members;

	public Group() {
		members = new ArrayList<Contact>();
		tags = new ArrayList<Tag>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(/* mappedBy = "groups" */)
	@JoinTable(name = "contact_group", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "contact_id"))
	public List<Contact> getMembers() {
		return members;
	}

	public void setMembers(List<Contact> members) {
		this.members = members;
	}

	public void addMember(Contact member) {
		getMembers().add(member);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Group [comment=" + comment + ", name=" + name + "]";
	}

}