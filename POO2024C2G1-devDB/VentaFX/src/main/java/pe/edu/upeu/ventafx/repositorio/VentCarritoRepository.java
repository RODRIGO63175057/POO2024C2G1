package pe.edu.upeu.ventafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.ventafx.modelo.VentCarrito;

@Repository
public interface VentCarritoRepository  extends JpaRepository<VentCarrito, Long> {
}
