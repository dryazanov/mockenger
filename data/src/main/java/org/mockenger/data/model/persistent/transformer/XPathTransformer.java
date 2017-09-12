package org.mockenger.data.model.persistent.transformer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static java.util.Objects.nonNull;
import static javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION;
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.mockenger.commons.utils.XmlHelper.stringToXml;
import static org.mockenger.data.model.dict.TransformerType.XPATH;

/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Getter
@Setter
public class XPathTransformer extends AbstractTransformer {

	private List<Pair> namespaces;


    public XPathTransformer() {
        setType(XPATH);
    }


    public XPathTransformer(final String pattern, final String replacement) {
        this();
        setPattern(pattern);
        setReplacement(replacement);
    }


    public XPathTransformer(final String pattern, final String replacement, final List<Pair> namespaces) {
        this(pattern, replacement);
        this.namespaces = namespaces;
    }


    /**
     *
     * @param source
     * @return
     */
    @Override
    public String transform(final String source) {
        validate();

        try {
        	final boolean isNamespaceAware = (nonNull(namespaces) && namespaces.size() > 0);
            final String result = transform(stringToXml(source, isNamespaceAware));

            if (result != null) {
                return result;
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("XML parse failure", e);
        }

        return source;
    }


    private String transform(final Document source) {
        try {
            final XPath xPath = XPathFactory.newInstance().newXPath();

            if (nonNull(namespaces)) {
				final SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();

				namespaces.forEach(pair -> namespaceContext.bindNamespaceUri(pair.getKey(), pair.getValue()));
				xPath.setNamespaceContext(namespaceContext);
			}

            final XPathExpression expression = xPath.compile(pattern);
            final NodeList nodeList = (NodeList) expression.evaluate(source, NODESET);

            if (nodeList.getLength() > 0) {
                log.debug(String.format("Node value: %s", nodeList.item(0).getNodeValue()));

                nodeList.item(0).setNodeValue(replacement);

                // save the result
                final StringWriter stringWriter = new StringWriter();
                final Transformer transformer = TransformerFactory.newInstance().newTransformer();

                transformer.setOutputProperty(OMIT_XML_DECLARATION, "yes");
                transformer.transform(new DOMSource(source), new StreamResult(stringWriter));

                log.debug(String.format("Transformed with xPath: %s", stringWriter.toString()));

                return stringWriter.toString();
            }
        } catch (XPathExpressionException | TransformerException e) {
            log.error("XML transformation failed", e);
        }

        return null;
    }
}
