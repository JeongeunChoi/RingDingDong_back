package com.ohh_really.ringdingdong.user.repository;

import com.ohh_really.ringdingdong.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

}
