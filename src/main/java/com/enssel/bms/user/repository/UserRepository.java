package com.enssel.bms.user.repository;

import com.enssel.bms.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, String> {
}
