package isima.crousnotifier.zzz.repositories;

import isima.crousnotifier.zzz.models.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogementRepository extends JpaRepository<Logement, Long> {
    @Query("SELECT l FROM Logement l WHERE " +
           "l.titre LIKE :titre")
    Logement findByTitre(@Param("titre") String titre);

    List<Logement> findByCodeZip(@Param("codeZip") String codeZip);
}
