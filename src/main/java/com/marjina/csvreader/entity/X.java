package com.marjina.csvreader.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class X {

    @Id
    @GeneratedValue
    private Long id;

    private String A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String F;
    private String G;
    private String H;
    private String I;
    private String J;

    public Boolean isComplete(){
        return !this.getA().isEmpty()&&!this.getB().isEmpty()&&!this.getC().isEmpty()&&!this.getD().isEmpty()&&
                !this.getE().isEmpty()&&!this.getF().isEmpty()&&!this.getG().isEmpty()
                &&!this.getH().isEmpty()&&!this.getI().isEmpty()&&!this.getJ().isEmpty();
    }
}
