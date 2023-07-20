package com.obss.metro.v1.repository;

import com.obss.metro.v1.entity.InUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InUserRepository extends JpaRepository<InUser, String> {}
