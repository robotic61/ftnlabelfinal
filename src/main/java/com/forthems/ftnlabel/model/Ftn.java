package com.forthems.ftnlabel.model;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "FTN", schema = "dbo")
public class Ftn {

    @Id
    @Column(name = "FTN_No")
    private String ftnNo; // Primary key
    // field name is used to match with JSON body

    @Column(name = "Material_No")
    private String pn;

    @Column(name = "MSL")
    private String msl; // All NULL

    @Column(name = "Description")
    private String description;

    @Column(name = "Brand")
    private String brand;

    @Column(name = "Maker")
    private String maker;

    @Column(name = "DateCode")
    private String dc; // mostly NULL

    @Column(name = "latest_expire_date")
    private LocalDateTime exp;

    @Column(name = "LotCode")
    private String lotCode;

    @Column(name = "CustBatch")
    private String custBatch;

    @Column(name = "CustomerPart")
    private String custPart;

    @Column(name = "Batch")
    private String batch;

    @Column(name = "Shelf")
    private String shelf;

    @Column(name = "Project")
    private String project;

    @Column(name = "Qty")
    private Integer qty;
    // can always use wrapper type(class type),
    // for field names, in case db column is NULL,
    // int default = 0
    // INTEGER default = NULL

    @Column(name = "BaseUnit")
    private String baseUnit;

    // try lombok to make the code cleaner without showing getter and setter, but we can use them.
}
