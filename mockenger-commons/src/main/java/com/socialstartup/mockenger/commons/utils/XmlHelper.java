package com.socialstartup.mockenger.commons.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import static java.nio.charset.Charset.forName;
import static javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION;

/**
 * @author Dmitry Ryazanov
 */
public class XmlHelper {

    public static SOAPMessage soapToXmlConverter(final Source source) throws SOAPException, IOException {
        final SOAPMessage soapMessage = createSOAPMessage(null, null);

        soapMessage.getSOAPPart().setContent(source);
        soapMessage.saveChanges();

        return soapMessage;
    }


    public static SOAPMessage stringToXmlConverter(final String source) throws SOAPException, IOException {
		final byte[] sourceBytes = source.getBytes(forName("UTF-8"));
		final InputStream inputStream = new ByteArrayInputStream(sourceBytes);

        return createSOAPMessage(new MimeHeaders(), inputStream);
    }


	public static String xmlToStringConverter(final Node node, final boolean omitXmlDeclaration) throws TransformerException {
		final DOMSource domSource = new DOMSource(node);
        final StringWriter stringWriter = new StringWriter();
		final StreamResult streamResult = new StreamResult(stringWriter);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OMIT_XML_DECLARATION, (omitXmlDeclaration ? "yes" : "no"));
		transformer.transform(domSource, streamResult);

        return stringWriter.toString();
    }


    public static Document stringToXml(final String source) throws ParserConfigurationException, IOException, SAXException {
		final StringReader stringReader = new StringReader(source);
		final InputSource inputSource = new InputSource(stringReader);

		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
    }


	private static SOAPMessage createSOAPMessage(final MimeHeaders headers, final InputStream inputStream) throws SOAPException, IOException {
		final MessageFactory messageFactory = MessageFactory.newInstance();

    	if (headers != null && inputStream != null) {
			return messageFactory.createMessage(new MimeHeaders(), inputStream);
		}

		return messageFactory.createMessage();
	}
}
