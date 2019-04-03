package org.nbone.demo.spring.bean;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-02
 */
public class StudentC {

    private StudentA studentA;

    public StudentC() {
        System.out.println("create StudentC.");
    }

    public StudentC(StudentA studentA) {
        this.studentA = studentA;
        System.out.println("create StudentC.");
    }

    public StudentA getStudentA() {
        return studentA;
    }

    public void setStudentA(StudentA studentA) {
        this.studentA = studentA;
    }
}
