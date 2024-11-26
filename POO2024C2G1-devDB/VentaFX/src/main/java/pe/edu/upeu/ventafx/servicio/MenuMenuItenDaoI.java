package pe.edu.upeu.ventafx.servicio;

import pe.edu.upeu.ventafx.dto.MenuMenuItenTO;

import java.util.List;
import java.util.Properties;

public interface MenuMenuItenDaoI {

    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma);

}
