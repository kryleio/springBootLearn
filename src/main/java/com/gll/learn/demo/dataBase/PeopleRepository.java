package com.gll.learn.demo.dataBase;

import com.gll.learn.demo.pojo.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<People, Integer>{

    public People findById(Long id);

    public People save(People peo);

    @Query(value = "SELECT p FROM People p WHERE p.name=:name")
    public People findName(@Param("name") String name);

}
