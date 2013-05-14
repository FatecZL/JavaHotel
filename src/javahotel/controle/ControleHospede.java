/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javahotel.controle;

/**
 *
 * @author mailson
 */

import javahotel.excecao.JavaHotelException;
import javahotel.modelo.Hospede;
import javahotel.persistencia.HospedeDao;
import java.util.*;

public class ControleHospede {
  public void adiciona (Hospede hospede) throws JavaHotelException{
        HospedeDao dao = new HospedeDao ();
        dao.gravarHospede(hospede);
    }
    public List <Hospede> realizaPesquisa(String nome) throws JavaHotelException{
      List <Hospede> lista = new ArrayList<Hospede>();
      HospedeDao dao = new HospedeDao();
      lista = dao.pesquisarHospede(nome);
      return lista;
      
    }
    public void excluirHospede(Hospede Hospede) throws JavaHotelException {
        HospedeDao dao = new HospedeDao();
        dao.excluirHospede(Hospede);
}
  
    
    

}
