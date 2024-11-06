package pe.edu.upeu.ventafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.ventafx.modelo.Proveedor;

@Repository
public interface ProveedorRepository  extends JpaRepository<Proveedor, Long> {
}
