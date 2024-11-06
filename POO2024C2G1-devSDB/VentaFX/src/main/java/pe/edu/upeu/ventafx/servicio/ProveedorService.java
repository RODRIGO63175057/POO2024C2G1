package pe.edu.upeu.ventafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.ventafx.modelo.Proveedor;
import pe.edu.upeu.ventafx.repositorio.ProveedorRepository;

import java.util.List;

@Service
public class ProveedorService {
    @Autowired
    ProveedorRepository repo;

    public Proveedor save(Proveedor proveedor) {
        return repo.save(proveedor);
    }

    public List<Proveedor> list() {
        return repo.findAll();
    }

    public Proveedor update(Proveedor to) {
        try {
            Long id = to.getIdProveedor(); // Extrae el ID del objeto to
            Proveedor existingProveedor = repo.findById(id).orElse(null);
            if (existingProveedor != null) {
                existingProveedor.setDniRuc(to.getDniRuc());
                existingProveedor.setNombresRaso(to.getNombresRaso());
                existingProveedor.setCelular(to.getCelular());
                existingProveedor.setDireccion(to.getDireccion());
                existingProveedor.setEmail(to.getEmail());
                return repo.save(existingProveedor);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Proveedor searchById(Long id) {
        return repo.findById(id).orElse(null);
    }
}