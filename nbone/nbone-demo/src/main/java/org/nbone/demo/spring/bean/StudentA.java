package org.nbone.demo.spring.bean;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-02
 */
public class StudentA {

    private StudentB studentB ;

    public StudentA() {
        System.out.println("create StudentA.");

    }

    public StudentA(StudentB studentB) {
        this.studentB = studentB;
        System.out.println("create StudentA.");
    }

    public StudentB getStudentB() {
        return studentB;
    }

    public void setStudentB(StudentB studentB) {
        this.studentB = studentB;
    }
}
