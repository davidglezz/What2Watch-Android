package com.example.user.testiguandroid.API;

import com.example.user.testiguandroid.Logica.Pelicula;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.net.URL;

class XMLDecoder {

    public List<Pelicula> decodearXMLVariasPeliculas(String url){

        List<Pelicula> peliculasDelJson= new ArrayList<Pelicula>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();

            //	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("result");

            //	System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                //		System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
		/*
					System.out.println("Pelicula Title : " + eElement.getAttribute("Title"));
					System.out.println("Pelicula Year : " + eElement.getAttribute("Year"));
					System.out.println("Pelicula IMDBID : " + eElement.getAttribute("imdbID"));
					System.out.println("Pelicula Type : " + eElement.getAttribute("Type"));
					System.out.println("Pelicula Poster : " + eElement.getAttribute("Poster"));
		 */
                    peliculasDelJson.add(new Pelicula(eElement.getAttribute("Title"), Integer.parseInt(eElement.getAttribute("Year")), eElement.getAttribute("imdbID"), eElement.getAttribute("Type"), eElement.getAttribute("Poster")));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return peliculasDelJson;

    }

    public Pelicula decodearXMLUnaPelicula(String url){

        Pelicula peliADevolver=null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();

            //	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("movie");

            //	System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                //		System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
		/*
					System.out.println("Pelicula Title : " + eElement.getAttribute("Title"));
					System.out.println("Pelicula Year : " + eElement.getAttribute("Year"));
					System.out.println("Pelicula IMDBID : " + eElement.getAttribute("imdbID"));
					System.out.println("Pelicula Type : " + eElement.getAttribute("Type"));
					System.out.println("Pelicula Poster : " + eElement.getAttribute("Poster"));
		 */
                    peliADevolver=new Pelicula(eElement.getAttribute("title"),
                            Integer.parseInt(eElement.getAttribute("year")),
                            eElement.getAttribute("imdbID"),
                            eElement.getAttribute("type"),
                            eElement.getAttribute("poster"),
                            eElement.getAttribute("rated"),
                            eElement.getAttribute("released"),
                            eElement.getAttribute("runtime"),
                            eElement.getAttribute("genre"),
                            eElement.getAttribute("director"),
                            eElement.getAttribute("writer"),
                            eElement.getAttribute("actors"),
                            eElement.getAttribute("plot"),
                            eElement.getAttribute("country"),
                            eElement.getAttribute("awards"),
                            eElement.getAttribute("imdbRating"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return peliADevolver;

    }


}

