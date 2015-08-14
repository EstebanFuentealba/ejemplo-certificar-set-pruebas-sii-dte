/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package siidte;
import java.io.ByteArrayInputStream;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.w3c.dom.Node;
/**
 *
 * @author Esteban
 */
public class Utils {
    public static String getParametersFromHTML(String html) throws XPathExpressionException {
        Tidy tidy = new Tidy();
        tidy.setShowWarnings(false);
        tidy.setShowErrors(0);
        tidy.setQuiet(true);
        tidy.setMakeClean(true);

        Document document = tidy.parseDOM(new ByteArrayInputStream(html.getBytes()), null);
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("//input");
        NodeList nodosHTMLDOM = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        StringBuilder  sb = new StringBuilder();
        for (int i = 0; i < nodosHTMLDOM.getLength(); i++) {
            Node node = nodosHTMLDOM.item(i);
            sb.append(node.getAttributes().getNamedItem("name").getNodeValue()).append("=").append(node.getAttributes().getNamedItem("value").getNodeValue()).append("&");
        }
        return sb.toString();

    }
}
