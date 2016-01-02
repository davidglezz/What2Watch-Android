package com.example.user.testiguandroid.Activities;

import android.os.AsyncTask;

import com.example.user.testiguandroid.Logica.Pelicula;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class XMLDecoderUnaPelicula extends AsyncTask<String,Void,List<Pelicula>>{

    private List<Pelicula> decodearXMLVariasPeliculas(String url){

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

    private Pelicula decodearXMLUnaPelicula(String url){

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


    @Override
    protected List<Pelicula> doInBackground(String... params) {
        List<Pelicula> toReturnNunca= new ArrayList<Pelicula>();
       if(params[0].equals("una")){
           Pelicula p=decodearXMLUnaPelicula(params[1]);
           List<Pelicula> toRet= new ArrayList<Pelicula>();
           toRet.add(p);
           return toRet;
       }
        else if(params[0].equals("varias")){
            return decodearXMLVariasPeliculas(params[1]);
       }
        return toReturnNunca;
    }


    @Override
    protected void onPostExecute(List<Pelicula> result){

    }
}

