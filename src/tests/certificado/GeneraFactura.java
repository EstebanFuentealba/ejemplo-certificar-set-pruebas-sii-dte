/**
 * Copyright [2009] [NIC Labs]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the 	License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or 
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 **/

package tests.certificado;

import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.Dec126Type;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.ImpAdicDTEType;
import cl.sii.siiDte.ImpAdicDTEType.Enum;
import cl.sii.siiDte.MntImpType;
import cl.sii.siiDte.MontoType;
import cl.sii.siiDte.PctType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import siidte.Example.*;
import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.apache.xmlbeans.XmlOptions;

import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDefType.Documento.Detalle;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Emisor;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Receptor;
import cl.sii.siiDte.DTEDocument;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import net.estebanfuentealba.dte.Folio;
import net.estebanfuentealba.dte.TipoDocumento;
import tests.VARIABLES_GLOBALES;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Totales;

public class GeneraFactura {

	private static void printUsage() {
		System.err
				.println("Utilice: java cl.nic.dte.examples.GeneraFactura "
						+ "-a <caf.xml> -p <plantilla.xml> -c <certDigital.p12> -s <password> -o <resultado.xml>");
	}

	/**
	 * @param args
	 */
        private static String PLANTILLA_DOCUMENTO = "C:/Users/Esteban/Documents/My Dropbox/ATL DTE/DTE_OpenLibs/ejemplos/plantilla_documento.xml";
	private static String RESULT_FACTURA = "D:/ATL/CERT/Factura.xml";
        private static String PLANTILLA_ATL = "D:/ATL/plantilla_atl.xml";
        private static String cafFile = "D:/ATL/CERT/FoliosSII7601980734520101014225.xml";

        public static void main(String[] args) throws Exception {


		String certS = VARIABLES_GLOBALES.URL_CERT;
		String passS = VARIABLES_GLOBALES.PASS_CERT;
		String resultS = GeneraFactura.RESULT_FACTURA;
		String plantillaS = GeneraFactura.PLANTILLA_DOCUMENTO;
                
                KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		String alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + certS);

		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		PrivateKey key = (PrivateKey) ks.getKey(alias, passS.toCharArray());
                








                
                // Leo Autorizacion
		// Debo meter el namespace porque SII no lo genera
		// Debo meter el namespace porque SII no lo genera
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		AutorizacionType caf = AUTORIZACIONDocument.Factory.parse(
				new File(cafFile), opts).getAUTORIZACION();


                DTEDocument docT = DTEDocument.Factory.parse(new File(GeneraFactura.PLANTILLA_ATL), opts);

                docT.getDTE().getDocumento().getEncabezado().getIdDoc().setFolio(8);
                docT.getDTE().getDocumento().setID("N" + docT.getDTE().getDocumento().getEncabezado().getIdDoc().getFolio());
                Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 45);
		 docT.getDTE().getDocumento().getEncabezado().getIdDoc().xsetFchCancel(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));
                //  TOTALES
		Totales tot = docT.getDTE().getDocumento().getEncabezado().addNewTotales();
                tot.setMntExe(4536);
		tot.setMntTotal(4536);
                //  DETALLES
                Detalle[] detalle = new Detalle[1];
                detalle[0] = Detalle.Factory.newInstance();
                detalle[0].setNmbItem("HORAS PROGRAMADOR");
                detalle[0].setNroLinDet(1);
                detalle[0].setQtyItem(BigDecimal.valueOf(2));
                detalle[0].setPrcItem(BigDecimal.valueOf(2268));
                detalle[0].setUnmdItem("Hora");
                detalle[0].setMontoItem(2268);

                docT.getDTE().getDocumento().setDetalleArray(detalle);
                
                // Timbro
                
		docT.getDTE().timbrar(caf.getCAF(), caf.getPrivateKey(null));
                
                // antes de firmar le doy formato a los datos
		opts = new XmlOptions();
		opts.setSaveImplicitNamespaces(namespaces);
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);

		// releo el doc para que se reflejen los cambios de formato
		docT = DTEDocument.Factory.parse(docT.newInputStream(opts), opts);

		// firmo
		docT.getDTE().sign(key, cert);

		// Guardo
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
		docT.save(new File(RESULT_FACTURA), opts);


                VerificaFactura.DO(RESULT_FACTURA);
                /*
                //INICIO BORRAR
                opts.setLoadSubstituteNamespaces(namespaces);
                opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
                //FIN BORRAR
                */
                /*
		AutorizacionType caf = AUTORIZACIONDocument.Factory.parse(
				new File(cafFile), opts).getAUTORIZACION();
                */

		/*
                 // Construyo base a partir del template
		//DTEDocument doc = DTEDocument.Factory.parse(new File(plantillaS), opts);
                DTEDocument doc = DTEDocument.Factory.newInstance();
                
                DTEDefType def = DTEDefType.Factory.newInstance();
                DTEDefType.Documento d = DTEDefType.Documento.Factory.newInstance();
                d.setEncabezado(DTEDefType.Documento.Encabezado.Factory.newInstance());
                d.getEncabezado().setIdDoc(IdDoc.Factory.newInstance());
                d.getEncabezado().setEmisor(Emisor.Factory.newInstance());
                d.getEncabezado().setReceptor(Receptor.Factory.newInstance());
                def.setDocumento(d);
                doc.setDTE(def);
                doc.getDTE().getDocumento().setID("N150");
                doc.getDTE().setVersion(BigDecimal.ONE);
                //###   IdDoc
                IdDoc idDoc = doc.getDTE().getDocumento().getEncabezado().getIdDoc();

                idDoc.setTipoDTE(BigInteger.valueOf(TipoDocumento.FACTURA_NO_AFECTA_O_EXENTA_ELECTRONICA));

		idDoc.setFolio(caf.getCAF().getDA().getRNG().getD());
                idDoc.setFchEmis(Calendar.getInstance());
                idDoc.setFmaPago(new BigInteger("1"));
                idDoc.setIndServicio(new BigInteger("1"));
                
                //###   EMISOR
                Emisor emisor = doc.getDTE().getDocumento().getEncabezado().getEmisor();
                emisor.setRUTEmisor(Utilities.getRutFromCertificate(cert));
                    //Acteco EDITAR
                //emisor.setActecoArray(new int[] { 1,2,3,4 });
                    //CdgSIISucur EDITAR
                emisor.setCdgSIISucur(123456789);
                //###   Receptor
                Receptor receptor = doc.getDTE().getDocumento().getEncabezado().getReceptor();
                    //RUTRecep EDITAR
                receptor.setRUTRecep("55555555-5");
                    //DirRecep EDITAR
                receptor.setDirRecep("Embajada USA");
                receptor.setCmnaRecep("Vitacura");
                receptor.setCiudadRecep("Santiago");

                //Detalle[] detalle = new Detalle[1];
                detalle[0] = Detalle.Factory.newInstance();
                detalle[0].setNmbItem("HORAS PROGRAMADOR");
                detalle[0].setNroLinDet(1);
                detalle[0].setQtyItem(BigDecimal.valueOf(2));
                detalle[0].setPrcItem(BigDecimal.valueOf(2268));
                detalle[0].setUnmdItem("Hora");

                doc.getDTE().getDocumento().setDetalleArray(detalle);
                
                doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);
                */

                
	}

}
