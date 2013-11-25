/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tassidicambio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author bruno
 */
public class ExClient {

    public ExClient() {
    }

    public void getResource(String outFile) {
        try {

            URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/soap+xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            String xmlString = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                xmlString = xmlString + output;
            }
            this.xmlToString(outFile, xmlString);

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void xmlToString(String filePath, String xmlString) {
        try {
            //File fXmlFile = new File(xmlString);

            InputSource insrc = new InputSource(new StringReader(xmlString));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(insrc);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Cube");
            PrintWriter out = new PrintWriter(filePath);
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);



                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    if (eElement.hasAttribute("currency")) {
                        System.out.println("currency " + eElement.getAttribute("currency"));
                        System.out.println("rate " + eElement.getAttribute("rate"));
                        String tmp1 = eElement.getAttribute("currency");
                        tmp1 = this.pad(tmp1, 10, " ");
                        String tmp2 = eElement.getAttribute("rate");
                        tmp2 = this.pad(tmp2, 20, " ");
                        out.println(tmp1 + tmp2);
                    }

                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println(e);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ExClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (SAXException ex) {
            Logger.getLogger(ExClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

    }

    public String pad(String value, int length, String with) {
        StringBuilder result = new StringBuilder(length);
        result.append(value);

        while (result.length() < length) {
            result.insert(value.length(), with);
        }

        return result.toString();
    }
}
