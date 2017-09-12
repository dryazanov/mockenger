package org.mockenger.commons.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Dmitry Ryazanov
 */
public class XmlHelper {

	public static Document stringToXml(final String source) throws ParserConfigurationException, IOException, SAXException {
		return stringToXml(source, false);
	}

    public static Document stringToXml(final String source, final boolean isNamespaceAware)
			throws ParserConfigurationException, IOException, SAXException {

		final StringReader stringReader = new StringReader(source);
		final InputSource inputSource = new InputSource(stringReader);
		final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

		builderFactory.setNamespaceAware(isNamespaceAware);

		return builderFactory.newDocumentBuilder().parse(inputSource);
    }


	public static boolean hasDifferences(final String xml1, final String xml2) {
		return DiffBuilder.compare(Input.fromString(xml1))
				.withTest(Input.fromString(xml2))

				// TODO: Check if this option has a big impact on performance
				// Take into account node order
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
				.checkForSimilar()

				.ignoreWhitespace()
				.ignoreComments()
				.build()
				.hasDifferences();
	}
}
