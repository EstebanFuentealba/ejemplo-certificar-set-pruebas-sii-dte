/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.estebanfuentealba.dte.certificate;

import cl.nic.dte.TimbreException;
import cl.nic.dte.VerifyResult;
import cl.nic.dte.net.ConexionSii;
import cl.nic.dte.net.ConexionSiiException;
import cl.nic.dte.util.Utilities;

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
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.MedioPagoType;
import cl.sii.siiDte.RECEPCIONDTEDocument;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument;
import com.Ostermiller.util.MD5;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.xml.namespace.QName;
import net.estebanfuentealba.dte.TipoDocumento;
import org.apache.xmlbeans.XmlCursor;
import org.xml.sax.SAXException;
import siidte.Example.CreaFactura;
import siidte.Example.GeneraEnvio;
import tests.VARIABLES_GLOBALES;

/**
 *
 * @author EstebanFuentealba
 */
public class CertificateSET {
        private static String PLANTILLA_DOCUMENTO = "C:/Users/Esteban/Documents/My Dropbox/ATL DTE/DTE_OpenLibs/ejemplos/plantilla_documento.xml";
	private static String RESULT_FACTURA = "D:/ATL/CERT/Factura.xml";
        private static String RESULT_LIBRO_CV = "D:/ATL/CERT/LibroCV.xml";
        private static String PLANTILLA_ATL = "D:/ATL/plantilla_atl.xml";
        private static String PLANTILLA_ENVIO_ATL="D:/ATL/plantilla_envio_atl.xml";
        private static String PLANTILLA_LIBRO_ATL = "D:/ATL/plantilla_libro_atl.xml";
        private static String RESULTADO_ENVIO = "D:/ATL/CERT/resultado.xml";
        private static String cafS = "D:/ATL/CERT/FoliosSII76019807342920101191156.xml";
        private static int folioCaso86424_1 = 44; //OK
            /* exenta */
            //OK
            private static int folioCaso86424_2 = 41; //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            private static int folioCaso86424_3 = 45; //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* exenta */
            //OK
            private static int folioCaso86424_4 = 42; //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            private static int folioCaso86424_5 = 13;
            /* exenta */
            //OK
            /* credito */
            //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* debito */
            private static int folioCaso86424_6 = 46; //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* debito */
            /* exenta */
            //OK
            private static int folioCaso86424_7 = 43; //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* exenta */
            //OK
            /* credito */
            //OK
            /* debito */
            /* exenta */
            //OK
            /* credito */
            //OK
            private static int folioCaso86424_8 = 14; //OK
        
        public static void main(String[] args)  {

        try {
            /*
            ################################################
            ##      CASO   86424-2
            ##      DOCUMENTO		NOTA DE CREDITO ELECTRONICA
             *
             */
            //CertificateSET.SetDePruebas1(27);  //Aceptados con Reparo
            /*
            ################################################
            ##      CASO   86424-2
            ##      DOCUMENTO		NOTA DE CREDITO ELECTRONICA
             *
             */
            //CertificateSET.SetDePruebas2(1);  //Rechazados
            /*
            ################################################
            ##      CASO   86424-3
            ##      DOCUMENTO	FACTURA NO AFECTA O EXENTA ELECTRONICA
             *
             */
            //CertificateSET.SetDePruebas3(28);    //Aceptados con Reparo
            /*
             * TODO CERTIFICADO
             */
            ArrayList<String> rutasFacturas = new ArrayList<String>();
            
            int NroLinRef = 1;
            Totales totales = null;
            ArrayList<Referencia> referencias;
            Referencia referencia;
            Receptor receptor;
            ArrayList<Detalle> detalles;
            Detalle detalle;
            String rutaPrimeraFactura;
            String cafFolioFacturaExenta1 = "D:/ATL/CERT/FoliosSII76019807344420101112196.xml";
            String cafFolioNotaDeCredito2 = "D:/ATL/CERT/FoliosSII760198076141201011121913.xml";
            String cafFolioNotaDeDebito = "D:/ATL/CERT/FoliosSII760198075613201011121914.xml"; //DEBITO 3
            /*
            try {
            //##    RECEPTOR
            receptor = Receptor.Factory.newInstance();
            receptor.setRUTRecep("60803000-K");
            receptor.setRznSocRecep("Servicio de Impuestos Internos");
            receptor.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
            receptor.setContacto("Director Impuestos Internos");
            receptor.setDirRecep("Teatinos 120");
            receptor.setCmnaRecep("Santiago");
            receptor.setCiudadRecep("Santiago");
            //
            // #################################################################
            // ##   Caso 86424_1
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntExe(4536);
            totales.setMntTotal(4536);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("HORAS PROGRAMADOR");
            detalle.setNroLinDet(1);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setQtyItem(BigDecimal.valueOf(2));
            detalle.setPrcItem(BigDecimal.valueOf(2268));
            detalle.setUnmdItem("Hora");
            detalle.setMontoItem(4536);
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(   TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_1,
            cafFolioFacturaExenta1
            );
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_2
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(1);
            referencia.setTpoDocRef(String.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            referencia.setRazonRef("MODIFICA MONTO");
            referencia.setFolioRef(String.valueOf(folioCaso86424_1));
            referencia.setCodRef(BigInteger.valueOf(3));
            //FechaRef: Es la fecha de emisión del documento al que se hace referencia, el formato de la fecha es “AAAA-MM-DD” (año, mes, día), por ejemplo:
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
            
            referencias.add(referencia);
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntNeto(0);
            totales.setIVA(0);
            totales.setMntExe(566);
            totales.setMntTotal(566);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNroLinDet(1);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setQtyItem(BigDecimal.valueOf(2));
            detalle.setPrcItem(BigDecimal.valueOf(283));
            detalle.setNmbItem("HORAS PROGRAMADOR");
            detalle.setMontoItem(566);
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_2,
            cafFolioNotaDeCredito2);
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_3
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntExe(359687);
            totales.setMntTotal(359687);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
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
            rutaPrimeraFactura = crearFactura(  TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_3,
            cafFolioFacturaExenta1);
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_4
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(1);
            referencia.setTpoDocRef(String.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            referencia.setRazonRef("CORRIGE GIRO");
            referencia.setFolioRef(String.valueOf(folioCaso86424_3));
            referencia.setCodRef(BigInteger.valueOf(2));
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
            referencias.add(referencia);
            //##    CORRIGE GIRO
            receptor.setGiroRecep("Nuevo Giro");
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntNeto(0);
            totales.setIVA(0);
            totales.setMntExe(0);
            totales.setMntTotal(0);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("SERV CONSULTORIA FACT ELECTRONICA");
            detalle.setNroLinDet(1);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setMontoItem(0);
            //AGREGA GO
            detalles.add(detalle);
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("SERV CONSULTORIA GUIA DESPACHO ELECT");
            detalle.setNroLinDet(2);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setMontoItem(0);
            //AGREGA GO
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(  TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_4,
            cafFolioNotaDeCredito2
            );
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_5
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(1);
            referencia.setTpoDocRef(String.valueOf(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA));
            referencia.setRazonRef("ANULA NOTA DE CREDITO ELECTRONICA");
            referencia.setFolioRef(String.valueOf(folioCaso86424_4));
            referencia.setCodRef(BigInteger.valueOf(1));
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
            referencias.add(referencia);
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntNeto(0);
            totales.setIVA(0);
            totales.setMntExe(0);
            totales.setMntTotal(0);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("SERV CONSULTORIA FACT ELECTRONICA");
            detalle.setNroLinDet(1);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setMontoItem(0);
            //AGREGA GO
            detalles.add(detalle);
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("SERV CONSULTORIA GUIA DESPACHO ELECT");
            detalle.setNroLinDet(2);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setMontoItem(0);
            //AGREGA GO
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(  TipoDocumento.NOTA_DE_DEBITO_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_5,
            cafFolioNotaDeDebito
            );
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_6
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntExe(435042);
            totales.setMntTotal(435042);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("CAPACITACION USO CIGUEÑALES");
            detalle.setNroLinDet(1);
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(267859));
            detalle.setMontoItem(267859);
            //AGREGA GO
            detalles.add(detalle);
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("CAPACITACION USO PLC's CNC");
            detalle.setNroLinDet(2);
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(167183));
            detalle.setMontoItem(167183);
            //AGREGA GO
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(  TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_6,
            cafFolioFacturaExenta1);
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_7
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(1);
            referencia.setTpoDocRef(String.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            referencia.setRazonRef("MODIFICA MONTO");
            referencia.setFolioRef(String.valueOf(folioCaso86424_6));
            referencia.setCodRef(BigInteger.valueOf(3));
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
            referencias.add(referencia);
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntNeto(0);
            totales.setIVA(0);
            totales.setMntExe(301113);
            totales.setMntTotal(301113);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("CAPACITACION USO CIGUEÑALES");
            detalle.setNroLinDet(1);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(133930));
            detalle.setMontoItem(133930);
            //AGREGA GO
            detalles.add(detalle);
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("CAPACITACION USO PLC's CNC");
            detalle.setNroLinDet(2);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(167183));
            detalle.setMontoItem(167183);
            //AGREGA GO
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_7,
            cafFolioNotaDeCredito2);
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //
            // #################################################################
            // ##   Caso 86424_8
            // #################################################################
            //
            //##    REFERENCIAS
            referencias = new ArrayList<Referencia>();
            referencia = Referencia.Factory.newInstance();
            referencia.setNroLinRef(NroLinRef);
            referencia.setTpoDocRef(String.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            referencia.setRazonRef("MODIFICA MONTO");
            referencia.setFolioRef(String.valueOf(folioCaso86424_6));
            referencia.setCodRef(BigInteger.valueOf(3));
            referencia.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(new Date())));
            referencias.add(referencia);
            //##    TOTALES
            totales = Totales.Factory.newInstance();
            totales.setMntNeto(0);
            totales.setIVA(0);
            totales.setMntExe(301296);
            totales.setMntTotal(301296);
            //##    DETALLES
            detalles = new ArrayList<Detalle>();
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("CAPACITACION USO CIGUEÑALES");
            detalle.setNroLinDet(1);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(267859));
            detalle.setMontoItem(267859);
            //AGREGA GO
            detalles.add(detalle);
            detalle = Detalle.Factory.newInstance();
            detalle.setNmbItem("CAPACITACION USO PLC's CNC");
            detalle.setNroLinDet(2);
            detalle.setIndExe(BigInteger.valueOf(1));
            detalle.setQtyItem(BigDecimal.valueOf(1));
            detalle.setPrcItem(BigDecimal.valueOf(33437));
            detalle.setMontoItem(33437);
            //AGREGA GO
            detalles.add(detalle);
            rutaPrimeraFactura = crearFactura(TipoDocumento.NOTA_DE_DEBITO_ELECTRONICA,
            referencias,
            receptor,
            totales,
            detalles,
            folioCaso86424_8,
            cafFolioNotaDeDebito);
            if(rutaPrimeraFactura!=null)
            rutasFacturas.add(rutaPrimeraFactura);
            //################################
            //##    UPLOAD
            if(rutasFacturas.size()>0) {
            String id = "A21";
            String uploadFileResult = uploadFacturas( VARIABLES_GLOBALES.URL_CERT,
            VARIABLES_GLOBALES.PASS_CERT,
            RESULTADO_ENVIO,
            receptor.getRUTRecep(),
            id,
            PLANTILLA_ENVIO_ATL, rutasFacturas);
            VARIABLES_GLOBALES.LOG(uploadFileResult);
            } else {
            VARIABLES_GLOBALES.LOG("ERROR NIVEL DE FACTURA");
            }
            } catch (ClientProtocolException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XmlException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimbreException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SignatureException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyStoreException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CertificateException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MarshalException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLSignatureException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
            }
             */
            crearLibroCompraVenta();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MarshalException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLSignatureException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String crearFactura( int tipoDocumento,
                                ArrayList<Referencia> referencias,
                                Receptor receptor,
                                Totales totales,
                                ArrayList<Detalle> detalles,
                                int numeroFolio,
                                String cafFile) throws XmlException, IOException, TimbreException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException {

                String resultSave = "ResultadoFactura-"+(new Date()).getTime()+"-"+Math.round((Math.random()*1000))+".xml";
                
                // Leo Autorizacion
		// Debo meter el namespace porque SII no lo genera
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		AutorizacionType caf = AUTORIZACIONDocument.Factory.parse(new File(cafFile), opts)
				.getAUTORIZACION();

		// Construyo base a partir del template
                DTEDocument doc = DTEDocument.Factory.parse(new File(PLANTILLA_ATL), opts);

		//doc = DTEDocument.Factory.parse(new File(PLANTILLA_ATL));

		//KEY Y CERTIFICADO
                PrivateKey key          = VARIABLES_GLOBALES.getPrivateKey();
                X509Certificate cert    = VARIABLES_GLOBALES.getX509Certificate();

                
		// Agrego al doc datos inventados para pruebas
               // IdDoc
		IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
		iddoc.setFolio(numeroFolio);

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
                if(referencias.size()>0)
                    doc.getDTE().getDocumento().setReferenciaArray((Referencia[])referencias.toArray(new Referencia[referencias.size()]));

		// Receptor
		doc.getDTE().getDocumento().getEncabezado().setReceptor(receptor);

		// Totales
                if(totales != null)
                    doc.getDTE().getDocumento().getEncabezado().setTotales(totales);
                
		// Agrego detalles
                if(detalles.size()>0)
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
		doc.save(new File(resultSave), opts);

                //##    VERIFICA
                HashMap<String, String> namespacesS = new HashMap<String, String>();
		namespacesS.put("", "http://www.sii.cl/SiiDte");
		XmlOptions optsS = new XmlOptions();
		optsS.setLoadSubstituteNamespaces(namespacesS);

                DTEDocument docS = DTEDocument.Factory.parse(new FileInputStream(
				resultSave), optsS);

                int success = 0;
		VerifyResult resl = docS.getDTE().verifyTimbre();
                if (!resl.isOk()) {
			VARIABLES_GLOBALES.LOG("Documento: Timbre Incorrecto: "
					+ resl.getMessage());
		} else {
			VARIABLES_GLOBALES.LOG("Documento: Timbre OK");
                        success++;
		}

		resl = docS.getDTE().verifyXML();
		if (!resl.isOk()) {
			VARIABLES_GLOBALES.LOG("Documento: Estructura XML Incorrecta: "
					+ resl.getMessage());
		} else {
			VARIABLES_GLOBALES.LOG("Documento: Estructura XML OK");
                        success++;
		}

		resl = docS.getDTE().verifySignature();
		if (!resl.isOk()) {
			VARIABLES_GLOBALES.LOG("Documento: Firma XML Incorrecta: "
					+ resl.getMessage());
		} else {
			VARIABLES_GLOBALES.LOG("Documento: Firma XML OK");
                        success++;
		}
                if(success==3) {
                    return resultSave;
                }
        return null;
    }
    public static String uploadFacturas(String urlCertificado,
                                    String passwordCertificado,
                                    String rutaResultadoUpload,
                                    String rutReceptor,
                                    String id,
                                    String rutaPlantillaUpload,
                                    ArrayList<String> rutaFacturas) throws ClientProtocolException, IOException, ParseException, XmlException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, Exception {
         /*
          * Genera Archivo XML de envio
          */
         String envioFile = GeneraEnvio.Upload(urlCertificado,
                                    passwordCertificado,
                                    rutaResultadoUpload,
                                    rutReceptor,
                                    id,
                                    rutaPlantillaUpload,
                                    (String[])rutaFacturas.toArray(new String[rutaFacturas.size()])
                );
            ConexionSii con = new ConexionSii();
            //KEY Y CERTIFICADO
            PrivateKey key          = VARIABLES_GLOBALES.getPrivateKey();
            X509Certificate cert    = VARIABLES_GLOBALES.getX509Certificate();
            String token = con.getToken(key, cert);
            VARIABLES_GLOBALES.LOG("Token: " + token);

            String enviadorS = Utilities.getRutFromCertificate(cert);

            RECEPCIONDTEDocument recps = con.uploadEnvio(enviadorS, VARIABLES_GLOBALES.RUT_EMPRESA,
                            new File(envioFile), token);
            VARIABLES_GLOBALES.LOG(recps.xmlText());

        return recps.xmlText();
    }
    public static void crearLibroCompraVenta() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyException, MarshalException, XMLSignatureException, SAXException, IOException, ParserConfigurationException, XmlException, KeyStoreException, CertificateException, UnrecoverableKeyException {
        
            LibroCompraVentaDocument libroDocument = LibroCompraVentaDocument.Factory.parse(new FileInputStream(PLANTILLA_LIBRO_ATL));
            XmlCursor cursor = libroDocument.newCursor();
            if (cursor.toFirstChild()) {
                cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"), "http://www.sii.cl/SiiDte LibroCV_v10.xsd");
            }
            libroDocument.getLibroCompraVenta().setVersion(new BigDecimal("1.0"));
            Random r = new Random();
            libroDocument.getLibroCompraVenta().getEnvioLibro().setID("EnvioLbr" + String.valueOf((-1 * r.nextInt()) % 1000000));
            libroDocument.getLibroCompraVenta().getEnvioLibro().getCaratula().setCodAutRec("");
            //agregas seccion de  Resumen y totales
            libroDocument.getLibroCompraVenta().getEnvioLibro().addNewResumenPeriodo();
            //....
            // Llenas los totales
            //...
            LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo.TotalesPeriodo totalesPeriodo = null;
            //##    TOTAL FACTURA EXENTA O NO AFECTA
            totalesPeriodo = libroDocument.getLibroCompraVenta().getEnvioLibro().getResumenPeriodo().addNewTotalesPeriodo();
            totalesPeriodo.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            totalesPeriodo.setTotDoc(3);
            //##    TOTAL MONTO EXENTO
            totalesPeriodo.setTotMntExe(1402240);
            totalesPeriodo.setTotMntNeto(0);
            totalesPeriodo.setTotMntTotal(1402240);
            // Agregas una seccion de detalle
            libroDocument.getLibroCompraVenta().getEnvioLibro().addNewDetalle();
            //....
            // Llenas los detalles
            //...
            ArrayList<LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle> detalles = new ArrayList<LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle>();
            LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle detalle = null;
            //##    [1] FACTURA NO AFECTA O EXENTA ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA)); //?
            detalle.setNroDoc(folioCaso86424_1);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            detalle.setMntExe(4536);
            detalle.setMntNeto(0);
            detalle.setMntTotal(4536);
            //## AGREGA
            detalles.add(detalle);
            //##    [2] NOTA DE CREDITO ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_2);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            
            detalle.setMntExe(566);
            detalle.setMntTotal(566);
            //## AGREGA
            detalles.add(detalle);
            //##    [3] FACTURA NO AFECTA O EXENTA ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_3);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            
            detalle.setMntExe(359687);
            detalle.setMntTotal(359687);
            //## AGREGA
            detalles.add(detalle);
            //##    [4] NOTA DE CREDITO ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_4);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));

            detalle.setMntExe(0);
            detalle.setMntTotal(0);
            //## AGREGA
            detalles.add(detalle);
            //##    [5] NOTA DE DEBITO ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            //##    REFERENCIAS
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_DEBITO_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_5);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            
            detalle.setMntExe(0);
            detalle.setAnulado(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Anulado.Enum.forString("A"));
            detalle.setMntTotal(0);
            //## AGREGA
            detalles.add(detalle);
            //##    [6] FACTURA NO AFECTA O EXENTA ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_6);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            
            detalle.setMntExe(435042);
            detalle.setMntTotal(435042);
            //## AGREGA
            detalles.add(detalle);
            //##    [7] NOTA DE CREDITO ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_7);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            
            detalle.setMntExe(301113);
            detalle.setMntTotal(301113);
            //## AGREGA
            detalles.add(detalle);
            //##    [8] NOTA DE DEBITO ELECTRONICA
            detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
            detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_DEBITO_ELECTRONICA));
            detalle.setNroDoc(folioCaso86424_8);
            detalle.xsetFchDoc(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.FchDoc.Factory.newValue("2010-11-12"));
            
            detalle.setMntExe(301296);
            detalle.setMntTotal(301296);
            //## AGREGA
            detalles.add(detalle);
            //Cargas los detalles al libro
            libroDocument.getLibroCompraVenta().getEnvioLibro().setDetalleArray(detalles.toArray(new LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle[detalles.size()]));
            //Arreglas
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(0);
            opts.setCharacterEncoding("ISO-8859-1");
            //Firmas
            libroDocument.sign(VARIABLES_GLOBALES.getPrivateKey(), VARIABLES_GLOBALES.getX509Certificate());
            // Grabas
            opts = new XmlOptions();
            opts.setCharacterEncoding("ISO-8859-1");
            libroDocument.save(new File(CertificateSET.RESULT_LIBRO_CV), opts);
        

    }
  /*
        public static void crearLibroCompraVenta() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyException, MarshalException, XMLSignatureException, SAXException, IOException, ParserConfigurationException, XmlException, KeyStoreException, CertificateException, UnrecoverableKeyException {
        try {
        LibroCompraVentaDocument libroDocument = LibroCompraVentaDocument.Factory.parse(new FileInputStream(PLANTILLA_LIBRO_ATL));
        XmlCursor cursor = libroDocument.newCursor();
        if (cursor.toFirstChild()) {
        cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"), "http://www.sii.cl/SiiDte LibroCV_v10.xsd");
        }
        libroDocument.getLibroCompraVenta().setVersion(new BigDecimal("1.0"));
        Random r = new Random();
        libroDocument.getLibroCompraVenta().getEnvioLibro().setID("EnvioLbr" + String.valueOf((-1 * r.nextInt()) % 1000000));
        libroDocument.getLibroCompraVenta().getEnvioLibro().getCaratula().setCodAutRec("");
        //agregas seccion de  Resumen y totales
        libroDocument.getLibroCompraVenta().getEnvioLibro().addNewResumenPeriodo();
        //....
        // Llenas los totales
        //...
        LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo.TotalesPeriodo totalesPeriodo = null;
        //##    TOTAL FACTURA EXENTA O NO AFECTA
        totalesPeriodo = libroDocument.getLibroCompraVenta().getEnvioLibro().getResumenPeriodo().addNewTotalesPeriodo();
        totalesPeriodo.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
        totalesPeriodo.setTotDoc(2);
        //##    TOTAL MONTO EXENTO
        totalesPeriodo.setTotMntExe(67514);
        totalesPeriodo.setTotMntTotal(67514);
        totalesPeriodo = libroDocument.getLibroCompraVenta().getEnvioLibro().getResumenPeriodo().addNewTotalesPeriodo();
        totalesPeriodo.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA));
        totalesPeriodo.setTotDoc(1);
        //##    TOTAL MONTO NETO
        totalesPeriodo.setTotMntNeto(15645);
        //##    TOTAL MONTO IVA
        totalesPeriodo.setTotMntIVA(2972);
        //##    TOTAL MONTO
        totalesPeriodo.setTotMntTotal(81215);
        // Agregas una seccion de detalle
        libroDocument.getLibroCompraVenta().getEnvioLibro().addNewDetalle();
        //....
        // Llenas los detalles
        //...
        ArrayList<LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle> detalles = new ArrayList<LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle>();
        LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle detalle = null;
        //##    FACTURA
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA)); //?
        detalle.setNroDoc(234);
        detalle.setMntExe(0);
        detalle.setMntNeto(10750);
        detalle.setTasaImp(BigDecimal.valueOf(19));
        detalle.setTpoImp(1);
        detalle.setMntIVA(2042);
        detalle.setMntTotal(12792);
        //## AGREGA
        detalles.add(detalle);
        //##    FACTURA ELECTRONICA
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_ELECTRONICA));
        detalle.setNroDoc(32);
        detalle.setMntExe(8253);
        detalle.setMntNeto(4895);
        detalle.setTasaImp(BigDecimal.valueOf(19));
        detalle.setTpoImp(1);
        detalle.setMntIVA(2498);
        detalle.setMntTotal(15646);
        //## AGREGA
        detalles.add(detalle);
        //##    FACTURA EXENTA ELECTRONICA
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
        detalle.setNroDoc(29658);
        detalle.setMntExe(29658);
        detalle.setMntTotal(29658);
        //## AGREGA
        detalles.add(detalle);
        //##    FACTURA EXENTA
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));
        detalle.setNroDoc(45);
        detalle.setMntExe(29603);
        detalle.setMntTotal(29603);
        //## AGREGA
        detalles.add(detalle);
        //##    NOTA DE CREDITO
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        //##    REFERENCIAS
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_CREDITO));
        detalle.setFolioDocRef(234);
        detalle.setNroDoc(451);
        detalle.setMntExe(2650);
        //## AGREGA
        detalles.add(detalle);
        //##    NOTA DE CREDITO ELECTRONICA
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA));
        detalle.setNroDoc(67);
        detalle.setFolioDocRef(32);
        detalle.setMntExe(2650);
        detalle.setAnulado(LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Anulado.Enum.forString("A"));
        //## AGREGA
        detalles.add(detalle);
        //##    NOTA DE DEBITO ELECTRONICO
        detalle = LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.Factory.newInstance();
        detalle.setTpoDoc(BigInteger.valueOf(TipoDocumento.NOTA_DE_CREDITO_ELECTRONICA));
        detalle.setNroDoc(211);
        detalle.setFolioDocRef(781);
        detalle.setMntExe(2951);
        //## AGREGA
        detalles.add(detalle);
        //Cargas los detalles al libro)
        libroDocument.getLibroCompraVenta().getEnvioLibro().setDetalleArray(detalles.toArray(new LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle[detalles.size()]));
        //Arreglas
        XmlOptions opts = new XmlOptions();
        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(0);
        opts.setCharacterEncoding("ISO-8859-1");
        //Firmas
        libroDocument.sign(VARIABLES_GLOBALES.getPrivateKey(), VARIABLES_GLOBALES.getX509Certificate());
        // Grabas
        opts = new XmlOptions();
        opts.setCharacterEncoding("ISO-8859-1");
        libroDocument.save(new File(CertificateSET.RESULT_LIBRO_CV), opts);
        ConexionSii con = new ConexionSii();
        //KEY Y CERTIFICADO
        PrivateKey key = VARIABLES_GLOBALES.getPrivateKey();
        X509Certificate cert = VARIABLES_GLOBALES.getX509Certificate();
        String token = con.getToken(key, cert);
        VARIABLES_GLOBALES.LOG("Token: " + token);
        String enviadorS = Utilities.getRutFromCertificate(cert);
        RECEPCIONDTEDocument recps = con.uploadEnvio(enviadorS, VARIABLES_GLOBALES.RUT_EMPRESA, new File(CertificateSET.RESULT_LIBRO_CV), token);
        VARIABLES_GLOBALES.LOG(recps.xmlText());
        } catch (UnsupportedOperationException ex) {
        Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SOAPException ex) {
        Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConexionSiiException ex) {
        Logger.getLogger(CertificateSET.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
         */
}
