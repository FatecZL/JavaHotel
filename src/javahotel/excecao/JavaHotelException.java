/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javahotel.excecao;

/**
 *
 * @author Sebastiana
 */
public class JavaHotelException extends Exception {

    public JavaHotelException(String message) {
        super(message);
    }

    public JavaHotelException() {
        super("erro:desconhecido");
    }

}
