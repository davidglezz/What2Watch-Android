package com.example.user.testiguandroid.Logica;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by David on 03/01/2016.
 */
public class PopularMovies {

    private final static String URL = "http://www.imdb.com/chart/moviemeter";

    public static List<Pelicula> get() {

        List<Pelicula> peliculas = new ArrayList<Pelicula>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(URL);

            doc.getDocumentElement().normalize();

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "@data-titleid";
            NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

            System.out.println("nodes: " + nodes.getLength());

            for (int i = 0, z = nodes.getLength(); i < z; i++) {

                Node node = nodes.item(i);
                Log.d("PopularMovies",node.getNodeType() == Node.ATTRIBUTE_NODE ? "attr" : "-NO-");
                if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    Attr attr = (Attr) node;
                    //peliculas.add(Pelicula.get(attr.getValue().toString()));
                    Log.d("PopularMovies",attr.getValue().toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return peliculas;

    }
}
