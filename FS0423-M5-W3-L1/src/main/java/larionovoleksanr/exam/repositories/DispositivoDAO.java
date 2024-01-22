package larionovoleksanr.exam.repositories;

import larionovoleksanr.exam.entities.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispositivoDAO extends JpaRepository<Dispositivo, Long> {
}
