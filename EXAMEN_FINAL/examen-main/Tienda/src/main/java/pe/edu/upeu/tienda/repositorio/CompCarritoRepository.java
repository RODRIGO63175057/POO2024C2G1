package pe.edu.upeu.tienda.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.tienda.modelo.CompCarrito;

@Repository
public interface CompCarritoRepository extends JpaRepository<CompCarrito, Long> {
}
