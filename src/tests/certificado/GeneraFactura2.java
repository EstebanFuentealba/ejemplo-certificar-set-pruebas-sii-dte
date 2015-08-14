/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.certificado;

/**
 *
 * @author Esteban
 */

import cl.nic.dte.TimbreException;
import cl.nic.dte.VerifyResult;
import cl.nic.dte.examples.VerificaFactura;
import cl.nic.dte.net.ConexionSii;
import cl.nic.dte.net.ConexionSiiException;
import cl.nic.dte.util.Utilities;
import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlOptions;

import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType.Documento.Detalle;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Receptor;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Totales;
import cl.sii.siiDte.DTEDefType.Documento.Referencia;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.MedioPagoType;
import cl.sii.siiDte.RECEPCIONDTEDocument;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.xml.namespace.QName;
import net.estebanfuentealba.dte.TipoDocumento;
import org.apache.xmlbeans.XmlCursor;
import org.xml.sax.SAXException;
import siidte.Example.GeneraEnvio;
import tests.VARIABLES_GLOBALES;

public class GeneraFactura2 {
        private static String PLANTILLA_DOCUMENTO = "C:/Users/Esteban/Documents/My Dropbox/ATL DTE/DTE_OpenLibs/ejemplos/plantilla_documento.xml";
	private static String RESULT_FACTURA = "D:/ATL/CERT/Factura.xml";
        private static String PLANTILLA_ATL = "D:/ATL/plantilla_atl.xml";
        private static String PLANTILLA_ENVIO_ATL="D:/ATL/plantilla_envio_atl.xml";
        private static String RESULTADO_ENVIO = "D:/ATL/CERT/resultado.xml";
        //FOLIO NOTA DE CREDITO ELECTRONICA
        //private static String cafS = "D:/ATL/CERT/FoliosSII7601980761120101162353.xml";
        private static String cafS = "D:/ATL/CERT/FoliosSII7601980734282010117122.xml";

        
    public static void main(String[] args)  {
        //################################################
        //##    CASO   86424-2
        //DOCUMENTO		NOTA DE CREDITO ELECTRONICA
        //SetDePruebas1();

        //################################################
        //##    CASO   86424-2
        //DOCUMENTO		NOTA DE CREDITO ELECTRONICA
        //SetDePruebas2();

        //################################################
        //##    CASO   86424-3
        //DOCUMENTO	FACTURA NO AFECTA O EXENTA ELECTRONICA
        SetDePruebas3();

        

        /*
         * ########################################
         * ENVIADO CON EXITO
         * ########################################
        try {
            

            int folio = 27;
		DTEDocument doc;
		AutorizacionType caf;
		X509Certificate cert;
		PrivateKey key;

                
            // Leo Autorizacion
		// Debo meter el namespace porque SII no lo genera
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		caf = AUTORIZACIONDocument.Factory.parse(new File(cafS), opts)
				.getAUTORIZACION();

		// Construyo base a partir del template
                doc = DTEDocument.Factory.parse(new File(PLANTILLA_ATL), opts);
               
		//doc = DTEDocument.Factory.parse(new File(PLANTILLA_ATL));

		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(VARIABLES_GLOBALES.URL_CERT), VARIABLES_GLOBALES.PASS_CERT.toCharArray());
		String alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + VARIABLES_GLOBALES.URL_CERT);

		cert = (X509Certificate) ks.getCertificate(alias);
		key = (PrivateKey) ks.getKey(alias, VARIABLES_GLOBALES.PASS_CERT.toCharArray());

		// Agrego al doc datos inventados para pruebas
               // IdDoc
		IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
		iddoc.setFolio(folio);
                
                iddoc.setTipoDTE(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
		doc.getDTE().getDocumento().setID("N" + iddoc.getFolio());

		iddoc.xsetFchEmis(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));

		iddoc.setIndServicio(BigInteger.valueOf(3));
		iddoc.setFmaPago(BigInteger.valueOf(1));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 45);
		iddoc.xsetFchCancel(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));

		iddoc.setMedioPago(MedioPagoType.Enum.forString("LT"));
		iddoc.setFmaPago(BigInteger.valueOf(2));


                //REFERENCIA
                 Referencia[] referencias = new Referencia[1];
                referencias[0] = Referencia.Factory.newInstance();
                doc.getDTE().getDocumento().setReferenciaArray(referencias);
                Referencia referencia = doc.getDTE().getDocumento().getReferenciaArray(0);
                referencia.setNroLinRef(2);
                referencia.setTpoDocRef("SET");
                referencia.setRazonRef("CASO 86424-1");
                referencia.setFolioRef("N" + iddoc.getFolio());
                referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));
		// Receptor
		Receptor recp = doc.getDTE().getDocumento().getEncabezado()
				.addNewReceptor();
                recp.setRUTRecep("60803000-K");
		recp.setRznSocRecep("Servicio de Impuestos Internos");
		recp.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
		recp.setContacto("Director Impuestos Internos");
                recp.setDirRecep("Teatinos 120");
		recp.setCmnaRecep("Santiago");
		recp.setCiudadRecep("Santiago");

		// Totales
		Totales tot = doc.getDTE().getDocumento().getEncabezado()
				.addNewTotales();
		//tot.setMntNeto(33900);
                tot.setMntExe(4536);
		tot.setMntTotal(4536);

		// Agrego detalles
		Detalle[] detalle = new Detalle[1];
		detalle[0] = Detalle.Factory.newInstance();
                detalle[0].setNmbItem("HORAS PROGRAMADOR");
                detalle[0].setNroLinDet(1);
                detalle[0].setQtyItem(BigDecimal.valueOf(2));
                detalle[0].setPrcItem(BigDecimal.valueOf(2268));
                detalle[0].setUnmdItem("Hora");
                detalle[0].setMontoItem(4536);

		doc.getDTE().getDocumento().setDetalleArray(detalle);

		// Timbro

		doc.getDTE().timbrar(caf.getCAF(),caf.getPrivateKey(null));

		// antes de firmar le doy formato a los datos
		opts = new XmlOptions();
		opts.setSaveImplicitNamespaces(namespaces);
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);

		// releo el doc para que se reflejen los cambios de formato
		doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

		// firmo
		doc.getDTE().sign(key, cert);

		// Guardo
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
		doc.save(new File(RESULT_FACTURA), opts);

                
                int sucess = 0;
                //##    VERIFICA
                HashMap<String, String> namespacesS = new HashMap<String, String>();
		namespacesS.put("", "http://www.sii.cl/SiiDte");
		XmlOptions optsS = new XmlOptions();
		optsS.setLoadSubstituteNamespaces(namespacesS);

		DTEDocument docS = DTEDocument.Factory.parse(new FileInputStream(
				resultS), optsS);

		VerifyResult resl = docS.getDTE().verifyTimbre();
                if (!resl.isOk()) {
			System.out.println("Documento: Timbre Incorrecto: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Timbre OK");
                        sucess++;
		}

		resl = docS.getDTE().verifyXML();
		if (!resl.isOk()) {
			System.out.println("Documento: Estructura XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Estructura XML OK");
                        sucess++;
		}

		resl = docS.getDTE().verifySignature();
		if (!resl.isOk()) {
			System.out.println("Documento: Firma XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Firma XML OK");
                        sucess++;
		}
                if(sucess==3) {

                String envioFile = GeneraEnvio.Upload(VARIABLES_GLOBALES.URL_CERT,
                                    VARIABLES_GLOBALES.PASS_CERT,
                                    RESULTADO_ENVIO,
                                    recp.getRUTRecep(),
                                    "A17",
                                    PLANTILLA_ENVIO_ATL,
                                    new String[] { resultS }
                );
                ConexionSii con = new ConexionSii();
                String token = con.getToken(key, cert);

		System.out.println("Token: " + token);

		String enviadorS = Utilities.getRutFromCertificate(cert);

		RECEPCIONDTEDocument recps = con.uploadEnvio(enviadorS, VARIABLES_GLOBALES.RUT_EMPRESA,
				new File(envioFile), token);
		System.out.println(recps.xmlText());
            }
        }  catch (UnsupportedOperationException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SOAPException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConexionSiiException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MarshalException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLSignatureException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimbreException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
    public static void SetDePruebas1() {

        int folio = 1;

        //##    REFERENCIAS
        ArrayList<Referencia> referencias = new ArrayList<Referencia>();
            Referencia referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(2);
            referencia.setTpoDocRef("SET");
            referencia.setRazonRef("CASO 86424-1");
            referencia.setFolioRef("N"+folio);
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
        referencias.add(referencia);

        //##    RECEPTOR
        Receptor receptor = Receptor.Factory.newInstance();
        receptor.setRUTRecep("60803000-K");
        receptor.setRznSocRecep("Servicio de Impuestos Internos");
        receptor.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
        receptor.setContacto("Director Impuestos Internos");
        receptor.setDirRecep("Teatinos 120");
        receptor.setCmnaRecep("Santiago");
        receptor.setCiudadRecep("Santiago");

        //##    TOTALES
        Totales totales = Totales.Factory.newInstance();
        totales.setMntExe(4536);
        totales.setMntTotal(4536);

        //##    DETALLES
        ArrayList<Detalle> detalles = new ArrayList<Detalle>();
            Detalle detalle = Detalle.Factory.newInstance();
                detalle.setNmbItem("HORAS PROGRAMADOR");
                detalle.setNroLinDet(1);
                detalle.setQtyItem(BigDecimal.valueOf(2));
                detalle.setPrcItem(BigDecimal.valueOf(2268));
                detalle.setUnmdItem("Hora");
                detalle.setMontoItem(4536);
        detalles.add(detalle);

        //##    GO KOALA
        GeneraFactura2.CreateAndUploadFactura(  TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
                                                referencias,
                                                receptor,
                                                totales,
                                                detalles,
                                                folio);
    }
    public static void SetDePruebas2() {

        int folio = 1;

        //##    REFERENCIAS
        ArrayList<Referencia> referencias = new ArrayList<Referencia>();
            Referencia referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(2);
            referencia.setTpoDocRef("SET");
            referencia.setRazonRef("CASO 86424-2");
            referencia.setFolioRef("N27");
            referencia.setCodRef(BigInteger.valueOf(3));
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
        referencias.add(referencia);

        //##    RECEPTOR
        Receptor receptor = Receptor.Factory.newInstance();
        receptor.setRUTRecep("60803000-K");
        receptor.setRznSocRecep("Servicio de Impuestos Internos");
        receptor.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
        receptor.setContacto("Director Impuestos Internos");
        receptor.setDirRecep("Teatinos 120");
        receptor.setCmnaRecep("Santiago");
        receptor.setCiudadRecep("Santiago");

        //##    TOTALES
        Totales totales = Totales.Factory.newInstance();
        totales.setMntExe(283);
        totales.setMntTotal(283);

        //##    DETALLES
        ArrayList<Detalle> detalles = new ArrayList<Detalle>();
            Detalle detalle = Detalle.Factory.newInstance();
                detalle.setNmbItem("HORAS PROGRAMADOR");
                detalle.setNroLinDet(1);
                detalle.setQtyItem(BigDecimal.valueOf(1));
                detalle.setPrcItem(BigDecimal.valueOf(283));
                detalle.setMontoItem(283);
        detalles.add(detalle);

        //##    GO KOALA
        GeneraFactura2.CreateAndUploadFactura(  TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA,
                                                referencias,
                                                receptor,
                                                totales,
                                                detalles,
                                                folio);
    }
    public static void SetDePruebas3() {

        int folio = 28;

        //##    REFERENCIAS
        ArrayList<Referencia> referencias = new ArrayList<Referencia>();
            Referencia referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(2);
            referencia.setTpoDocRef("SET");
            referencia.setRazonRef("CASO 86424-3");
            referencia.setFolioRef("N"+folio);
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
        referencias.add(referencia);

        //##    RECEPTOR
        Receptor receptor = Receptor.Factory.newInstance();
        receptor.setRUTRecep("60803000-K");
        receptor.setRznSocRecep("Servicio de Impuestos Internos");
        receptor.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
        receptor.setContacto("Director Impuestos Internos");
        receptor.setDirRecep("Teatinos 120");
        receptor.setCmnaRecep("Santiago");
        receptor.setCiudadRecep("Santiago");

        //##    TOTALES
        Totales totales = Totales.Factory.newInstance();
        totales.setMntExe(359687);
        totales.setMntTotal(359687);

        //##    DETALLES
        ArrayList<Detalle> detalles = new ArrayList<Detalle>();
            Detalle detalle = Detalle.Factory.newInstance();
                detalle.setNmbItem("SERV CONSULTORIA FACT ELECTRONICA");
                detalle.setNroLinDet(1);
                detalle.setQtyItem(BigDecimal.valueOf(1));
                detalle.setPrcItem(BigDecimal.valueOf(167888));
                detalle.setMontoItem(167888);
                //AGREGA GO
                detalles.add(detalle);
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("SERV CONSULTORIA GUIA DESPACHO ELECT");
            detalle.setNroLinDet(2);
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(191799));
            detalle.setMontoItem(191799);
                //AGREGA GO
                detalles.add(detalle);

        //##    GO KOALA
        GeneraFactura2.CreateAndUploadFactura(  TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
                                                referencias,
                                                receptor,
                                                totales,
                                                detalles,
                                                folio);
    }
    public static void CreateAndUploadFactura(  int tipoDocumento,
                                                ArrayList<Referencia> referencias,
                                                Receptor receptor,
                                                Totales totales,
                                                ArrayList<Detalle> detalles,
                                                int numeroFolio) {
        try {
            String certS = VARIABLES_GLOBALES.URL_CERT;
            String passS = VARIABLES_GLOBALES.PASS_CERT;
            String resultS = RESULT_FACTURA;
            String plantillaS = PLANTILLA_DOCUMENTO;

            int folio = numeroFolio;
		DTEDocument doc;
		AutorizacionType caf;
		X509Certificate cert;
		PrivateKey key;


            // Leo Autorizacion
		// Debo meter el namespace porque SII no lo genera
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		caf = AUTORIZACIONDocument.Factory.parse(new File(cafS), opts)
				.getAUTORIZACION();

		// Construyo base a partir del template
                doc = DTEDocument.Factory.parse(new File(PLANTILLA_ATL), opts);

		//doc = DTEDocument.Factory.parse(new File(PLANTILLA_ATL));

		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(VARIABLES_GLOBALES.URL_CERT), VARIABLES_GLOBALES.PASS_CERT.toCharArray());
		String alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + VARIABLES_GLOBALES.URL_CERT);

		cert = (X509Certificate) ks.getCertificate(alias);
		key = (PrivateKey) ks.getKey(alias, VARIABLES_GLOBALES.PASS_CERT.toCharArray());

		// Agrego al doc datos inventados para pruebas
               // IdDoc
		IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
		iddoc.setFolio(folio);

                iddoc.setTipoDTE(BigInteger.valueOf(tipoDocumento));
		doc.getDTE().getDocumento().setID("N" + iddoc.getFolio());

		iddoc.xsetFchEmis(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));

		iddoc.setIndServicio(BigInteger.valueOf(3));
		iddoc.setFmaPago(BigInteger.valueOf(1));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 45);
		iddoc.xsetFchCancel(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));

		iddoc.setMedioPago(MedioPagoType.Enum.forString("LT"));
		iddoc.setFmaPago(BigInteger.valueOf(2));


                //REFERENCIA
                doc.getDTE().getDocumento().setReferenciaArray((Referencia[])referencias.toArray(new Referencia[referencias.size()]));

		// Receptor
		doc.getDTE().getDocumento().getEncabezado().setReceptor(receptor);
                
		// Totales
		doc.getDTE().getDocumento().getEncabezado().setTotales(totales);

		// Agrego detalles
		doc.getDTE().getDocumento().setDetalleArray((Detalle[]) detalles.toArray(new Detalle[detalles.size()]));

		// Timbro
		doc.getDTE().timbrar(caf.getCAF(),caf.getPrivateKey(null));

		// antes de firmar le doy formato a los datos
		opts = new XmlOptions();
		opts.setSaveImplicitNamespaces(namespaces);
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);

		// releo el doc para que se reflejen los cambios de formato
		doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

		// firmo
		doc.getDTE().sign(key, cert);

		// Guardo
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
		doc.save(new File(RESULT_FACTURA), opts);


                int sucess = 0;
                //##    VERIFICA
                HashMap<String, String> namespacesS = new HashMap<String, String>();
		namespacesS.put("", "http://www.sii.cl/SiiDte");
		XmlOptions optsS = new XmlOptions();
		optsS.setLoadSubstituteNamespaces(namespacesS);

		DTEDocument docS = DTEDocument.Factory.parse(new FileInputStream(
				resultS), optsS);

		VerifyResult resl = docS.getDTE().verifyTimbre();
                if (!resl.isOk()) {
			System.out.println("Documento: Timbre Incorrecto: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Timbre OK");
                        sucess++;
		}

		resl = docS.getDTE().verifyXML();
		if (!resl.isOk()) {
			System.out.println("Documento: Estructura XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Estructura XML OK");
                        sucess++;
		}

		resl = docS.getDTE().verifySignature();
		if (!resl.isOk()) {
			System.out.println("Documento: Firma XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Firma XML OK");
                        sucess++;
		}
                if(sucess==3) {

                String envioFile = GeneraEnvio.Upload(VARIABLES_GLOBALES.URL_CERT,
                                    VARIABLES_GLOBALES.PASS_CERT,
                                    RESULTADO_ENVIO,
                                    receptor.getRUTRecep(),
                                    "F"+folio+"T"+tipoDocumento,
                                    PLANTILLA_ENVIO_ATL,
                                    new String[] { resultS }
                );
                ConexionSii con = new ConexionSii();
                String token = con.getToken(key, cert);

		System.out.println("Token: " + token);

		String enviadorS = Utilities.getRutFromCertificate(cert);

		RECEPCIONDTEDocument recps = con.uploadEnvio(enviadorS, VARIABLES_GLOBALES.RUT_EMPRESA,
				new File(envioFile), token);
		System.out.println(recps.xmlText());
            }
        }  catch (UnsupportedOperationException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SOAPException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConexionSiiException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MarshalException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLSignatureException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimbreException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GeneraFactura2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
