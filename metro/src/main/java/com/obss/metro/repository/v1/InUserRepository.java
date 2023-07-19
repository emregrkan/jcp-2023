package com.obss.metro.repository.v1;

import com.obss.metro.entity.v1.InUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InUserRepository extends JpaRepository<InUser, String> {
}
