package com.socialstartup.mockenger.data.model.transformer;

import com.socialstartup.mockenger.commons.utils.XmlHelper;
import org.slf4j.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;

/**
 * Created by x079089 on 3/23/2015.
 */
public class XPathTranformer extends AbstractTransformer {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(XPathTranformer.class);


    public XPathTranformer() {
        setType(TransformerType.XPATH);
    }

    public XPathTranformer(String pattern, String replacement) {
        this();
        setPattern(pattern);
        setReplacement(replacement);
    }

    @Override
    public String transform(String source) {
        String result = null;
        Document document = null;
        try {
            document = XmlHelper.stringToXml(source);
            return transform(document);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String transform(Node source) {
        validate();

        NodeList nodeList = null;
//        String xPathStr = "/configuration/param1/text()";
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile(this.pattern);
            nodeList = (NodeList) expression.evaluate(source, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        if (nodeList.getLength() > 0) {
            LOG.debug("Node value: %s\n", nodeList.item(0).getNodeValue());
            nodeList.item(0).setNodeValue(this.replacement);
        }

        // save the result
        StringWriter stringWriter = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(source), new StreamResult(stringWriter));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }
}
