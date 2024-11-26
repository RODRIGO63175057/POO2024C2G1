package pe.edu.upeu.tienda.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.tienda.modelo.Emisor;

@Repository
public interface EmisorRepository extends JpaRepository<Emisor, Long> {
}
