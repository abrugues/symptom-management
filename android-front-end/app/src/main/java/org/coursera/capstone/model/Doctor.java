package org.coursera.capstone.model;


public class Doctor {

	private long id;
	
	private String userName;
	
	private String firstName;
	private String lastName;
	
	private String docUId;
	
	public Doctor() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDocUId() {
		return docUId;
	}

	public void setDocUId(String docUId) {
		this.docUId = docUId;
	}
}
