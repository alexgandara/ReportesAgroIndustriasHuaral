package imprimePDF;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import wsHomologador.detalle;

public class readXML {

	public static factura_cabecera Cabecera = new factura_cabecera();
	public static factura_detalle[] Detalle = new factura_detalle[200];
	public static factura_detalle_email[] Detalle_email = new factura_detalle_email[200];
	public static reglones[] misReglones = new reglones[10];
	public static palabras[] arregloPalabras = new palabras[200];
	public static int _lineas_de_la_factura = 0;
	public static int _lineas_Descripcion = 0;

	public static void readXML(String _file_name, String _correos,
			parametros misParametros) throws IOException {

		String _file = _file_name;
		String _correo_destino = "";
		if (!isNullOrEmpty(_correos)) {
			_correo_destino = _correos;
		} else {
			_correo_destino = "nada";

		}

		String _file_xml = misParametros.get_ruta_xml_con_firma() + _file
				+ ".xml";
		;
		String _file_respuesta = misParametros.get_ruta_respuestas() + "r-"
				+ _file + ".xml";
		String _file_pdf = misParametros.get_ruta_pdfs() + _file + ".pdf";
		String _file_html = "S:\\conecta.global\\data\\20175077023\\10_formatos\\formato.htm";
		String _file_zip_respuesta = misParametros.get_ruta_respuestas() + "R-"
				+ _file + ".xml";
		;
		String _file_jpg = misParametros.get_ruta_formatos();

		// String _file_xml =
		// ".\\data\\20175077023\\03_xmls_con_firma\\"+_file+".xml";
		// String _file_respuesta =
		// ".\\data\\20175077023\\04_respuestas\\"+"r-"+_file+".xml";
		// String _file_pdf = ".\\data\\20175077023\\05_pdfs\\"+_file+".pdf";
		// String _file_html =
		// "S:\\conecta.global\\data\\20175077023\\10_formatos\\formato.htm";
		// String _file_zip_respuesta =
		// ".\\data\\20175077023\\04_respuestas\\"+"R-"+_file+".zip";;

		File fXmlFile = new File(_file_xml);
		try {

			String raya = "----------------------------------------------------------------";

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			// NodeList nList = doc.getElementsByTagName("Invoice");

			System.out.println("DATOS DEL DOCUMENTO");

			System.out.println(raya);

			Cabecera.set_descripcion_documento(doc.getDocumentElement()
					.getNodeName());
			System.out.println("Documento _ _ _ _ _ _ : "
					+ Cabecera.get_descripcion_documento());

			// cbc:ID //para serie y folio
			NodeList nList_id = doc.getElementsByTagName("cbc:ID");
			Node nNode_id = nList_id.item(5);
			// System.out.println("" + nNode_id.getNodeName());
			System.out.println("...");

			String _temp = nNode_id.getTextContent();
			int _num = _temp.length();
			Cabecera.set_serie(_temp.substring(0, 4));
			Cabecera.set_folio(_temp.substring(5, _num));

			System.out.println("Serie _ _ _ _ _ _ _ _ _ _ _ _ _: "
					+ Cabecera.get_serie());
			System.out.println("Folio _ _ _ _ _ _ _ _ _ _ _ _ _: "
					+ Cabecera.get_folio());

			Cabecera.set_tipo_doc_descripcion("FACTURA ELECTRONICA");

			Cabecera.set_Ruc_Dni("RUC:");

			// cbc:IssueDate
			NodeList nList_IssueDate = doc
					.getElementsByTagName("cbc:IssueDate");
			Node nNode_IssueDate = nList_IssueDate.item(0);

			String _fecha = nNode_IssueDate.getTextContent();

			String _Dia = "";
			String _Mes = "";
			String _Ano = "";
			_Dia = _fecha.substring(8, 10); // 2016.09.17 2016-11-30
			_Mes = _fecha.substring(5, 7); // 2016.09.17 0123456789
			_Ano = _fecha.substring(0, 4); // 1234567890
			Cabecera.set_fecha(_Dia + "/" + _Mes + "/" + _Ano);
			System.out.println("Fecha del Docto _ _ _ _ _ _ _ _: "
					+ Cabecera.get_fecha());
			// cbc:InvoiceTypeCode
			NodeList nList_InvoiceTypeCode = doc
					.getElementsByTagName("cbc:InvoiceTypeCode");
			Node nNode_InvoiceTypeCode = nList_InvoiceTypeCode.item(0);
			Cabecera.set_tipo_doc(nNode_InvoiceTypeCode.getTextContent());
			System.out.println("Tipo del Documento: _ _ _ _ _ _: "
					+ Cabecera.get_tipo_doc());

			if (Cabecera.get_tipo_doc().substring(1).equals("3")) {
				Cabecera.set_tipo_doc_descripcion("BOLETA ELECTRONICA");
				Cabecera.set_Ruc_Dni("DNI:");
			}

			// cbc:DocumentCurrencyCode
			NodeList nList_DocumentCurrencyCode = doc
					.getElementsByTagName("cbc:DocumentCurrencyCode");
			Node nNode_DocumentCurrencyCode = nList_DocumentCurrencyCode
					.item(0);
			Cabecera.set_moneda(nNode_DocumentCurrencyCode.getTextContent());
			System.out.println("Tipo de Moneda_ _ _ _ _ _ _ _ _: "
					+ Cabecera.get_moneda());

			System.out.println(raya);

			// cbc:CustomerAssignedAccountID "RUC DEL EMISOR"
			NodeList nList_CustomerAssignedAccountID = doc
					.getElementsByTagName("cbc:CustomerAssignedAccountID");
			Node nNode_CustomerAssignedAccountID = nList_CustomerAssignedAccountID
					.item(0);
			Cabecera.set_ruc_emisor(nNode_CustomerAssignedAccountID
					.getTextContent());
			System.out.println("RUC del Emisor_ _ _ _ _ _ _ _ _: "
					+ Cabecera.get_ruc_emisor());

			// cac:PartyName
			NodeList nList_PartyName = doc
					.getElementsByTagName("cac:PartyName");
			Node nNode_PartyName = nList_PartyName.item(0);
			Cabecera.set_razon_social_emisor(nNode_PartyName.getTextContent());
			System.out.println("Razon Social del Emisor_ _ _ _ : "
					+ Cabecera.get_razon_social_emisor());

			// cbc:StreetName
			NodeList nList_StreetName = doc
					.getElementsByTagName("cbc:StreetName");
			Node nNode_StreetName = nList_StreetName.item(0);
			Cabecera.set_direccion_emisor(nNode_StreetName.getTextContent());
			System.out.println("Direccion del Emisor_ _ _ _ _ _: "
					+ Cabecera.get_direccion_emisor());

			// cbc:ID guia
			NodeList nList_guia = doc.getElementsByTagName("cbc:ID");
			Node nNode_guia = nList_guia.item(6);
			if (nNode_guia.getTextContent().equals("GUIA")) {
				Cabecera.set_guia("");
			} else {
				Cabecera.set_guia(nNode_guia.getTextContent());
			}

			System.out.println("GUIA_ _ _ _ _ _ _ _ _ _ _ _ _: "
					+ Cabecera.get_guia());

			// cbc:ID fecha de pago
			NodeList nList_fecha_pag = doc.getElementsByTagName("cbc:ID");
			Node nNode_fecha_pag = nList_fecha_pag.item(6);
			Cabecera.set_fecha_pago(nNode_fecha_pag.getTextContent());
			System.out.println("Fecha de Pago_ _ _ _ _ _ _ _: "
					+ Cabecera.get_fecha_pago());

			// cbc:ID ubigeo
			NodeList nList_ubigeo = doc.getElementsByTagName("cbc:ID");
			Node nNode_ubigeo = nList_ubigeo.item(8);
			Cabecera.set_ubigeo_emisor(nNode_ubigeo.getTextContent());
			System.out.println("Ubigeo del Emisor _ _ _ _ _ _ _: "
					+ Cabecera.get_ubigeo_emisor());

			// cbc:IdentificationCode
			NodeList nList_IdentificationCode = doc
					.getElementsByTagName("cbc:IdentificationCode");
			Node nNode_IdentificationCode = nList_IdentificationCode.item(0);
			Cabecera.set_pais_emisor(nNode_IdentificationCode.getTextContent());
			System.out.println("Pais del Emisor_ _ _ _ _ _ _ _ : "
					+ Cabecera.get_pais_emisor());

			System.out.println(raya);

			// cbc:CustomerAssignedAccountID "RUC DEL RECEPTOR"
			NodeList nList_CustomerAssignedAccountID_r = doc
					.getElementsByTagName("cbc:CustomerAssignedAccountID");
			Node nNode_CustomerAssignedAccountID_r = nList_CustomerAssignedAccountID_r
					.item(1);
			Cabecera.set_ruc_receptor(nNode_CustomerAssignedAccountID_r
					.getTextContent());
			System.out.println("RUC del Receptor_ _ _ _ _ _ _ _: "
					+ Cabecera.get_ruc_receptor());

			// cac:PartyName
			NodeList nList_PartyName_r = doc
					.getElementsByTagName("cac:PartyName");
			Node nNode_PartyName_r = nList_PartyName_r.item(2);
			Cabecera.set_razon_social_receptor(nNode_PartyName_r
					.getTextContent());
			System.out.println("Razon Social del Receptor_ _ _ : "
					+ Cabecera.get_razon_social_receptor());

			// cbc:StreetName
			NodeList nList_StreetName_r = doc
					.getElementsByTagName("cbc:StreetName");
			Node nNode_StreetName_r = nList_StreetName_r.item(1);
			Cabecera.set_direccion_receptor(nNode_StreetName_r.getTextContent());
			System.out.println("Direccion del Receptor_ _ _ _ _: "
					+ Cabecera.get_direccion_receptor());

			// cbc:ID ubigeo
			NodeList nList_ubigeo_r = doc.getElementsByTagName("cbc:ID");
			Node nNode_ubigeo_r = nList_ubigeo_r.item(9);
			Cabecera.set_ubigeo_receptor(nNode_ubigeo_r.getTextContent());
			System.out.println("Ubigeo del Emisor _ _ _ _ _ _ _: "
					+ Cabecera.get_ubigeo_receptor());

			// cbc:IdentificationCode
			NodeList nList_IdentificationCode_r = doc
					.getElementsByTagName("cbc:IdentificationCode");
			Node nNode_IdentificationCode_r = nList_IdentificationCode_r
					.item(1);
			Cabecera.set_pais_receptor(nNode_IdentificationCode_r
					.getTextContent());
			System.out.println("Pais del Receptor_ _ _ _ _ _ _ : "
					+ Cabecera.get_pais_receptor());

			System.out.println(raya);
			// Cabecera.set_total_descuentos(0);
			NodeList nList_ids = doc
					.getElementsByTagName("sac:AdditionalMonetaryTotal");
			String _id = "";
			double _PayableAmount = 0;

			for (int temp = 0; temp < nList_ids.getLength(); temp++) {

				Node nNode_ids = nList_ids.item(temp);

				Element eElement_ids = (Element) nNode_ids;

				_id = eElement_ids.getElementsByTagName("cbc:ID").item(0)
						.getTextContent();
				_PayableAmount = Double.parseDouble(eElement_ids
						.getElementsByTagName("cbc:PayableAmount").item(0)
						.getTextContent());

				// System.out.println("ID:"+_id+" "+"Payable:"+_PayableAmount);

				if (_id.equals("1001")) {
					// cbc:PayableAmount MONTO GRABADO
					Cabecera.set_total_gravado(_PayableAmount);
					// System.out.println("Importe Grabado_ _ _ _ _ _ _ _: " +
					// Cabecera.get_total_gravado());
				}

				if (_id.equals("1002")) {
					// cbc:PayableAmount MONTO inafecto
					Cabecera.set_total_inafecto(_PayableAmount);
					// System.out.println("Importe Inafecto _ _ _ _ _ _ _: " +
					// Cabecera.get_total_inafecto());
				}

				if (_id.equals("1003")) {
					// cbc:PayableAmount MONTO exonerado
					Cabecera.set_total_exonerado(_PayableAmount);
					// System.out.println("Importe Exonerado_ _ _ _ _ _ _: " +
					// Cabecera.get_total_exonerado());
				}

				if (_id.equals("1004")) {
					// cbc:PayableAmount MONTO exonerado
					// Cabecera.set_total_gratuitas(_PayableAmount);
					// System.out.println("Transferencia Gratuita _ _ _ _: " +
					// Cabecera.get_total_gratuitas());
				}

				if (_id.equals("2005")) {
					// cbc:PayableAmount MONTO descuentos
					// Cabecera.set_total_descuentos(_PayableAmount);
					// System.out.println("Importe Descuentos _ _ _ _ _ _: " +
					// Cabecera.get_total_descuentos());
				}

			}

			Cabecera.set_subtotal(Cabecera.get_total_gravado()
					+ Cabecera.get_total_exonerado()
					+ Cabecera.get_total_inafecto());
			System.out.println("Importe Sub Total_ _ _ _ _ _ _: "
					+ Cabecera.get_subtotal());

			// cbc:TaxAmount
			NodeList nList_igv = doc.getElementsByTagName("cbc:TaxAmount");
			Node nNode_igv = nList_igv.item(0);
			Cabecera.set_total_igv(Double.parseDouble(nNode_igv
					.getTextContent()));
			System.out.println("Importe IGV_ _ _  _ _ _ _ _ _ : "
					+ Cabecera.get_total_igv());

			// cac:LegalMonetaryTotal

			NodeList nList_total = doc
					.getElementsByTagName("cac:LegalMonetaryTotal");

			double _PayableAmount_total = 0;

			// for (int temp = 0; temp < nList_total.getLength(); temp++) {

			Node nNode_total = nList_total.item(0);

			Element eElement_total = (Element) nNode_total;

			// _id=eElement_ids.getElementsByTagName("cbc:ID").item(0).getTextContent();
			_PayableAmount_total = Double.parseDouble(eElement_total
					.getElementsByTagName("cbc:PayableAmount").item(0)
					.getTextContent());

			// ///////

			// cbc:PayableAmount
			// NodeList nList_total =
			// doc.getElementsByTagName("cbc:PayableAmount");
			// Node nNode_total = nList_total.item(4);
			// Cabecera.set_total(Double.parseDouble(nNode_total.getTextContent()));

			Cabecera.set_total(_PayableAmount_total);
			System.out.println("**Importe Total_ _  _ _ _ _ _ _ : "
					+ Cabecera.get_total());

			// Cabecera.set_total_letra(denomina.main(Cabecera.get_total()-Cabecera.get_total_descuentos()));
			Cabecera.set_total_letra(denomina.main(Cabecera.get_total()));
			System.out.println("Importe con Letra _ _ _ _ _ _ : "
					+ Cabecera.get_total_letra());

			// cbc:Value importe con letra
			// NodeList nList_Value = doc.getElementsByTagName("cbc:Value");
			// Node nNode_Value = nList_Value.item(0);
			// Cabecera.set_total_letra(nNode_Value.getTextContent());
			// System.out.println("Importe con Letra _ _ _ _ _ _ : " +
			// Cabecera.get_total_letra());

			// DigestValue
			NodeList nList_DigestValue = doc
					.getElementsByTagName("DigestValue");
			Node nNode_DigestValue = nList_DigestValue.item(0);
			Cabecera.set_codigo_hash(nNode_DigestValue.getTextContent());
			System.out.println("Codigo Hash_ _ _ _ _ _ _ _ _ : "
					+ Cabecera.get_codigo_hash());

			// SignatureValue
			NodeList nList_SignatureValue = doc
					.getElementsByTagName("SignatureValue");
			Node nNode_SignatureValue = nList_SignatureValue.item(0);
			Cabecera.set_signature(nNode_SignatureValue.getTextContent());
			// System.out.println("Codigo Hash_ _ _ _ _ _ _ _ _ : " +
			// Cabecera.get_codigo_hash());

			// cac:AdditionalDocumentReference
			// NodeList nList_AdditionalDocumentReference =
			// doc.getElementsByTagName("cac:AdditionalDocumentReference");
			// Node nNode_AdditionalDocumentReference =
			// nList_AdditionalDocumentReference.item(1);
			// Cabecera.set_guia(nNode_AdditionalDocumentReference.getTextContent());
			// System.out.println("Codigo Hash_ _ _ _ _ _ _ _ _ : " +
			// Cabecera.get_codigo_hash());

			// System.out.println("guia:"+Cabecera.get_guia());

			Cabecera.set_sello(Cabecera.get_ruc_emisor() + "|"
					+ Cabecera.get_tipo_doc() + "|" + Cabecera.get_serie()
					+ "|" + Cabecera.get_folio() + "|"
					+ Cabecera.get_total_igv() + "|" + Cabecera.get_total()
					+ "|" + Cabecera.get_fecha() + "|" + "" + "|" + "" + "|"
					+ Cabecera.get_codigo_hash() + Cabecera.get_signature());
			// tipo de doc adquiriente
			// numero de doc adquiriente

			// sello

			// Cabecera.set_total_igv(0);
			// Cabecera.set_subtotal(0);
			// Cabecera.set_total(0);

			System.out.println(raya);
			System.out
					.println("Detalle del Documento_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");

			// cbc:ID cantidad
			NodeList nList_linea = doc
					.getElementsByTagName("cbc:InvoicedQuantity");

			System.out
					.println("numero de lineas _: " + nList_linea.getLength());
			int _total_linea = nList_linea.getLength();

			for (int _z = 0; _z <= 100; _z++) {
				Detalle[_z] = new factura_detalle();
			}

			for (int _z = 0; _z <= 100; _z++) {
				Detalle_email[_z] = new factura_detalle_email();
			}

			// cbc:ID cbc:LineExtensionAmount
			NodeList nList_LineExtensionAmount = doc
					.getElementsByTagName("cbc:LineExtensionAmount");

			// cbc:PriceAmount cac:Price
			NodeList nList_PriceAmount = doc.getElementsByTagName("cac:Price");

			// cbc:TaxableAmount
			NodeList nList_TaxableAmount = doc
					.getElementsByTagName("cbc:TaxableAmount");

			// cbc:Description
			NodeList nList_Description = doc
					.getElementsByTagName("cbc:Description");

			// cac:SellersItemIdentification
			NodeList nList_SellersItemIdentification = doc
					.getElementsByTagName("cac:SellersItemIdentification");

			// cbc:TaxExemptionReasonCode
			NodeList nList_TaxExemptionReasonCode = doc
					.getElementsByTagName("cbc:TaxExemptionReasonCode");

			// para sacar la unidad de medida cbc:InvoicedQuantity
			NodeList nList_InvoicedQuantity = doc
					.getElementsByTagName("cbc:InvoicedQuantity");

			int _lineaArreglo = 0;
			int _lineas_email = 0;

			for (int _linea = 0; _linea < nList_linea.getLength(); _linea++) {

				Node nNode_linea = nList_linea.item(_linea);
				Detalle[_lineaArreglo].set_cantidad(Double
						.parseDouble(nNode_linea.getTextContent()));

				Node nNode_tipo_igv = nList_TaxExemptionReasonCode.item(_linea);
				Detalle[_lineaArreglo].set_tipo_igv(nNode_tipo_igv.getTextContent());

				Node nNode_LineExtensionAmount = nList_LineExtensionAmount
						.item(_linea);
				Detalle[_lineaArreglo].set_subtotal((Double.parseDouble(nNode_LineExtensionAmount.getTextContent())));

				Node nNode_PriceAmount = nList_PriceAmount.item(_linea);
				Detalle[_lineaArreglo].set_precio_unitario((Double.parseDouble(nNode_PriceAmount.getTextContent())));
				double _subtotal_sin_igv = Detalle[_lineaArreglo].get_precio_unitario()	* Detalle[_lineaArreglo].get_cantidad();				_subtotal_sin_igv = round(_subtotal_sin_igv, 2);

				Detalle[_lineaArreglo].set_subtotal_sin_igv(_subtotal_sin_igv);

				Node nNode_TaxableAmount = nList_TaxableAmount.item(_linea);
				Detalle[_lineaArreglo].set_igv((Double
						.parseDouble(nNode_TaxableAmount.getTextContent())));

				Node nNode_SellersItemIdentification = nList_SellersItemIdentification
						.item(_linea);
				Node nNode_codigo = nNode_SellersItemIdentification
						.getFirstChild();
				Detalle[_lineaArreglo].set_codigo(nNode_codigo.getTextContent());
				
				System.out.println("Codigo "+Detalle[_lineaArreglo].get_codigo());

				Node nNode_InvoicedQuantity = nList_InvoicedQuantity
						.item(_linea);
				if (nNode_InvoicedQuantity.hasAttributes()) {
					NamedNodeMap attributes = nNode_InvoicedQuantity
							.getAttributes();
					Node nameAttribute = attributes.getNamedItem("unitCode");
					if (nameAttribute != null) {
						// System.out.println("Name attribute: " +
						// nameAttribute.getTextContent());
						Detalle[_lineaArreglo].set_unidad(nameAttribute
								.getTextContent());
					}
				}

				Node nNode_Description = nList_Description.item(_linea);
				String _text = nNode_Description.getTextContent();
				System.out.println(_text);

				Detalle_email[_lineas_email].set_codigo(Detalle[_lineaArreglo]
						.get_codigo());
				Detalle_email[_lineas_email]
						.set_precio_unitario(Detalle[_lineaArreglo]
								.get_precio_unitario());
				if (Detalle[_lineaArreglo].get_cantidad() > 0) {
					Detalle_email[_lineas_email]
							.set_cantidad(Detalle[_lineaArreglo].get_cantidad());
				}

				if (Detalle[_lineaArreglo].get_subtotal() > 0) {
					Detalle_email[_lineas_email]
							.set_subtotal(Detalle[_lineaArreglo].get_subtotal());
				}

				if (Detalle[_lineaArreglo].get_igv() > 0) {
					Detalle_email[_lineas_email].set_igv(Detalle[_lineaArreglo].get_igv());
				}

				Detalle_email[_lineas_email].set_descripcion(_text);
				_lineas_email++;
				// System.out.println("para email"+

				if (_text.length() < 58) {
					Detalle[_lineaArreglo].set_descripcion(nNode_Description
							.getTextContent());
					_lineaArreglo++;
					_lineas_Descripcion = _linea + _lineaArreglo;

				} else {

					_lineaArreglo = _lineaArreglo + _linea;
					int y = llenaPalabras(_text);
					String _reglon = "";
					int _tam_palabra = 0;
					int _tam_linea = 0;

					for (int _palabras = 0; _palabras <= y - 1; _palabras++) {
						_tam_palabra = arregloPalabras[_palabras].get_palabra()
								.length();
						if ((_tam_linea + _tam_palabra) < 58) {
							if (_reglon.equals("")
									&& arregloPalabras[_palabras].get_palabra()
											.equals(" ")) {

							} else {
								_reglon = _reglon
										+ arregloPalabras[_palabras]
												.get_palabra();
								_tam_linea = _tam_linea + _tam_palabra;
							}
						} else {
							_reglon = _reglon
									+ arregloPalabras[_palabras].get_palabra();
							Detalle[_lineaArreglo - _linea]
									.set_descripcion(_reglon);
							if (Detalle[_lineaArreglo - _linea].get_cantidad() == 0) {
								Detalle[_lineaArreglo - _linea].set_codigo(".");
							}

							_reglon = "";
							_tam_linea = 0;

							_lineaArreglo++;

						}
					}
					// System.out.println(_reglon);
					Detalle[_lineaArreglo - _linea].set_descripcion(_reglon);
					if (Detalle[_lineaArreglo - _linea].get_cantidad() == 0) {
						Detalle[_lineaArreglo - _linea].set_codigo(".");
					}
					_lineaArreglo++;
					try {
						Detalle[_lineaArreglo - _linea].set_descripcion("");
						_lineaArreglo++;
						_lineas_Descripcion = _linea + _lineaArreglo;
						// _lineaArreglo++;

					} catch (Exception e) {
						_lineaArreglo--;
						// e.printStackTrace();
					}
					_lineas_Descripcion = _linea + _lineaArreglo;
					// _lineaArreglo++;

				}

			}

			int _linea_imp2 = 0;
			for (int _linea_imp = 0; _linea_imp < _total_linea; _linea_imp++) {
				_linea_imp2 = _linea_imp + 1;
				System.out.println("");
				System.out
						.println("Linea_ _ _ _ _ _ _ _ _ _ _: " + _linea_imp2);
				System.out.println("Codigo_ _ _ _ _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_codigo());
				System.out.println("Unidad de Medida_ _ _ _ _ : "
						+ Detalle[_linea_imp].get_unidad());
				System.out.println("Descripcion _ _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_descripcion());
				System.out.println("Cantidad_ _ _ _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_cantidad());
				System.out.println("Precio Unitario _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_precio_unitario());
				System.out.println("IGV _ _ _ _ _ _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_igv());
				System.out.println("TIPO IGV_ _ _ _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_tipo_igv());
				System.out.println("Monto con IGV _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_subtotal());
				System.out.println("Monto sin IGV _ _ _ _ _ _ : "
						+ Detalle[_linea_imp].get_subtotal_sin_igv());
				_lineas_de_la_factura = _linea_imp2;

				// Cabecera.set_total_igv(Cabecera.get_total_igv()+Detalle[_linea_imp].get_igv());
				// Cabecera.set_subtotal(Cabecera.get_subtotal()+Detalle[_linea_imp].get_subtotal_sin_igv());

				// Cabecera.set_total(Cabecera.get_subtotal()+Detalle[_linea_imp].get_igv());

/*				if (Detalle[_linea_imp].get_tipo_igv().equals("37")) {
					Cabecera.set_total_gratuitas(Cabecera.get_total_gravado()
							+ Cabecera.get_total_exonerado()
							+ Cabecera.get_total_inafecto());
					Cabecera.set_total_gravado(0);
					Cabecera.set_total_exonerado(0);
					Cabecera.set_total_inafecto(0);
					Cabecera.set_total(0);
					Cabecera.set_total_letra(denomina.main(Cabecera.get_total()));

				}
*/
			}

			// factura.imp_factura(_file_xml, Cabecera, Detalle);
			Cabecera.set_mensaje_html(readFile(_file_html));

			// _lineas_Descripcion

			// printPDFmc.imp_factura(_file_xml, Cabecera, Detalle,
			// _lineas_de_la_factura,_file_pdf);
			printPDFA4.imp_factura(_file_xml, Cabecera, Detalle,
					_lineas_Descripcion, _file_pdf, _file_jpg);
			System.out.println("Reporte PDF Generado:" + _file_pdf);
			if (_correo_destino == "nada") {
				System.out.println("Correo Vacio, no se envio correo...");
			} else {
				System.out.println("Enviando Correo a " + _correo_destino);
				email.main(_correo_destino, _file_xml, _file_pdf,
						_file_respuesta, _file, Cabecera, Detalle,
						_lineas_de_la_factura, _file_zip_respuesta,
						Detalle_email);
				System.out.println("Correo Enviado.");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isNullOrEmpty(String a) {
		return a == null || a.isEmpty();
	}

	public static String readFile(String filename) throws IOException {
		String content = null;
		File file = new File(filename); // for ex foo.txt
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return content;
	}

	public static int llenaPalabras(String _cadena) {
		int _tam = _cadena.length();
		String _car = "";
		String _palabra = "";
		int _tam_palabra = 0;
		int _num_palabras = 0;
		int _ult_pos = 0;

		for (int x = 0; x <= _tam - 1; x++) {
			_car = _cadena.substring(x, x + 1);
			// System.out.println(_car+"  "+x);

			_tam_palabra++;

			if (_car.equals(" ")) {

				_palabra = _cadena.substring(_ult_pos, _ult_pos + _tam_palabra);
				_ult_pos = x + 1;
				_tam_palabra = 0;
				arregloPalabras[_num_palabras] = new palabras();
				arregloPalabras[_num_palabras].set_palabra(_palabra);
				// System.out.println("la palabra que subi es "+_palabra);
				_num_palabras++;

			}

		}

		_palabra = _cadena.substring(_ult_pos, _ult_pos + _tam_palabra);
		// _ult_pos=x+1;
		// _tam_palabra=0;
		arregloPalabras[_num_palabras] = new palabras();
		arregloPalabras[_num_palabras].set_palabra(_palabra);
		// System.out.println("la palabra que subi es "+_palabra);
		_num_palabras++;

		return _num_palabras;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
