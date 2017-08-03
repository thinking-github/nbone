package org.nbone.bean.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Employee { 
	 @NotNull(message = "The id of employee can not be null") 
	 private Integer id; 

	 @NotNull(message = "The name of employee can not be null") 
	 @Size(min = 1,max = 10,message="The size of employee's name must between 1 and 10") 
	 private String name; 

	 @NotNull(message = "The passwrod of employee can not be null-----------") 
	 @Size(min = 8,max = 10) 
	 private String passwrod; 
	 
	 
	 public int getId() { 
	 return id; 
	 } 
	 public void setId(int id) { 
	 this.id = id; 
	 } 
	 public String getName() { 
	 return name; 
	 } 
	 public void setName(String name) { 
	 this.name = name; 
	 } 
	 
	 public String getPasswrod() {
		return passwrod;
	 }
	 public void setPasswrod(String passwrod) {
		this.passwrod = passwrod;
	 }
	 
	 
	public static void main(String[] args) { 
		 Employee employee = new Employee(); 
		 employee.setName("Zhang Guan Nan"); 
		 employee.setPasswrod("002");
		 
		 ValidatorFactory vf = Validation.buildDefaultValidatorFactory(); 
		 Validator validator = vf.getValidator(); 
		 Set<ConstraintViolation<Employee>> set = validator.validate(employee); 
		 for (ConstraintViolation<Employee> constraintViolation : set) { 
			 System.out.println("==="+constraintViolation.getMessage()); 
		 } 
	 } 
}