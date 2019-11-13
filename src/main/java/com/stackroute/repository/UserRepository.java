package com.stackroute.repository;

import com.stackroute.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DAOUser,String> {
    DAOUser findByEmailIdIgnoreCase(String emailId);
    DAOUser findByEmailId(String emailId);
}
