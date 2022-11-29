package projectus.pus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projectus.pus.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String username);

    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);

//    @Query("select u from User u join fetch u.authorities a where u.email = :email")
//    Optional<User> findByEmailWithAuthority(String email);
}
