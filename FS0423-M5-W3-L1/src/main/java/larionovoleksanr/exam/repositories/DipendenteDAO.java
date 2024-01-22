package larionovoleksanr.exam.repositories;

import larionovoleksanr.exam.entities.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DipendenteDAO extends JpaRepository<Dipendente, Long> {
    Optional<Dipendente> findByEmail(String email);
}
