package pe.edu.upeu.ventafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.ventafx.modelo.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

}
