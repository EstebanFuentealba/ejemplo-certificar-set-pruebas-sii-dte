/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siidte;

import cl.nic.dte.TimbreException;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.dsig.SignatureType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.xml.sax.SAXException;
import tests.VARIABLES_GLOBALES;

/**
 *
 * @author koala
 */
public class Main {

    private static final String URL_CERT = VARIABLES_GLOBALES.URL_CERT;
    private static final String PASS_CERT = VARIABLES_GLOBALES.PASS_CERT;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, XmlException, IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeyException {
     
            try {
                //Crear Documento Tributario
                TestingDTE.CrearDTE(5, "D:/Backup/FoliosSII7601980734520101014225.xml", null);

            } catch (InvalidAlgorithmParameterException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLSignatureException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MarshalException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimbreException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SignatureException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Verificar Documento Schema
            try {
            TestingDTE.VerificaFactura("D:/SIITEST/TestCrearFactura.xml");
            } catch (XmlException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SignatureException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
           try {

       HashMap<String, String> namespaces = new HashMap<String, String>();
       namespaces.put("", "http://www.sii.cl/SiiDte");
       XmlOptions opts = new XmlOptions();
       opts.setLoadSubstituteNamespaces(namespaces);
       DTEDocument doc=DTEDocument.Factory.parse(new File("D:/SIITEST/TestCrearFactura.xml"),opts);
      //  SignatureType st=doc.getDTE().getSignature();
        TestingDTE.CrearDocumentoEnvio(null);
            } catch (KeyStoreException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CertificateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XmlException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLSignatureException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MarshalException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
          /*  try {
            TestingDTE.VerificarDocumentoEnvio("D:/SIITEST/TestDocumentoEnvio.xml");
            } catch (XmlException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

             HashMap<String, String> namespaces = new HashMap<String, String>();
       namespaces.put("", "http://www.sii.cl/SiiDte");
       XmlOptions opts = new XmlOptions();
       opts.setLoadSubstituteNamespaces(namespaces);
       DTEDocument doc=DTEDocument.Factory.parse(new File("D:/SIITEST/TestCrearFactura.xml"),opts);
        SignatureType st=doc.getDTE().getSignature();
       

        }*/


    }
}
