package com.gll.learn.demo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "service_status"/*, schema = "testshell"*/)//, catalog = ""
public class ServiceStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column
    private String serviceName;

    @Column
    private Date time;

    @NonNull
    @Column
    private String reason;

    @Column
    private String origion;
}
