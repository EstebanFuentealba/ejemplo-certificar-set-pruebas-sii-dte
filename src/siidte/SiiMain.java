/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siidte;

import cl.nic.dte.TimbreException;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDefType.Documento.Detalle;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Emisor;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Receptor;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Totales;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE;
import cl.sii.siiDte.FechaType;
import java.io.File;
import java.io.FileInputStream;
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
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.xml.sax.SAXException;
import tests.VARIABLES_GLOBALES;

/**
 *
 * @author Francisco
 */
public class SiiMain {

    /**
     * @param args the command line arguments
     */
    private static final String URL_CERT = VARIABLES_GLOBALES.URL_CERT;
    private static final String PASS_CERT = VARIABLES_GLOBALES.PASS_CERT;
    private static final String URL_SOURCE = "D:/SIITEST";
    private static final String URL_CAF = "D:/Backup/FoliosSII7601980734520101014225.xml";

    public static void main(String[] args) {
        try {
            try {
                //Genero y guardo el Documento Tributario
          DTEDocument doc = null;
                try {
                    try {
                        doc = getDocument(5);
                    } catch (KeyException ex) {
                        Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MarshalException ex) {
                        Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (XMLSignatureException ex) {
                        Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (KeyStoreException ex) {
                    Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CertificateException ex) {
                    Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnrecoverableKeyException ex) {
                    Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                doc.save(new File(URL_SOURCE+"/dte.xml"));
                try {

             //  EnvioDTEDocument envio=getDocumentoEnvio(new DTEDefType[]{doc.getDTE()});
              // envio.getEnvioDTE().addNewSignature();
              // envio.getEnvioDTE().setSignature(doc.getDTE().getSignature());
                XmlOptions opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		//envio.save(new File(URL_SOURCE+"/envioDte.xml"), opts);


                TestingDTE.VerificaFactura(URL_SOURCE+"/dte.xml");
               // TestingDTE.VerificarDocumentoEnvio(URL_SOURCE+"/envioDte.xml");


                }catch (KeyException ex) {
                    Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } catch (IOException ex) {
                Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);

               
            }
        } catch (XmlException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimbreException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (NoSuchPaddingException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(SiiMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DTEDocument getDocument(int folio) throws XmlException, IOException, TimbreException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException {

       
        PrivateKey key;

        DTEDocument doc;
        AutorizacionType caf;
       
        HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("", "http://www.sii.cl/SiiDte");
        XmlOptions opts = new XmlOptions();
        opts.setLoadSubstituteNamespaces(namespaces);

        caf = AUTORIZACIONDocument.Factory.parse(new File(URL_CAF), opts).getAUTORIZACION();
        doc = DTEDocument.Factory.newInstance(opts);
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(URL_CERT), PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();
        System.out.println("Usando certificado " + alias
                + " del archivo PKCS12: " + URL_CERT);

        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
       // String enviadorS = Utilities.getRutFromCertificate(x509);
        PrivateKey pKey = (PrivateKey) ks.getKey(alias, PASS_CERT.toCharArray());
        doc.addNewDTE();
        doc.getDTE().addNewDocumento();
        doc.getDTE().setVersion(BigDecimal.valueOf(1));
        doc.getDTE().getDocumento().addNewEncabezado();
        IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
        iddoc.setFolio(folio);
        doc.getDTE().getDocumento().setID("N" + iddoc.getFolio());
        iddoc.setTipoDTE(BigInteger.valueOf(34));

        iddoc.xsetFchEmis(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));


        Emisor emisor = doc.getDTE().getDocumento().getEncabezado().addNewEmisor();
        emisor.setRUTEmisor("76019807-2");
        emisor.setRznSoc("Sociedad de profesionales ");
        emisor.setCiudadOrigen("CONCEPCION");
        emisor.setGiroEmis("ASESORIA Y CONSULTORIA TRIBUTARIA LABORAL Y CONTABLE");
        emisor.setActecoArray(new int[]{724000});


        Receptor recp = doc.getDTE().getDocumento().getEncabezado().addNewReceptor();
        recp.setRUTRecep("76006822-5");
        recp.setRznSocRecep("Servicio de Impuestos Internos");
        recp.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
        recp.setContacto("Director Impuestos Internos");
        recp.setDirRecep("Teatinos 120");
        recp.setCmnaRecep("Santiago");
        recp.setCiudadRecep("Santiago");

        // Totales
        Totales tot = doc.getDTE().getDocumento().getEncabezado().addNewTotales();
        //tot.setMntNeto(33900);
        tot.setMntExe(40341);
        tot.setTasaIVA(BigDecimal.valueOf(19));
        tot.setIVA(6441);
        tot.setMntTotal(40341);

        // Agrego detalles
        Detalle[] det = new Detalle[2];
        det[0] = Detalle.Factory.newInstance();
        det[0].setNroLinDet(1);
        det[0].setNmbItem("dominio sii");
        det[0].setQtyItem(BigDecimal.valueOf(1));
        det[0].setPrcItem(BigDecimal.valueOf(16949.584));
        det[0].setMontoItem(16950);

        det[1] = Detalle.Factory.newInstance();
        det[1].setNroLinDet(1);
        det[1].setNmbItem("dominio impuestosinternos");
        det[1].setQtyItem(BigDecimal.valueOf(1));
        det[1].setPrcItem(BigDecimal.valueOf(16949.584));
        det[1].setMontoItem(16950);
        doc.getDTE().getDocumento().setDetalleArray(det);
        doc.getDTE().timbrar(caf.getCAF(), caf.getPrivateKey(null));
        opts = new XmlOptions();

        opts.setSaveImplicitNamespaces(namespaces);
        opts.setLoadSubstituteNamespaces(namespaces);
        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(0);
        doc.getDTE().sign(pKey, x509);


      //  doc.getDTE().sign(pkey, x509);
        // releo el doc para que se reflejen los cambios de formato
        doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

        return doc;


    }

    public static EnvioDTEDocument getDocumentoEnvio(DTEDefType dtes[]) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidAlgorithmParameterException, KeyException, MarshalException, SAXException, XMLSignatureException, ParserConfigurationException, XmlException {

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

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(URL_CERT), PASS_CERT.toCharArray());
        String alias = ks.aliases().nextElement();
        System.out.println("Usando certificado " + alias
                + " del archivo PKCS12: " + URL_CERT);

        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        String enviadorS = Utilities.getRutFromCertificate(x509);
        PrivateKey pKey = (PrivateKey) ks.getKey(alias, PASS_CERT.toCharArray());

        envio.getEnvioDTE().getSetDTE().setID("EnviaoN" + 5);

        envio.getEnvioDTE().getSetDTE().addNewCaratula();
        cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula car = envio.getEnvioDTE().getSetDTE().getCaratula();


        // documentos a enviar
        HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("", "http://www.sii.cl/SiiDte");
        XmlOptions opts = new XmlOptions();
        opts.setLoadSubstituteNamespaces(namespaces);
        HashMap<Integer, Integer> hashTot = new HashMap<Integer, Integer>();

        for (int i = 0; i < dtes.length; i++) {
            // armar hash para totalizar por tipoDTE
            if (hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue()) != null) {
                hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue(), hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue()) + 1);
            } else {
                hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue(), 1);
            }
        }

        SubTotDTE[] subtDtes = new SubTotDTE[hashTot.size()];
        int i = 0;
        for (Integer tipo : hashTot.keySet()) {
            SubTotDTE subt = SubTotDTE.Factory.newInstance();
            subt.setTpoDTE(new BigInteger(tipo.toString()));
            subt.setNroDTE(new BigInteger(hashTot.get(tipo).toString()));
            subtDtes[i] = subt;
            i++;
        }

        car.setSubTotDTEArray(subtDtes);

		// Le doy un formato bonito (debo hacerlo antes de firmar para no
		// afectar los DTE internos)
		opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		envio = EnvioDTEDocument.Factory.parse(envio.newInputStream(opts));

		envio.getEnvioDTE().getSetDTE().setDTEArray(dtes);

		// firmo
		//envio.sign(pKey, x509);

		






        return envio;
    }
}
