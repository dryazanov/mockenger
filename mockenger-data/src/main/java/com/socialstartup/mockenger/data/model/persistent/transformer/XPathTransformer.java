package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.socialstartup.mockenger.commons.utils.XmlHelper;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Dmitry Ryazanov on 3/23/2015.
 */
public class XPathTransformer extends AbstractTransformer {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(XPathTransformer.class);


    public XPathTransformer() {
        setType(TransformerType.XPATH);
    }

    public XPathTransformer(String pattern, String replacement) {
        this();
        setPattern(pattern);
        setReplacement(replacement);
    }

    /**
     *
     * @param source
     * @return
     */
    // TODO: Store info about failed transformation and show it later to user
    @Override
    public String transform(String source) {
        validate();

        try {
            Document document = XmlHelper.stringToXml(source);
//            document.setXmlStandalone(false);
            String result = transform(document);
            if (result != null) {
                return result;
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOG.debug(e.getMessage());
        }

        return source;
    }

    // TODO: Store info about failed transformation and show it later to user
    private String transform(Node source) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile(this.pattern);
            NodeList nodeList = (NodeList) expression.evaluate(source, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                LOG.debug(String.format("Node value: %s", nodeList.item(0).getNodeValue()));
                nodeList.item(0).setNodeValue(this.replacement);

                // save the result
                DOMSource domSource = new DOMSource(source);
                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.transform(domSource, streamResult);

                LOG.debug(String.format("Transformed with xPath: %s", stringWriter.toString()));

                return stringWriter.toString();
            }
        } catch (XPathExpressionException | TransformerException e) {
            LOG.debug(e.getMessage());
        }

        return null;
    }
}
