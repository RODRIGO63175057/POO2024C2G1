package pe.edu.upeu.calcfx.servicio;

//EN UN INTERFACE JAMAS PUEDE IR IMPLEMENTACION ,SOLO DECLARAR
import pe.edu.upeu.calcfx.modelo.CalcTO;

import java.util.List;

public interface CalcServiceI {
    public void guardarResultados(CalcTO to);
    public List<CalcTO>obtenerResultados();
    public void eliminiarResultados(int index);//Ua
    public void actualizarResultados(CalcTO to,int index);//D





}
