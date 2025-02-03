package com.findjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findjob.entity.UsersType;

@Repository
public interface UsersTypeRepository extends JpaRepository<UsersType, Integer>{

}
