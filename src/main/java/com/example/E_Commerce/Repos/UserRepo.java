package com.example.E_Commerce.Repos;


import com.example.E_Commerce.Entities.UserClass;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserClass, String> {
    UserClass findByUserName(String username);

    Optional<UserClass> findByUserId(String userID);

    @EntityGraph(attributePaths = "orderClass")
    List<UserClass> findAll();

}
