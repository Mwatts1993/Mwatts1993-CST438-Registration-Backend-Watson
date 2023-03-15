package com.cst438.domain;

public class StudentDTO {

    public String studentName;

    public String studentEmail;

    public String studentStatus;

    public StudentDTO() {
        this.studentEmail = null;
        this.studentName = null;
        this.studentStatus = null;
    }

    public StudentDTO(String email, String name) {
        this.studentEmail = email;
        this.studentName = name;
        this.studentStatus = null;
    }

    public StudentDTO(String email, String name, String status) {
        this.studentEmail = email;
        this.studentName = name;
        this.studentStatus = status;
    }

    @Override
    public String toString() {
        return "StudentDTO [studentEmail=" + this.studentEmail + ", studentName=" + this.studentName
                + ", studentStatus=" + this.studentStatus + "]";
    }

}

