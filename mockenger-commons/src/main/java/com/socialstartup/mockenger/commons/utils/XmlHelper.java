package com.socialstartup.mockenger.commons.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
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
import java.nio.charset.Charset;

/**
 * Created by x079089 on 3/20/2015.
 */
public class XmlHelper {

    public static SOAPMessage soapToXmlConverter(Source source) throws SOAPException {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        soapMessage.getSOAPPart().setContent(source);
        soapMessage.saveChanges();
        return soapMessage;
    }

    public static SOAPMessage stringToXmlConverter(String source) throws SOAPException, IOException {
        InputStream inputStream = new ByteArrayInputStream(source.getBytes(Charset.forName("UTF-8")));
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(new MimeHeaders(), inputStream);
        return soapMessage;
    }

    public static String xmlToStringConverter(Node node, boolean omitXmlDeclaration) throws TransformerException {
        StringWriter stringWriter = new StringWriter();

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (omitXmlDeclaration ? "yes" : "no"));
        transformer.transform(new DOMSource(node), new StreamResult(stringWriter));

        return stringWriter.toString();
    }

    public static Document stringToXml(String source) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(source)));
    }
}
