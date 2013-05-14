/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javahotel.persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javahotel.constante.Constante;
import javahotel.excecao.JavaHotelException;
import javahotel.modelo.Hospede;

/**
 *
 * @author Sebastiana
 */
public class HospedeDao {
    
    Connection con = null;
    PreparedStatement stmt = null;
    Hospede hospede;
    ResultSet rs = null;
   
    private static final String SQL_INCLUIRHOSPEDE = "insert into hospede (codigohospede,nome,cpf,telefonefixo,"+
                                                   "telefonecelular,email) values (?,?,?,?,?,?)";
    
    private static final String SQL_ALTERARHOSPEDE = "UPDATE hospede set "
            +"nome=?,cpf=?,"
            +"telefonefixo=?,telefonecelular=?,email=? "
            +"where codigohospede=?";
    private static final String SQL_EXCLUIRHOSPEDE =  
            "DELETE from hospede "
             +"where codigohospede = ?";
    
    private void alterarHospede(Hospede hospede) throws JavaHotelException {
        if (hospede == null){
        String mensagem = "Não foi informado o hospede a ser alterado";
        throw new JavaHotelException(mensagem);
        }
        
        con = null;
        stmt =null;
        try{
            con = GerenciadorDeConexao.getConexao();
            stmt = con.prepareStatement(SQL_ALTERARHOSPEDE);
            stmt.setString(1, hospede.getNome());
            stmt.setString(2, hospede.getCpf());
            stmt.setString(3, hospede.getTelefoneFixo());
            stmt.setString(4, hospede.getTelefoneCelular());
            stmt.setString(5, hospede.getEmail());
            stmt.setLong(6, hospede.getCodigo());
            stmt.executeUpdate();
            
            }catch(SQLException ex){
                StringBuffer mensagem = new StringBuffer("Não foi possível atualizar "
                + "os dados do hospede");
                mensagem.append("\nMotivo: "+ex.getMessage());
                throw new JavaHotelException(mensagem.toString());
            } finally {
            GerenciadorDeConexao.closeConexao(con, stmt);
        }
}
    public void excluirHospede(Hospede hospede) throws JavaHotelException {
        if (hospede == null){
        String mensagem = "Não foi informado o hospede a ser excluído!";
        throw new JavaHotelException(mensagem);
        }
        con = null;
        stmt = null;
        try {
        con = GerenciadorDeConexao.getConexao();
        stmt = con.prepareStatement(SQL_EXCLUIRHOSPEDE);
        stmt.setLong(1, hospede.getCodigo());
        stmt.executeUpdate();
        }catch(SQLException ex){
        StringBuffer mensagem = new StringBuffer("Não foi possível excluir o hospede");
        mensagem.append("\nMotivo: "+ex.getMessage());
        throw new JavaHotelException(mensagem.toString());
        } finally {
        GerenciadorDeConexao.closeConexao(con, stmt);
        }
        }
                                                    
   
    public void gravarHospede(Hospede hospede)throws JavaHotelException {
        if (hospede.getCodigo() == Constante.NOVO){
            incluirHospede(hospede);
        }
        
    }        
    
    public void incluirHospede (Hospede hospede) throws JavaHotelException{
        if (hospede == null){
            String mensagem = "não foi informado o hospede a cadastrar";
            throw new JavaHotelException(mensagem);
        }
        try{
            
            con = GerenciadorDeConexao.getConexao();
            stmt = con.prepareStatement (SQL_INCLUIRHOSPEDE);
            GeradorDeChave geradorDeChave = new GeradorDeChave("hospede");
            long codigosocio = geradorDeChave.getProximoCodigo();
            stmt.setLong (1,codigosocio);
            stmt.setString (3,hospede.getNome());
            stmt.setString (6, hospede.getTelefoneFixo());
            stmt.setString (7, hospede.getTelefoneCelular());
            stmt.setString (8, hospede.getEmail());
            stmt.executeUpdate();
            
        }
        catch (SQLException ex){
            StringBuffer mensagem = new StringBuffer ("não foi possível incluir o hospede");
            mensagem.append("\nMotivo:"+ex.getMessage());
            throw new JavaHotelException (mensagem.toString());            
        }
        finally{
            GerenciadorDeConexao.closeConexao (con,stmt);
        }
    }
    public List pesquisarHospede (String clausulaWhere) throws JavaHotelException {
          clausulaWhere = "%"+clausulaWhere+"%";
          String sql = "select * from hospede where nome like ?";
          List resultado = new ArrayList();
          try{
              con = GerenciadorDeConexao.getConexao();
              stmt = con.prepareStatement(sql);
              stmt.setString(1, clausulaWhere);
              rs = stmt.executeQuery();
              while (rs.next()){
                  Hospede hospede = criarHospede(rs);
                  resultado.add(hospede);
              }
          }catch (SQLException ex){
              StringBuffer mensagem = new StringBuffer ("não foi possivel realizar a pesquisa ");
              mensagem.append("\nMOtivo:" +ex);
              
          }finally{
              GerenciadorDeConexao.closeConexao(con,stmt,rs);
          }
          return resultado;
            }

    private Hospede criarHospede(ResultSet rs)throws JavaHotelException {
        Hospede hospede= new Hospede();
        try{
            hospede.setCodigo(rs.getLong("codigohospede"));
            hospede.setNome(rs.getString("nome"));
            hospede.setTelefoneFixo(rs.getString("telefonefixo"));
            hospede.setTelefoneCelular(rs.getString("telefonecelular"));
            hospede.setEmail(rs.getString("email"));
                                   
           }
           catch (SQLException exe){
               StringBuffer mensagem = new StringBuffer("Não foi possível obter os dados do hospede");
               mensagem.append("\nMOtivo:" +exe);
               throw new JavaHotelException(mensagem.toString());
           }
     return hospede;   
    }
    
}
