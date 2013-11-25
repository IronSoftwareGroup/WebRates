/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tassidicambio;

/**
 *
 * @author bruno
 */
public class TassiDiCambio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ExClient c = new ExClient();
        c.getResource(args[0]);
    }
}
