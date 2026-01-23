package com.Prakhar.Ecommerce.repo;

import com.Prakhar.Ecommerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

      User findByEmail(String Email);


}
