/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
/**
 *
 * @author Esteban
 */
public class VARIABLES_GLOBALES {
    public static final String URL_CERT     = "D:/ATL/Walter.pfx";
    public static final String PASS_CERT    = "ulw19253";
        public static final String RUT_EMPRESA  = "76019807-2";
        public static final String RUT_CONTRIBUYENTE = "";
    public static final String DIR_CERTIFICATE = "D:/ATL/CERT/";
    public static final boolean DEBUG       = true;

    public static X509Certificate getX509Certificate() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(VARIABLES_GLOBALES.URL_CERT), VARIABLES_GLOBALES.PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();
        System.out.println("Usando certificado " + alias
                        + " del archivo PKCS12: " + VARIABLES_GLOBALES.URL_CERT);

        return (X509Certificate) ks.getCertificate(alias);
    }
    public static PrivateKey getPrivateKey() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(VARIABLES_GLOBALES.URL_CERT), VARIABLES_GLOBALES.PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();
        System.out.println("Usando certificado " + alias
                        + " del archivo PKCS12: " + VARIABLES_GLOBALES.URL_CERT);
        return (PrivateKey) ks.getKey(alias, VARIABLES_GLOBALES.PASS_CERT.toCharArray());
    }
    public static void LOG(String aMostrar) {
        if(VARIABLES_GLOBALES.DEBUG) {
            System.out.println(aMostrar);
        }
    }

}
