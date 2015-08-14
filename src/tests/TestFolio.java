/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.estebanfuentealba.dte.Folio;
import net.estebanfuentealba.dte.TipoDocumento;

/**
 *
 * @author Esteban
 */
public class TestFolio {

    private static final String URL_CERT = VARIABLES_GLOBALES.URL_CERT;
    private static final String PASS_CERT = VARIABLES_GLOBALES.PASS_CERT;

    public static void main(String[] args) {
        try {
            
            String rut = VARIABLES_GLOBALES.RUT_EMPRESA;
            int numeroDeArchivos = 1;

//gay

            String[] data = rut.split("-");
            Folio folio = new Folio();
            String xml = folio.get(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
                    data[0],
                    data[1],
                    numeroDeArchivos,
                    URL_CERT,
                    PASS_CERT);
            System.out.println(xml);
        } catch (Exception ex) {
            Logger.getLogger(TestFolio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
