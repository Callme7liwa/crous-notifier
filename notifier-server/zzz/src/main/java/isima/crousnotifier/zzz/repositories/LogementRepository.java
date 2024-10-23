package isima.crousnotifier.zzz.repositories;

import isima.crousnotifier.zzz.models.Logement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogementRepository extends JpaRepository<Logement, Long> {
    Optional<Logement> findByCodeZip(String codeZip);
}
