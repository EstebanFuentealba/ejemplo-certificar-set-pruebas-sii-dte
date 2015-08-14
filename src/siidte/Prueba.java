/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siidte;

import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE;
import cl.sii.siiDte.FechaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import tests.VARIABLES_GLOBALES;

/**
 *
 * @author Francisco
 */
public class Prueba {

    private static final String URL_CERT = VARIABLES_GLOBALES.URL_CERT;
    private static final String PASS_CERT = VARIABLES_GLOBALES.PASS_CERT;
    private static final String URL_SOURCE = "D:/SIITEST";
    private static final String URL_CAF = "D:/Backup/FoliosSII7601980734520101014225.xml";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws KeyStoreException, FileNotFoundException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, XmlException {
        // TODO code application logic here


        // generar documento de envio

        X509Certificate cert;
        PrivateKey key;
        EnvioDTEDocument envio = EnvioDTEDocument.Factory.newInstance();
        envio.addNewEnvioDTE();
        envio.getEnvioDTE().addNewSetDTE();
        envio.getEnvioDTE().setVersion(BigDecimal.valueOf(1));
        XmlCursor cursor = envio.newCursor();
        if (cursor.toFirstChild()) {
            cursor.setAttributeText(new QName(
                    "http://www.w3.org/2001/XMLSchema-instance",
                    "schemaLocation"),
                    "http://www.sii.cl/SiiDte EnvioDTE_v10.xsd");
        }

        HashMap<String, String> namespaces1 = new HashMap<String, String>();
        namespaces1.put("", "http://www.sii.cl/SiiDte");
        XmlOptions opts1 = new XmlOptions();
        opts1.setLoadSubstituteNamespaces(namespaces1);
        DTEDocument doc;
        AutorizacionType caf;
        doc = DTEDocument.Factory.parse(new File(URL_SOURCE + "/TestCrearFactura.xml"), opts1);


        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(URL_CERT), PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();
        System.out.println("Usando certificado " + alias
                + " del archivo PKCS12: " + URL_CERT);

        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        String enviadorS = Utilities.getRutFromCertificate(x509);
        PrivateKey pKey = (PrivateKey) ks.getKey(alias, PASS_CERT.toCharArray());

        envio.getEnvioDTE().getSetDTE().setID("EnviaoN" + 5);
        SubTotDTE[] subtDtes = new SubTotDTE[1];

        SubTotDTE subt = SubTotDTE.Factory.newInstance();

        subt.setTpoDTE(BigInteger.valueOf(34));
        subt.setNroDTE(BigInteger.valueOf(5));
        subtDtes[0] = subt;
        envio.getEnvioDTE().getSetDTE().addNewCaratula();
        cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula car = envio.getEnvioDTE().getSetDTE().getCaratula();
        car.setVersion(BigDecimal.valueOf(1));
        car.setNroResol(5);
        car.setSubTotDTEArray(subtDtes);
        car.setRutEmisor("76019807-2");
        car.setRutEnvia("14213228-1");
        car.setRutReceptor("76006822-5");
        car.setFchResol(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())).calendarValue());


        envio.getEnvioDTE().getSetDTE().setDTEArray(new DTEDefType[] {doc.getDTE()});
        // documentos a enviar
        HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("", "http://www.sii.cl/SiiDte");
        XmlOptions opts = new XmlOptions();
        opts.setLoadSubstituteNamespaces(namespaces);
        HashMap<Integer, Integer> hashTot = new HashMap<Integer, Integer>();
        // Le doy un formato bonito (debo hacerlo antes de firmar para no
        // afectar los DTE internos)
        opts = new XmlOptions();
        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(0);
        envio = EnvioDTEDocument.Factory.parse(envio.newInputStream(opts));
        //envio.getEnvioDTE().getSetDTE().setDTEArray(dtes);




        //


        envio.getEnvioDTE().addNewSignature();
        envio.getEnvioDTE().setSignature(doc.getDTE().getSignature());




        envio.save(new File(URL_SOURCE + "/archivoe.xml"));

        TestingDTE.VerificarDocumentoEnvio(URL_SOURCE + "/archivoe.xml");















        //generar factura




    }
}
