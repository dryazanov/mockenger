package com.socialstartup.mockenger.frontend.common;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

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
