package isima.crousnotifier.zzz.repositories;

import isima.crousnotifier.zzz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM users u WHERE " +
           "u.listCodeDepartement LIKE %:codeDepartement% " +
           "OR u.listCodeDepartement = :codeDepartement")
    List<User> findByListCodeDepartement(@Param("codeDepartement") String codeDepartement);
}