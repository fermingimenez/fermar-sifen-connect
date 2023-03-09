package py.com.fermar.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.json.stream.gson.GsonStreamFactory;
import net.shibboleth.utilities.java.support.xml.ElementSupport;
import py.com.fermar.exception.AttributeNotFoundException;
import py.com.fermar.exception.JsonConverterException;
import py.com.fermar.exception.MoreOnePropertyFoundException;
import py.com.fermar.exception.MoreOnePropertyNotFoundException;
import py.com.fermar.exception.PropertyNotFoundException;
import py.com.fermar.exception.XMLReadException;

/**
 * Clase utilitaria para el manejo de datos con la forma de XML
 *
 */
public class XmlUtils {

	private XmlUtils() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class.getName());

	public static final String XML_ENCODING_UTF_8 = "UTF-8";
	public static final String PROPERTY_NOT_FOUND = "No se ha encontrado la propiedad";
	private static final String XML_NOT_FOUND = "No se ha podido leer el XML dentro del CDATA";

	private static final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	/**
	 * Convierte un {@link Source} (documento XML) a un {@link Map} de forma de
	 * tratarlo como {@code JSON}
	 *
	 * @param xmlSource Documento XML
	 * @return {@link Map} con la forma de {@code JSON}
	 * @throws IOException En caso de una excepcion en el proceso de conversion
	 */
	public static Map<String, Object> xmlToMap(StAXSource xmlSource) throws IOException {
		// Configuracion de la salida del JSON
		JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true).autoPrimitive(true).prettyPrint(false)
				.namespaceDeclarations(false).build();
		// OutputStream de salida
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			XMLEventWriter writer = new JsonXMLOutputFactory(config, new GsonStreamFactory())
					.createXMLEventWriter(outputStream);
			// Se toma writer y se transforma
			XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(xmlSource.getXMLStreamReader());
			writer.add(eventReader);
			eventReader.close();
			writer.close();
			// Conversion del OutputStreama a Maps
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(outputStream.toString(), new TypeReference<Map<String, Object>>() {
			});
		} catch (XMLStreamException e) {
			throw new JsonConverterException(e);
		}
	}

	/**
	 * A partir de un {@link Document} y una ruta dentro del arbol de documento,
	 * obtiene todas las ocurrencias del elemento definido en la ruta que se
	 * encuentran dentro del documento
	 *
	 * @param documentXml  Documento XML {@link Document}
	 * @param pathProperty Ruta dentro del arbol del documento
	 * @return {@link List} de {@link Map}
	 * @throws TransformerException En caso de errores en la transformacion de los
	 *                              strings
	 * @throws XMLStreamException   En caso de errores en la generacion del Stream
	 *                              de XML
	 * @throws IOException          En caso de error en I/O
	 */
	public static List<Map<String, Object>> nodesListToJsonArray(@NotNull Document documentXml,
			@NotNull String pathProperty) throws TransformerException, XMLStreamException, IOException {
		List<Node> nodesData = getNodesListFromPath(documentXml, pathProperty);
		List<Map<String, Object>> jsonDataArray = new ArrayList<>();
		for (Node node : nodesData) {
			if (node == null) {
				jsonDataArray.add(new HashMap<>());
			} else {
				String nodeContentString = documentToString(node);
				jsonDataArray.add(xmlToMap(
						new StAXSource(xmlInputFactory.createXMLStreamReader(new StringReader(nodeContentString)))));
			}
		}

		return jsonDataArray;
	}

	/**
	 * Obtiene al atributo de un nodo dentro de un archivo {@code XML}. Ej: Para
	 * obtener Id. pathProperty = rDE.DE, attributeName = Id, type = Clase a la que
	 * se desea obtener el resultado
	 *
	 * @param documentXml   {@link Document} Documento XML
	 * @param pathProperty  Ruta de la propiedad a buscar, separada por puntos. Ej.
	 *                      rDE.DE
	 * @param attributeName Nombre del atributo dentor del nodo. Ej: Id
	 * @param type          Tipo al que se desea obtener el resultado
	 * @param <T>           Clase a la que se desea convertir
	 * @return El valor del atributo convertido al tipo pasado como parametro.
	 * @throws AttributeNotFoundException En caso de encontrarse el atributo
	 * @throws PropertyNotFoundException  En caso de encontrarse la propiedad
	 */
	public static <T> T getAttributeNode(@NotNull Document documentXml, @NotNull String pathProperty,
			@NotNull String attributeName, @NotNull Class<T> type) {
		Node nodeData = getNodeFromPath(documentXml, pathProperty);
		if (nodeData != null) {
			NamedNodeMap attributes = nodeData.getAttributes();
			Node attributeNode = attributes.getNamedItem(attributeName);
			if (attributeNode != null) {
				return TypeConverterUtils.convert(attributeNode.getNodeValue(), type);
			} else {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("No se ha encontrado el atributo {} en la propiedad {}", attributeName, pathProperty);

				throw new AttributeNotFoundException(
						"No se ha encontrado el atributo" + attributeName + "en la propiedad " + pathProperty);
			}
		} else {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("No se ha encontrado la propiedad {}", pathProperty);

			throw new PropertyNotFoundException("No se ha encontrado la propiedad " + pathProperty);
		}
	}

	/**
	 * Extrae la seccion {@code CDATA} dentro de un {@link Document} y obtiene un
	 * {@link Document} a partir del mismo
	 *
	 * @param document     Documento XML entero
	 * @param tagNameCDATA Nombre del elemento que contiene el {@code CDATA}
	 * @return {@link Document} con el contenido del {@code CDATA}
	 */
	public static Document getXmlDocumentFromCDATA(@NotNull Document document, @NotNull String tagNameCDATA) {
		try {
			String xmlString = getXmlStringFromCDATATag(document, tagNameCDATA);
			documentBuilderFactory.setNamespaceAware(true);

			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		} catch (Exception e) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("Error al leer el XML dentro del tag {}", tagNameCDATA);

			throw new XMLReadException(XML_NOT_FOUND, e);
		}
	}

	/**
	 * Extrae la seccion {@code CDATA} dentro de un {@link Document} y obtiene un
	 * {@link Document} a partir del mismo
	 *
	 * @param document     Documento XML entero
	 * @param tagNameCDATA Nombre del elemento que contiene el {@code CDATA}
	 * @return {@link String} con el contenido del {@code CDATA}
	 */
	private static String getXmlStringFromCDATATag(@NotNull Document document, @NotNull String tagNameCDATA) {
		try {
			String xml = null;
			NodeList xDE = document.getElementsByTagName(tagNameCDATA);
			Node cdataNode = xDE.item(0).getFirstChild();
			if (cdataNode != null) {
				if (cdataNode instanceof CharacterData) {
					CharacterData data = (CharacterData) cdataNode;
					xml = data.getData();
				}
				if (xml != null) {
					return xml;
				} else {
					throw new XMLReadException("No se ha podido leer el XML dentro del tag" + tagNameCDATA);
				}
			} else {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("No se ha encontrado la seccion del CDATA");

				throw new XMLReadException("No se ha encontrado la seccion del CDATA");
			}
		} catch (Exception e) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("No se ha podido leer el XML dentro del tag {}", tagNameCDATA);

			throw new XMLReadException(XML_NOT_FOUND, e);
		}
	}

	/**
	 * Obtiene el {@code XML} contenido dentro de una seccion CDATA
	 *
	 * @param document     {@link Document} que representa al XML
	 * @param tagNameCDATA Nombre del tag del XML que contiene la seccion CDATA
	 * @return XML en formato {@link StAXSource}
	 */
	public static StAXSource getXmlSourceFromCDATA(@NotNull Document document, @NotNull String tagNameCDATA) {
		try {
			String xmlString = getXmlStringFromCDATATag(document, tagNameCDATA);
			XMLStreamReader reader = xmlInputFactory
					.createXMLStreamReader(new StreamSource(new StringReader(xmlString)));
			return new StAXSource(reader);
		} catch (Exception e) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("No se ha podido leer el XML dentro del tag {}", tagNameCDATA);

			throw new XMLReadException(XML_NOT_FOUND, e);
		}
	}

	/**
	 * Obtiene una lista de {@link Node} en base a la ruta dentro del arbol del
	 * {@link Document}
	 * <p>
	 * Ejemplo: rDE.DE.gDtipDe.gCamItem
	 * </p>
	 *
	 * @param documentXml  Documento XML entero
	 * @param pathProperty Ruta del elemento dentro del arbol del Documento
	 *                     separados por puntos
	 * @return {@link List} de {@link Node}
	 */
	public static List<Node> getNodesListFromPath(@NotNull Node documentXml, @NotNull String pathProperty) {
		LinkedList<String> path = new LinkedList<>(Arrays.asList(pathProperty.split("\\.")));
		NodeList nodesList = documentXml.getChildNodes();
		String nodeName = path.pop();
		List<Node> nodes = new ArrayList<>();
		for (int i = 0; i < nodesList.getLength(); i++) {
			Node node = nodesList.item(i);
			if (StringUtils.equals(nodeName, node.getNodeName())) {
				nodes = getNode(node, path, nodes);
				if (nodes.isEmpty()) {
					// LOGGER.error("El elemento {} no ha sido encontrado", pathProperty);
					throw new PropertyNotFoundException("El elemento " + pathProperty + "no ha sido encontrado");
				}
			}
		}
		return nodes;
	}

	/**
	 * Obtiene el objeto {@link Node} del XML y la ruta enviada como parametro
	 * <p>
	 * Ej. rDE.dId 1 dId.rDE
	 * </p>
	 * Para obtener dId, pathProperty = rDE.dId, type = Tipo que de se desea
	 * obtener, en este caso Integer.class
	 *
	 * @param documentXml  {@link Document} que representa al XML
	 * @param pathProperty Direccion del nodo dentro del XML separados por puntos.
	 *                     Ej: rDE.dId
	 * @return La informacion contenida dentro del tag
	 * @throws PropertyNotFoundException En caso de no encontrar la propiedad
	 *                                   buscada
	 */
	public static Node getNodeFromPath(@NotNull Node documentXml, @NotNull String pathProperty) {
		LinkedList<String> path = new LinkedList<>(Arrays.asList(pathProperty.split("\\.")));
		NodeList nodesList = documentXml.getChildNodes();
		String nodeName = path.pop();
		Node nodeData = null;
		List<Node> nodes = new ArrayList<>();
		for (int i = 0; i < nodesList.getLength(); i++) {
			Node node = nodesList.item(i);
			if (StringUtils.equals(nodeName, node.getNodeName())) {
				if (path.isEmpty()) {
					return node;
				} else {
					nodes = getNode(node, path, nodes);
					if (nodes.size() == 1) {
						nodeData = nodes.get(0);
					} else if (nodes.size() > 1) {

						if (isArrayEmpty(nodes)) {
							throw new MoreOnePropertyNotFoundException(new PropertyNotFoundException(
									"No existe el elemento " + nodeName + " en la ruta " + pathProperty));
						}
						throw new MoreOnePropertyFoundException(
								"Existe mas de un elemento" + nodeName + " con esa ruta");

					} else if (nodes.isEmpty()) {
						throw new PropertyNotFoundException(
								"No existe el elemento " + nodeName + " en la ruta " + pathProperty);
					}
				}
			}
		}
		return nodeData;
	}

	private static <E> boolean isArrayEmpty(List<E> list) {
		boolean empty = true;

		if (list == null || list.isEmpty()) {
			return empty;
		}

		for (Object ob : list) {
			if (ob != null) {
				empty = false;
				break;
			}
		}

		return empty;
	}

	/**
	 * Obtiene la informacion contenida dentro de un tag XML de acuerdo al path.
	 * <p>
	 * Ej. rDE.dId 1 dId.rDE
	 * </p>
	 * Para obtener dId, pathProperty = rDE.dId, type = Tipo que de se desea
	 * obtener, en este caso Integer.class
	 *
	 * @param documentXml  {@link Document} que representa al XML
	 * @param pathProperty Direccion del nodo dentro del XML separados por puntos.
	 *                     Ej: rDE.dId
	 * @param type         Tipo al que se desea obtener el resultado
	 * @param <T>          La clase a que se desea convertir
	 * @return La informacion contenida dentro del tag
	 * @throws PropertyNotFoundException En caso de no encontrar la propiedad
	 *                                   buscada
	 */
	public static <T> T getElementNodeData(@NotNull Document documentXml, @NotNull String pathProperty,
			@NotNull Class<T> type) {
		Node nodeData = getNodeFromPath(documentXml, pathProperty);
		if (nodeData != null) {
			return TypeConverterUtils.convert(nodeData.getTextContent(), type);
		} else {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("No se ha encontrado la propiedad {} en el XML", pathProperty);

			throw new PropertyNotFoundException(PROPERTY_NOT_FOUND);
		}
	}

	/**
	 * Obtiene el contenido dentro del elemento del XML a partir de su {@code QName}
	 *
	 * @param documentXml Documento XML a revisar
	 * @param qName       {@link QName}
	 * @param type        Tipo al que se desea convertir el contenido
	 * @param <T>         Tipo al que se desea convertir el contenido
	 * @return El contenido del elemento en el tipo #type
	 * @throws PropertyNotFoundException En caso no encontrarse la propiedad buscada
	 *                                   o cuando exista mas de una con el mismo
	 *                                   {@code QName}
	 */
	public static <T> T getElementNodeData(@NotNull Element documentXml, QName qName, @NotNull Class<T> type) {
		final List<Element> elements = ElementSupport.getChildElementsByTagNameNS(documentXml, qName.getNamespaceURI(),
				qName.getLocalPart());
		if (elements.isEmpty()) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("No se ha encontrado la propiedad {} {} {} en el XML", qName.getNamespaceURI(),
						qName.getLocalPart(), qName.getPrefix());

			throw new PropertyNotFoundException(PROPERTY_NOT_FOUND);
		}
		if (elements.size() > 1) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("Existe mas de un elemento con dicha propiedad {} {} {} en el XML", qName.getNamespaceURI(),
						qName.getLocalPart(), qName.getPrefix());

			throw new PropertyNotFoundException(PROPERTY_NOT_FOUND);
		}
		return TypeConverterUtils.convert(elements.get(0).getTextContent(), type);
	}

	/**
	 * Metodo que recorre de forma recursiva el arbol del sXML
	 *
	 * @param node Nodo padre
	 * @param path Cola de ruta a seguir. Ej: rDE.dId = [rDE, dId].
	 *             <p>
	 *             Cada vez que se recorre por uno de los elementos de la lista se
	 *             realiza un pop de la misma
	 *             </p>
	 * @return {@link Node} El nodo del tag buscado
	 */
	private static List<Node> getNode(Node node, LinkedList<String> path, List<Node> foundedNodes) {
		if (path.size() == 1) {
			NodeList nodesList = node.getChildNodes();
			Node childNode;
			String nodeName = "";
			boolean nodeList = false;
			for (int i = 0; i < nodesList.getLength(); i++) {
				childNode = nodesList.item(i);
				nodeName = childNode.getNodeName();
				if (StringUtils.equals(path.get(0), nodeName)) {
					foundedNodes.add(childNode);
					nodeList = true;
				}
			}

			if (foundedNodes.isEmpty() || !nodeList) {
				foundedNodes.add(null);
			}

			return foundedNodes;

		} else if (path.size() > 1) {
			String nodeName = path.pop();
			List<Node> nodeList = new ArrayList<>();
			NodeList nodesList = node.getChildNodes();
			for (int i = 0; i < nodesList.getLength(); i++) {
				Node childNode = nodesList.item(i);
				if (StringUtils.equals(nodeName, childNode.getNodeName())) {
					nodeList = getNode(childNode, path, foundedNodes);
				}
			}
			path.addFirst(nodeName);
			return nodeList;

		} else {
			throw new PropertyNotFoundException("No se ha definido el path del nodo donde buscar");
		}
	}

	/**
	 * Obtiene un {@link String} a partir de un {@link Document}
	 *
	 * @param document XML a convertir
	 * @param <T>      tipo de dato
	 * @return El documento en {@link String}
	 * @throws TransformerException En caso de errores de transformacion
	 */
	public static <T extends Node> String documentToString(T document) throws TransformerException {
		StringWriter sw = new StringWriter();
		TransformerFactory tf = transformerFactory;
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		Transformer transformer = tf.newTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(sw));
		return sw.toString();
	}

	public static Document stringToDocument(String xmlString)
			throws IOException, SAXException, ParserConfigurationException {
		documentBuilderFactory.setNamespaceAware(true);

		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		} catch (Exception e) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("Usando otro método. Error al pasar string to document: {} {}", xmlString, e.getMessage());

			Reader reader = new InputStreamReader(new ByteArrayInputStream(xmlString.getBytes()),
					StandardCharsets.UTF_8);
			InputSource is = new InputSource(reader);
			is.setEncoding(StandardCharsets.UTF_8.name());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(is);
		}

	}

	public static Document stringToDocumentEventos(String xmlString)
			throws IOException, SAXException, ParserConfigurationException {
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));

	}

	/**
	 * Convertimos node a Document
	 * 
	 * @param node nodo
	 * @return Document
	 * @throws TransformerException         TransformerException
	 * @throws IOException                  IOException
	 * @throws SAXException                 SAXException
	 * @throws ParserConfigurationException ParserConfigurationException
	 */
	public static Document convertNodeToDocument(Node node)
			throws TransformerException, IOException, SAXException, ParserConfigurationException {
		String nodeContentString = documentToString(node);
		return stringToDocument(nodeContentString);
	}

	public static String getPathTagXml(@NotNull Document document, @NotNull String tagName) {
		Element element = document.getDocumentElement();
		String path = tagName;
		String nodeParent = element.getNodeName();
		NodeList nodelist = element.getElementsByTagName(tagName);
		String tagParent = obtenerPadre(nodelist, tagName);

		while (!Strings.isNullOrEmpty(tagParent)) {
			path = new StringBuilder().append(tagParent).append(".").append(path).toString();
			if (!nodeParent.equals(tagParent)) {
				nodelist = element.getElementsByTagName(tagParent);
				tagParent = obtenerPadre(nodelist, tagParent);
			} else {
				break;
			}
		}
		return path;
	}

	private static String obtenerPadre(NodeList nodelist, String tagName) {
		try {
			return nodelist.item(0).getParentNode().getNodeName();
		} catch (Exception e) {
			throw new PropertyNotFoundException("No se encontró path del tag '" + tagName + "' requerido");
		}
	}

	public static final String sourceToString(final Source source) throws TransformerException {

		TransformerFactory tf = transformerFactory;
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		Transformer transformer = tf.newTransformer();

		StringWriter outputWriter = new StringWriter();
		transformer.transform(source, new StreamResult(outputWriter));

		return outputWriter.toString();
	}
}
