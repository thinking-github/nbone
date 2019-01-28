package org.nbone.demo.common.domain;

public class StudentAndTeacher {
	
	private User user;
	
	private  Teacher teacher;


	public StudentAndTeacher() {
	}

	public StudentAndTeacher(User user, Teacher teacher) {
		this.user = user;
		this.teacher = teacher;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	

}
