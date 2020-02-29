package com.gll.learn.demo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;

/*   lombok笔记
@NonNull：主要作用于成员变量和参数中，标识不能为空，否则抛出空指针异常。

@Data：作用于类上，是以下注解的集合：
@ToString @EqualsAndHashCode @Getter @Setter @RequiredArgsConstructor

@NoArgsConstructor：生成无参构造器；
@RequiredArgsConstructor：生成包含final和@NonNull注解的成员变量的构造器；
@AllArgsConstructor：生成全参构造器

@Cleanup：自动关闭资源，针对实现了java.io.Closeable接口的对象有效，如：典型的IO流对象

@SneakyThrows：可以对受检异常进行捕捉并抛出*/

@Data
@NoArgsConstructor
@Entity
@Table(name = "people")
public class People implements Serializable {

    /*TABLE：使用一个特定的数据库表格来保存主键。
    SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
    IDENTITY：主键由数据库自动生成（主要是自动增长型）
    AUTO：主键由程序控制。*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NonNull
    @Column(name = "name")
    protected String name;

    @Column(name = "age")
    protected String age;

    @Column(name = "sex")
    protected String sex;

    @Column(name = "text")
    protected String text;

}
