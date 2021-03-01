package com.akashneil.batch.dao.repository;

import com.akashneil.batch.dao.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {
}
