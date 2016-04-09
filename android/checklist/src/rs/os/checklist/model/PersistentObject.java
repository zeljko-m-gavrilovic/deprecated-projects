package rs.os.checklist.model;

import java.io.Serializable;

/**
 * Keep track if the object is transient or persistent and implement appropriate hasCode and equals methods
 * @author zgavrilovic
 *
 */
public class PersistentObject implements Serializable {
	private static final long serialVersionUID = 8135992651076998212L;
	public Long TRANSIENT_OBJECT_ID = -1L;

	protected Long id;

	public PersistentObject() {
		id = TRANSIENT_OBJECT_ID;
	}

	public boolean isPersistent() {
		return !TRANSIENT_OBJECT_ID.equals(getId());
	}

	@Override
	public int hashCode() {
		if (isPersistent()) {
			int result = 17;
			result = 37 * result + getId().hashCode();
			return result;
		}
		return System.identityHashCode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		/*
		 * if (this == other) { return true; }
		 */
		if (other == null
				|| !PersistentObject.class.isAssignableFrom(other.getClass())) {
			return false;
		}

		if (isPersistent()) {
			PersistentObject castOther = (PersistentObject) other;
			return getId().equals(castOther.getId());
		} else {
			return (this == other);
		}
	}


	// getters and setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}