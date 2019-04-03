package org.nbone.demo.spring.bean;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-02
 */
public class StudentB {

    private StudentC studentC;


    public StudentB() {
        System.out.println("create StudentB.");
    }

    public StudentB(StudentC studentC) {
        this.studentC = studentC;
        System.out.println("create StudentB.");
    }

    public StudentC getStudentC() {
        return studentC;
    }

    public void setStudentC(StudentC studentC) {
        this.studentC = studentC;
    }
}
