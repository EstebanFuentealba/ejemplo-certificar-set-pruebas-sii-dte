/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siidte;

import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.w3c.dom.Node;
/**
 *
 * @author Esteban
 */
public class Folio {

    public static final int FACTURA_ELECTRONICA                         = 33;
    public static final int FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA      = 34;
    public static final int BOLETA_ELECTRONICA_DE_VENTAS_Y_SERV         = 39;
    public static final int BOLETA_ELECTRONICA_DE_VTAS_Y_SERV_EXENTA    = 41;
    public static final int LIQUIDACION_FACTURA_ELECTRONICA             = 43;
    public static final int FACTURA_DE_COMPRA_ELECTRONICA               = 46;
    public static final int GUIA_DE_DESPACHO_ELECTRONICA                = 52;
    public static final int NOTA_DE_DEBITO_ELECTRONICA                  = 56;
    public static final int NOTA_DE_CREDITO_ELECTRONICA                 = 61;
    public static final int FACTURA_DE_EXPORTACION_ELECTRONICA          = 110;
    public static final int NOTA_DE_DEBITO_EXPORTACION_ELECTRONICA      = 111;
    public static final int NOTA_DE_CREDITO_EXPORTACION_ELECTRONICA     = 112;


    private static final String SOLICITA_FOLIOS = "/cvc_cgi/dte/of_solicita_folios";
    private static final String CONFIRMA_FOLIOS = "/cvc_cgi/dte/of_confirma_folio";
    private static final String GENERA_FOLIO    = "/cvc_cgi/dte/of_genera_folio";
    private static final String GENERA_ARCHIVO  = "/cvc_cgi/dte/of_genera_archivo";
    private static final String AMBIENTE        = "https://maullin.sii.cl";
    private String expireTime = "";

    public Folio() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.eval("function getTime(x) { var expira = new Date(); expira.setTime(expira.getTime() + 7200000); return expira.toGMTString(); }");
        Invocable invocable = (Invocable) engine;
        Object object = invocable.invokeFunction("getTime", "x");
        this.expireTime = object.toString();
    }
    public String save(int tipoDocumento, String rutEmpresa, String digitoVerificador, String urlCertificado, String passCertificado, String rutaArchivo) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException, XPathExpressionException {
      return this.get(tipoDocumento, rutEmpresa, digitoVerificador, 1, urlCertificado, passCertificado,SaveAs.File,rutaArchivo);
    }
    public String save(int tipoDocumento, String rutEmpresa, String digitoVerificador,int cantidadDocumentos, String urlCertificado, String passCertificado, String rutaArchivo) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException, XPathExpressionException {
      return this.get(tipoDocumento, rutEmpresa, digitoVerificador, cantidadDocumentos, urlCertificado, passCertificado,SaveAs.File,rutaArchivo);
    }
    public String get(int tipoDocumento, String rutEmpresa, String digitoVerificador, String urlCertificado, String passCertificado) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException, XPathExpressionException {
        return this.get(tipoDocumento, rutEmpresa, digitoVerificador, 1, urlCertificado, passCertificado,SaveAs.Text,"");
    }
    public String get(int tipoDocumento, String rutEmpresa, String digitoVerificador,int cantidadDocumentos, String urlCertificado, String passCertificado) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException, XPathExpressionException {
        return this.get(tipoDocumento, rutEmpresa, digitoVerificador, cantidadDocumentos, urlCertificado, passCertificado,SaveAs.Text,"");
    }
    private String get(int tipoDocumento, String rutEmpresa, String digitoVerificador,int cantidadDocumentos, String urlCertificado, String passCertificado,SaveAs guardarComo,String rutaArchivo) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException, XPathExpressionException {
        //Obtengo Cookies
        System.out.println("[INFO] Connect To SII.");
        HTTPSConexion con = new HTTPSConexion(Folio.AMBIENTE + Folio.SOLICITA_FOLIOS, urlCertificado, passCertificado, true);
        con.setCookiePrefix("NETSCAPE_LIVEWIRE.locexp=" + this.expireTime);

        //Envio La Solicitud
        HTTPSConexion confirma = new HTTPSConexion(Folio.AMBIENTE + Folio.CONFIRMA_FOLIOS, HTTPMethod.POST, "RUT_EMP=" + rutEmpresa + "&DV_EMP=" + digitoVerificador + "&FOLIO_INICIAL=0&COD_DOCTO=" + tipoDocumento + "&CANT_DOCTOS="+cantidadDocumentos+"&ACEPTAR=Solicitar Numeración", urlCertificado, passCertificado, false);
        confirma.setCookieManager(con.getCookieManager());
        confirma.setCookiePrefix(con.getCookiePrefix());
        confirma.addHeader("Referer", con.getHttpConnection().getURL().getProtocol() + "://" + con.getHttpConnection().getURL().getHost() + con.getHttpConnection().getURL().getPath());
        String html = confirma.downloadAsString();
        String paramsGeneraFolio = Utils.getParametersFromHTML(html);
        System.out.println("[INFO] Send Request To SII.");
        System.out.println(html);
        
        //Envio La Solicitud Para Generar
        HTTPSConexion generaFolio = new HTTPSConexion(Folio.AMBIENTE + Folio.GENERA_FOLIO, HTTPMethod.POST, paramsGeneraFolio, urlCertificado, passCertificado, false);
        generaFolio.setCookieManager(con.getCookieManager());
        generaFolio.setCookiePrefix(con.getCookiePrefix());
        generaFolio.addHeader("Referer", confirma.getHttpConnection().getURL().getProtocol() + "://" + confirma.getHttpConnection().getURL().getHost() + confirma.getHttpConnection().getURL().getPath());
        html = generaFolio.downloadAsString();
        String paramsGeneraArchivo = Utils.getParametersFromHTML(html);
        System.out.println("[INFO] Confirm Request To SII.");
        System.out.println(html);
        
        //Descargo Archivo
        HTTPSConexion generaArchivo = new HTTPSConexion(Folio.AMBIENTE + Folio.GENERA_ARCHIVO, HTTPMethod.POST, paramsGeneraArchivo, urlCertificado, passCertificado, false);
        generaArchivo.setCookieManager(con.getCookieManager());
        generaArchivo.setCookiePrefix(con.getCookiePrefix());
        generaArchivo.addHeader("Referer", generaFolio.getHttpConnection().getURL().getProtocol() + "://" + generaFolio.getHttpConnection().getURL().getHost() + generaFolio.getHttpConnection().getURL().getPath());
        System.out.println("[INFO] Get Response From SII.");
        if(guardarComo == SaveAs.File)  {
            return generaArchivo.downloadAsFile(rutaArchivo);
        }
        return generaArchivo.downloadAsString();
    }
}
