/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.certificado;

import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import siidte.TestingDTE;

/**
 *
 * @author Esteban
 */
public class NumeroUnoTest {
    public static void main(String[] args) {
        try {
            TestingDTE.VerificarDocumentoEnvio("D:/ATL/TestDocumentoEnvio_1.xml");
        } catch (XmlException ex) {
            Logger.getLogger(NumeroUnoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NumeroUnoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NumeroUnoTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
