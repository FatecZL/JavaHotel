/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javahotel.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javahotel.excecao.JavaHotelException;

/**
 *
 * @author Sebastiana
 */
public class GeradorDeChave {
    
    private static final byte INCREMENTO = 1;
    private Connection con;
    private String tabela;
    private long proximoCodigo;
    private long maximoCodigo;

    public GeradorDeChave(String tabela) throws 
            JavaHotelException{
        this.con = GerenciadorDeConexao.getConexao();
        this.tabela = tabela;
        proximoCodigo = 0;
        maximoCodigo = 0;
        
        try{
            con.setAutoCommit(false);
        }catch(SQLException ex){
            StringBuffer mensagem = new StringBuffer("Não foi possível "
                    + "desligar a confirmação automática");
            mensagem.append("\nMotivo: "+ ex.getMessage());
            throw new JavaHotelException(mensagem.toString());
        }
    }
    
    public synchronized long getProximoCodigo() throws JavaHotelException 
    {
        if(proximoCodigo==maximoCodigo){
            reservarCodigo();
        }
        return proximoCodigo;
    }

    private void reservarCodigo() throws JavaHotelException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long proximoCodigoNovo;
        String sql = "select  proximocodigo from chaves where tabela = ? "
                + "for update";
        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1, tabela);
            rs = stmt.executeQuery();
            rs.next();
            proximoCodigoNovo = rs.getLong("proximoCodigo");
        }catch(SQLException ex){
            StringBuffer mensagem = new StringBuffer("Não foi possível "
                    + "gerar o código");
            mensagem.append("\nMotivo: "+ex.getMessage());
            throw  new JavaHotelException(mensagem.toString());
        }long maximoCodigoNovo = proximoCodigoNovo + INCREMENTO;
        stmt = null;
        rs = null;
        
        try {
            sql = "UPDATE chaves SET proximoCodigo = ? WHERE tabela = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, maximoCodigoNovo);
            stmt.setString(2, tabela);
            stmt.executeUpdate();
            con.commit();
            proximoCodigo = proximoCodigoNovo;
            maximoCodigo = maximoCodigoNovo;
        } catch ( SQLException exc){
            StringBuffer mensagem = new StringBuffer("Não foi possível "
                    + "gerar o código");
            mensagem.append("\nMotivo: " + exc.getMessage());
            throw new JavaHotelException(mensagem.toString());
        } finally {
            GerenciadorDeConexao.closeConexao(con, stmt, rs);
        }
        
    }
    
}
