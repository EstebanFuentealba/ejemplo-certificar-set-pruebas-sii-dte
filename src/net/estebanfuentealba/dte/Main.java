/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.estebanfuentealba.dte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author koala
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private static final String URL_CERT = "D:/Walter.pfx";
    private static final String PASS_CERT = "ulw19253";

    public static SSLSocketFactory getFactory(File pKeyFile, String pKeyPassword) throws NoSuchAlgorithmException, KeyStoreException, FileNotFoundException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream keyInput = new FileInputStream(pKeyFile);
        keyStore.load(keyInput, pKeyPassword.toCharArray());
        keyInput.close();

        keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

        return context.getSocketFactory();
    }

    public static String getCookie() throws ScriptException, NoSuchMethodException, MalformedURLException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        SSLContext context = SSLContext.getInstance("SSL");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(URL_CERT), PASS_CERT.toCharArray());
        kmf.init(ks, PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();

        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        context.init(kmf.getKeyManagers(), null, null);

        engine.eval("function getTime(x) { var expira = new Date(); expira.setTime(expira.getTime() + 7200000); return expira.toGMTString(); }");
        Invocable invocable = (Invocable) engine;
        Object object = invocable.invokeFunction("getTime", "x");
        CookieManager cm = new CookieManager();
        URL url = new URL("https://maullin.sii.cl/cvc_cgi/dte/of_solicita_folios");
        HttpsURLConnection httpConn = (HttpsURLConnection) (url).openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.addRequestProperty("Referer", "https://hercules.sii.cl/cgi_AUT2000/autInicio.cgi?referencia=https://maullin.sii.cl/cvc_cgi/dte/of_solicita_folios");
        httpConn.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
        httpConn.setSSLSocketFactory(context.getSocketFactory());
        httpConn.connect();
        cm.storeCookies(httpConn);
        return cm.toString() + " NETSCAPE_LIVEWIRE.locexp=" + object;
    }

    public static String getFolio(String rut, String dv) throws ScriptException, NoSuchMethodException, MalformedURLException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        StringBuffer sb = new StringBuffer();
        SSLContext context = SSLContext.getInstance("SSL");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(URL_CERT), PASS_CERT.toCharArray());
        kmf.init(ks, PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();

        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        context.init(kmf.getKeyManagers(), null, null);
        boolean setCookies = true;

        URL url = new URL("https://maullin.sii.cl/cvc_cgi/dte/of_confirma_folio");
        HttpsURLConnection httpConn = (HttpsURLConnection) (url).openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


        httpConn.addRequestProperty("Referer", "https://hercules.sii.cl/cgi_AUT2000/autInicio.cgi?referencia=https://maullin.sii.cl/cvc_cgi/dte/of_solicita_folios");
        httpConn.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
        httpConn.setSSLSocketFactory(context.getSocketFactory());
        String cookie = Main.getCookie();
        System.out.println(cookie);
        httpConn.addRequestProperty("Cookie", cookie);
        httpConn.getOutputStream().write(("RUT_EMP=" + rut + "&DV_EMP=" + dv + "&FOLIO_INICIAL=0&COD_DOCTO=34&CANT_DOCTOS=1&ACEPTAR=Solicitar Numeraci%F3n").getBytes("UTF-8"));



        InputStream in = httpConn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String rut="76019807-2";
            int numeroDeArchivos = 1;



            String[] data = rut.split("-");
            Folio folio = new Folio();
            String xml =    folio.get(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
                                    data[0],
                                    data[1],
                                    numeroDeArchivos,
                                    URL_CERT,
                                    PASS_CERT
                            );
            System.out.println(xml);
            /*
            cl.nic.dte.net.ConexionSii sii = new cl.nic.dte.net.ConexionSii();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(URL_CERT), PASS_CERT.toCharArray());
            String alias = ks.aliases().nextElement();

            X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
            PrivateKey pKey = (PrivateKey) ks.getKey(alias, PASS_CERT.toCharArray());

            CAF caf = CAF.Factory.newInstance();

            System.out.println(caf.getFRMA().getStringValue());

            String token = sii.getToken(pKey, x509);
            DTEDocument doc = null;
            doc = DTEDocument.Factory.parse(new File(plantillaS));

            IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
            iddoc.setFolio(86424);
            doc.getDTE().getDocumento().setID("N" + iddoc.getFolio());
            //32 FACTURA EXENTA
            iddoc.setTipoDTE(BigInteger.valueOf(32));
            //Primera REferencia
            Referencia referencia = doc.getDTE().getDocumento().getReferenciaArray(0);
            // SET
            referencia.setTpoDocRef("SET");
            // Seteo el Caso
            referencia.setRazonRef("CASO 86424-1");
            // firmo
            doc.getDTE().sign(pKey, x509);


            // Leo la autorizacion y timbro
            // Debo meter el namespace porque SII no lo genera
            HashMap<String, String> namespaces = new HashMap<String, String>();
            namespaces.put("", "http://www.sii.cl/SiiDte");
            XmlOptions opts = new XmlOptions();
            opts.setLoadSubstituteNamespaces(namespaces);


            AutorizacionType auth = AUTORIZACIONDocument.Factory.parse(
            new File(cafS), opts).getAUTORIZACION();

            doc.getDTE().timbrar(auth.getCAF(), auth.getPrivateKey(null));

            // Le doy un formato bonito
            opts = new XmlOptions();
            opts.setSaveImplicitNamespaces(namespaces);
            opts.setLoadSubstituteNamespaces(namespaces);
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(0);

            doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

            // firmo
            doc.getDTE().sign(pKey, x509);

            opts = new XmlOptions();
            opts.setCharacterEncoding("ISO-8859-1");
            opts.setSaveImplicitNamespaces(namespaces);
            doc.save(new File("/home/koala/out.xml"), opts);
             */

            //System.out.println(Utilities.getRutFromCertificate(x509) + " " + token);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
