/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javahotel.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javahotel.excecao.JavaHotelException;

/**
 *
 * @author Sebastiana
 */
public class GerenciadorDeConexao {
    
    private static final String URL = "jdbc:mysql://localhost:3306/javahotel";
    private static final String USER = "equipescrum";
    private static final String PASSWORD = "forcamaxima";

    static Connection getConexao() throws JavaHotelException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException ex) {
            StringBuffer mensagem = new StringBuffer("Não foi possivel"
                    + "estabelecer conexao com o banco de dados");
            mensagem.append("\nMotivo:" + ex.getMessage());
            throw new JavaHotelException(mensagem.toString());
        }

    }

    static void closeConexao(Connection con) throws JavaHotelException {
        closeConexao(con, null, null);
    }

    static void closeConexao(Connection con, PreparedStatement stmt)
            throws JavaHotelException {
        closeConexao(con, stmt, null);
    }

    static void closeConexao(Connection con, PreparedStatement stmt,
            ResultSet rs) throws JavaHotelException {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            StringBuffer mensagem = new StringBuffer("Não foi Possivel"
                    + "finalizar a conexão com o banco de dados.");
            mensagem.append("\nMotivo:" + ex.getMessage());
            throw new JavaHotelException(mensagem.toString());

        }
    }
}
