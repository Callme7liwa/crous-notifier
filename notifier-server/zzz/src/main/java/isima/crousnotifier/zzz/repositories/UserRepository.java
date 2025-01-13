package isima.crousnotifier.zzz.repositories;

import isima.crousnotifier.zzz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM users u WHERE " +
           "u.listCodeZip LIKE %:codeZip% " +
           "OR u.listCodeZip = :codeZip")
    List<User> findByListCodeZip(@Param("codeZip") String codeZip);

    Optional<User> findByEmail(String email);
}