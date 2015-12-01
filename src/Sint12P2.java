import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Sint12P2 extends HttpServlet {

    public static ArrayList<String> listaXML = new ArrayList<String>();
    public static ArrayList<String> listaXMLleidos = new ArrayList<String>();
    public static ArrayList<Document> listDoc = new ArrayList<Document>();
    public static ArrayList<String> listError = new ArrayList<String>();
    public static ArrayList<String> listFichError = new ArrayList<String>();
    public boolean inicio = false;

    public void init() {

        String URL = "sabina.xml";
        listaXML.add(URL);

        int i = 0;
        while (listaXML.size() > 0) {

            i++;
            String url = listaXML.get(0);
            processIML(url);
            listaXML.remove(0);
            listaXMLleidos.add(url);

        }
        return;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        if (req.getParameter("etapa") != null) { // si no existe el hidden "etapa" es que se ha cargado la pagina desde cero
            String etapa = req.getParameter("etapa");
            if (etapa.equals("0")) { //Comprobamos si se ha pulsado el botón de "Inicio"
                Inicio(out, req, res);
            } else {
                if (req.getParameter("anterior") == null || req.getParameter("anterior").equals("null")) { // si el hidden "anterior" es null es que no se ha pulsado el botón "Atras"
                    if (req.getParameter("consultainicial") != null) { //si no existe el hidden "consultainicial" vamos a la pantalla inicial
                        if (req.getParameter("consultainicial").equals("Cantantes")) {
                            if (etapa.charAt(0) == '2') {
                                resultado1(out, req, res);
                            } else {
                                etapa21(out, req, res);
                            }
                        } else {
                            switch (etapa.charAt(0)) { //comprobar etapa
                                case '1':
                                    etapa22(out, req, res);
                                    break;
                                case '2':
                                    etapa32(out, req, res);
                                    break;
                                case '3':
                                    resultado2(out, req, res);
                                    break;
                            }
                        }
                    } else {
                        if (req.getParameter("consulta").equals("1")) {
                            etapa11(out, req, res);
                        } else {
                            etapa12(out, req, res);
                        }
                    }
                } else { //El hidden "anterior" no es null --> comprobar donde se ha pulsado el botón "Atras"
                    if (etapa.charAt(1) == '1') { //consulta 1
                        switch (req.getParameter("anterior").charAt(0)) { //comprobar etapa
                            case '1':
                                etapa11(out, req, res);
                                break;
                            case '2':
                                etapa21(out, req, res);
                                break;
                        }
                    } else { //consulta 2
                        switch (req.getParameter("anterior").charAt(0)) { //comprobar etapa
                            case '1':
                                etapa12(out, req, res);
                                break;
                            case '2':
                                etapa22(out, req, res);
                                break;
                            case '3':
                                etapa32(out, req, res);
                                break;
                        }
                    }
                } //fin consula atras
            } //fin existe el hidden "etapa"
        } else {
            Inicio(out, req, res);
        }
    }


    /*****************************************************************************************************************************************/


    public void Inicio(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        imprimirInicio(out);
        out.println("<div class='error'>");
        out.println("<b>Errores encontrados</b>: <br>");
        if (listError.size() == 0) {
            out.println("Ningún error encontrado");
        } else {
            for (int i = 0; i < listError.size(); i++) {
                out.println(listError.get(i) + "<br>");
            }
        }
        out.println("<br><b>Ficheros erróneos</b>: <br>");
        if (listFichError.size() == 0) {
            out.println("Ningún fichero erróneo encontrado");
        } else {
            for (int i = 0; i < listFichError.size(); i++) {
                out.println(listFichError.get(i) + "<br>");
            }
        }
        out.println("</div>");

        out.println("<h3>Selecciona la consulta que desea hacer</h3>");
        out.println("<form method='GET' action='?etapa=1' >");
        out.println("<input type='hidden' name='etapa' value='10'>");
        if (req.getParameter("consultainicial") != null) {
            if (req.getParameter("consultainicial").equals("Cantantes")) {
                out.println("<input type='radio' checked='' value='1' name='consulta'>Lista de canciones");
                out.println("<br>");
                out.println("<input type='radio' value='2' name='consulta'>Número de canciones");
                out.println("<br>");
            } else {
                out.println("<input type='radio' value='1' name='consulta'>Lista de canciones");
                out.println("<br>");
                out.println("<input type='radio' checked='' value='2' name='consulta'>Número de canciones");
                out.println("<br>");
            }
        } else {
            out.println("<input type='radio' value='1' checked='' name='consulta'>Lista de canciones");
            out.println("<br>");
            out.println("<input type='radio' value='2' name='consulta'>Número de canciones");
            out.println("<br>");
        }
        out.println("<p>");
        out.println("<input type='submit' value='Enviar' ");
        out.println("<p>");
        out.println("</form>");
        imprimirFinal(out);

    }

    public void etapa11(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        ArrayList<String> list = getCantantes();
        imprimirInicio(out);
        out.println("<h4>Seleccione el inteprete deseado:</h4>");
        out.println("<form method='GET' action='?etapa=21&consultainicial=Cantantes' >");
        out.println("<input type='hidden' name='etapa' value='11'>");
        out.println("<input type='hidden' name='consultainicial' value='Cantantes'>");

        for (int i = 0; i < list.size(); i++) {
            out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='interprete'>" + list.get(i) + "");
            out.println("<br>");
        }

        out.println("<input type='radio' value='Todos' name='interprete'>Todos");
        out.println("<br>");

        out.println("<p>");
        out.println("<input type='submit' value='Enviar' >");
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);


    }

    public void etapa21(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setCharacterEncoding("ISO-8859-15");
        ArrayList<String> list = getAlbumCantante(req.getParameter("interprete"));
        System.out.println("encoding: "+req.getCharacterEncoding());
        System.out.println("---->"+req.getParameter("interprete"));
        imprimirInicio(out);
        out.println("<h2>Lista de canciones</h2> <h3>Cantante: " + req.getParameter("interprete") + "</h3>");
        out.println("<h4>Seleccione el álbum deseado:</h4>");
        out.println("<form method='GET' action='?etapa=31&consultainicial=Cantantes&Cantante=" + req.getParameter("interprete") + "' >");
        out.println("<input type='hidden' name='consultainicial' value='Cantantes'>");
        out.println("<input type='hidden' name='etapa' value='21'>");
        out.println("<input type='hidden' name='anterior' value='null'>");
        out.println("<input type='hidden' name='interprete' value='" + req.getParameter("interprete") + "'>");

        for (int i = 0; i < list.size(); i++) {
            out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='album1'>" + list.get(i) + "");
            out.println("<br>");
        }
        out.println("<input type='radio' value='Todos' name='album1'>Todos ");
        out.println("<br>");

        out.println("<p>");
        out.println("<input type='submit' value='Enviar' >");
        out.println("<br>");
        out.println("<input type='submit' onclick='form.anterior.value=11' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);


    }

    public void resultado1(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        ArrayList<String> list = getCancionesCantante(req.getParameter("interprete"), req.getParameter("album1"));

        imprimirInicio(out);
        out.println("<h2>Lista de canciones</h2><h3>Cantante: " + req.getParameter("interprete") + "<br>Álbum: " + req.getParameter("album1") + "</h3>");
        out.println("<h4>Resultado de su consulta:</h4>");

        out.println("<br>");
        out.println("<ul>");

        for (int i = 0; i < list.size(); i++) {
            out.println("<il>" + list.get(i) + "</li>");
            out.println("<br>");
        }

        out.println("</ul>");
        out.println("<br>");
        out.println("<form method='GET'>");
        out.println("<input type='hidden' name='consultainicial' value='Cantantes'>");
        out.println("<input type='hidden' name='interprete' value='" + req.getParameter("interprete") + "'>");
        out.println("<input type='hidden' name='album-1' value='" + req.getParameter("album1") + "'>");
        out.println("<input type='hidden' name='etapa' value='31'>");

        out.println("<input type='hidden' name='anterior' value='null'>");

        out.println("<p>");
        out.println("<input type='submit' onclick='form.anterior.value=21' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);


    }

    /*********************************************************************************************
     * **********************************************************************************************
     ************************************************************************************************/


    public void etapa12(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        ArrayList<String> list = getAnhoAlbumes();

        imprimirInicio(out);
        out.println("<h3>Lista de canciones por estilo</h3>");
        out.println("<h4>Seleccione el año deseado:</h4>");
        out.println("<form method='GET' action='?etapa=22&consultainicial=Canciones' >");
        out.println("<input type='hidden' name='etapa' value='12'>");
        out.println("<input type='hidden' name='consultainicial' value='Canciones'>");


        for (int i = 0; i < list.size(); i++) {
            out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='anhio'>" + list.get(i) + "");
            out.println("<br>");
        }
        out.println("<input type='radio' value='Todos' name='anhio'>Todos");
        out.println("<br>");
        out.println("<p>");
        out.println("<input type='submit' value='Enviar' >");
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);


    }

    public void etapa22(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        ArrayList<String> list = getAlbumesPorAnho(req.getParameter("anhio"));

        imprimirInicio(out);
        out.println("<h2>Número de canciones</h2><h3>Año: " + req.getParameter("anhio") + "</h3>");
        out.println("<h4>Seleccione el álbum deseado:</h4>");
        out.println("<form method='GET' action='?etapa=32&consultainicial=Canciones&anio=" + req.getParameter("anhio") + "' >");
        out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
        out.println("<input type='hidden' name='etapa' value='22'>");

        out.println("<input type='hidden' name='anterior' value='null'>");

        out.println("<input type='hidden' name='anhio' value='" + req.getParameter("anhio") + "'>");

        for (int i = 0; i < list.size(); i++) {
            out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='album2'>" + list.get(i) + "");
            out.println("<br>");
        }

        out.println("<input type='radio' value='Todos' name='album2'>Todos");
        out.println("<br>");
        out.println("<p>");
        out.println("<input type='submit' value='Enviar' >");
        out.println("<br>");
        out.println("<input type='submit' onclick='form.anterior.value=12' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);


    }

    public void etapa32(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        ArrayList<String> list = getEstilo(req.getParameter("anhio"), req.getParameter("album2"));

        imprimirInicio(out);
        out.println("<h2>Número de canciones</h2><h3>Año: " + req.getParameter("anhio") + "<br>Álbum: " + req.getParameter("album2") + "</h3>");
        out.println("<h4>Seleccione el estilo deseado:</h4>");
        out.println("<form method='GET' action='?etapa=42&consultainicial=Canciones&anio=" + req.getParameter("anhio") + "album=" + req.getParameter("album2") + "' >");
        out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
        out.println("<input type='hidden' name='anhio' value='" + req.getParameter("anhio") + "'>");
        out.println("<input type='hidden' name='etapa' value='32'>");

        out.println("<input type='hidden' name='anterior' value='null'>");

        out.println("<input type='hidden' name='album2' value='" + req.getParameter("album2") + "'>");

        for (int i = 0; i < list.size(); i++) {
            out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='estilo'>" + list.get(i) + "");
            out.println("<br>");
        }

        out.println("<input type='radio' value='Todos' name='estilo'>Todos");
        out.println("<br>");
        out.println("<p>");
        out.println("<input type='submit' value='Enviar' >");
        out.println("<br>");
        out.println("<input type='submit' onclick='form.anterior.value=22' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);


    }

    public void resultado2(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        ArrayList<String> list = getCancionesEstilo(req.getParameter("anhio"), req.getParameter("album2"), req.getParameter("estilo"));

        imprimirInicio(out);
        out.println("<h2>Número de canciones</h2><h3>Año: " + req.getParameter("anhio") + "<br>Álbum: " + req.getParameter("album2") + "<br>Estilo: " + req.getParameter("estilo") + "</h3>");
        out.println("<h4>Resultado de su consulta:</h4>");
        out.println("<br>");
        out.println("El número de canciones es: " + list.size());
        out.println("<form method='GET'>");
        out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
        out.println("<input type='hidden' name='anhio' value='" + req.getParameter("anhio") + "'>");
        out.println("<input type='hidden' name='album2' value='" + req.getParameter("album2") + "'>");
        out.println("<input type='hidden' name='Estilo' value='" + req.getParameter("estilo") + "'>");
        out.println("<input type='hidden' name='etapa' value='42'>");

        out.println("<input type='hidden' name='anterior' value='null'>");

        out.println("<p>");
        out.println("<input type='submit' onclick='form.anterior.value=32' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);

    }

    public void imprimirInicio(PrintWriter out) {
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<meta charset='ISO-8859-15'>");
        out.println("<link rel='stylesheet' href='iml.css'>");
        out.println("<link href='https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>");
        out.println("<title>Consulta músical</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<header>");
        out.println("<h1>Servicio web de consulta músical</h1>");
        out.println("</header>");
        out.println("<div>");
        return;
    }

    public void imprimirFinal(PrintWriter out) {
        out.println("</div>");
        out.println("<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 2</footer>");
        out.println("</body>");
        out.println("</html>");
        return;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doGet(req, res);
    }

    /*************************************************************************************************************************************
     * **********************************************************************************************************************************
     *************************************************************************************************************************************/

    public static void processIML(String XML) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        DocumentBuilder db;
        Document doc;
        XML_DTD_ErrorHandler errorHandler = new XML_DTD_ErrorHandler();

        try {
            db = dbf.newDocumentBuilder();

            db.setErrorHandler(errorHandler);

            doc = db.parse(XML);

            listDoc.add(doc);
            NodeList iml = doc.getElementsByTagName("IML");
            for (int i = 0; i < iml.getLength(); i++) {

                if ((!listaXML.contains(iml.item(i).getTextContent()) && !iml.item(i).getTextContent().equals("")) && !listaXMLleidos.contains(iml.item(i).getTextContent())) {
                    listaXML.add(iml.item(i).getTextContent());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {

            listError.add("Error: " + e.toString());
            listFichError.add("Fiechero erróneo: " + XML);
            if (errorHandler.hasError() || errorHandler.hasWarning() || errorHandler.hasFatalError()) {
                listFichError.add(XML);
                listError.add(errorHandler.getMessage());
                errorHandler.clear();
                return;
            }
        }


    }

    /***
     * CONSULTA 1
     *********/
    public static ArrayList<String> getCantantes() {

        ArrayList<String> lista = new ArrayList<>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement(); //Element Interprete
            Node firstChild = element.getFirstChild(); //Primer hijo (#text) del elemento Interprete
            Node nextSibling = firstChild.getNextSibling(); //Hermano -> element Nombre
            Node firstChild1 = nextSibling.getFirstChild();
            Node nombre = firstChild1.getNextSibling(); //Elemento NombreC o NombreG
            lista.add(nombre.getTextContent());
        }
        return lista;
    }

    public static ArrayList<String> getAlbumCantante(String cantante) {
        ArrayList<String> lista = new ArrayList<>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement();
            String nombre = element.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getTextContent();
            if (nombre.equals(cantante) || cantante.equalsIgnoreCase("todos")) {
                NodeList nombreA = doc.getElementsByTagName("NombreA");
                for (int i = 0; i < nombreA.getLength(); i++) {
                    String albumes = nombreA.item(i).getTextContent();
                    lista.add(albumes);
                }
            }
        }
        return lista;
    }

    public static ArrayList<String> getCancionesCantante(String cantante, String album) {
        ArrayList<String> lista = new ArrayList<>();

        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement();
            String nombre = element.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getTextContent();
            if (nombre.equalsIgnoreCase(cantante) || cantante.equalsIgnoreCase("todos")) {
                NodeList canciones = doc.getElementsByTagName("Cancion");
                for (int j = 0; j < canciones.getLength(); j++) {
                    String nombreA = canciones.item(j).getParentNode().getFirstChild().getNextSibling().getTextContent();
                    if (nombreA.equals(album) || album.equalsIgnoreCase("todos")) {
                        NodeList childNodes = canciones.item(j).getChildNodes(); //NODOS CANCION QUE COINCIDEN CON LOS PARAMETROS SELECCIONADOS

                        ArrayList<String> descrp = new ArrayList<>();
                        String nombreC = null;
                        String duracion = null;
                        String descrip = "";

                        for (int i = 0; i < childNodes.getLength(); i++) {
                            if (childNodes.item(i).getNodeName().equals("#text")) { //SELECCIONAR LOS COMENTARIOS DENTRO DEL ELEMENTO CANCION
                                String aux = childNodes.item(i).getTextContent();
                                aux = aux.replaceAll("\n", "").trim();
                                if (!aux.equals(""))
                                    descrp.add(aux);
                            } else {
                                if (childNodes.item(i).getNodeName().equals("NombreT")) { //SACAR EL NOMBRE DE LA CANCION
                                    nombreC = childNodes.item(i).getTextContent();
                                } else {
                                    if (childNodes.item(i).getNodeName().equals("Duracion")) { //SACAR LA DURACION DE LA CANCION
                                        duracion = childNodes.item(i).getTextContent();
                                    }
                                }
                            }
                        }
                        for (String cad : descrp) {
                            descrip = descrip + cad;
                        }
                        String cancion = nombreC + " (" + descrip + "; " + duracion + ")";
                        lista.add(cancion);
                    }
                }
            }
        }

        return lista;
    }

    /*******
     * CONSULTA 2
     *******/
    public static ArrayList<String> getAnhoAlbumes() {

        ArrayList<String> lista = new ArrayList<>();
        for (Document doc : listDoc) {
            NodeList listanho = doc.getElementsByTagName("Año"); //Element Año
            for (int i = 0; i < listanho.getLength(); i++) {
                String anho = listanho.item(i).getTextContent();
                lista.add(anho);
            }
        }
        return lista;
    }

    public static ArrayList<String> getAlbumesPorAnho(String anho) {
        ArrayList<String> lista = new ArrayList();
        for (Document doc : listDoc) {
            NodeList albumes = doc.getElementsByTagName("Album");
            for (int i = 0; i < albumes.getLength(); i++) {
                String Anho = albumes.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getTextContent();
                if (Anho.equals(anho) || anho.equalsIgnoreCase("todos")) {
                    String album = albumes.item(i).getFirstChild().getNextSibling().getTextContent();
                    lista.add(album);
                }
            }
        }
        return lista;
    }

    public static ArrayList<String> getEstilo(String anho, String album) {
        ArrayList<String> lista = new ArrayList();
        for (Document doc : listDoc) {
            NodeList albumes = doc.getElementsByTagName("Album");
            for (int i = 0; i < albumes.getLength(); i++) {
                String Anho = albumes.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getTextContent();
                String nombreA = albumes.item(i).getFirstChild().getNextSibling().getTextContent();
                if ((Anho.equals(anho) || anho.equalsIgnoreCase("todos")) && (nombreA.equals(album) || album.equalsIgnoreCase("todos"))) {
                    Node canciones = albumes.item(i).getLastChild().getPreviousSibling();
                    NamedNodeMap attributes = canciones.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++) {
                        String estilo = attributes.item(j).getTextContent();
                        if (!lista.contains(estilo))
                            lista.add(estilo);
                    }
                }
            }
        }
        return lista;
    }

    public static ArrayList<String> getCancionesEstilo(String anho, String album, String estilo) {
        ArrayList<String> lista = new ArrayList();

        for (Document doc : listDoc) {
            NodeList albumes = doc.getElementsByTagName("Album");
            for (int i = 0; i < albumes.getLength(); i++) {
                String Anho = albumes.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getTextContent();
                String nombreA = albumes.item(i).getFirstChild().getNextSibling().getTextContent();
                if ((Anho.equals(anho) || anho.equalsIgnoreCase("todos")) && (nombreA.equals(album) || album.equalsIgnoreCase("todos"))) {
                    Element disco = (Element) albumes.item(i);
                    NodeList canciones = disco.getElementsByTagName("Cancion"); //TODAS LAS CANCIONES DEL ALBUM SELECIONADO
                    for (int a = 0; a < canciones.getLength(); a++) {
                        NamedNodeMap attributes = canciones.item(a).getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            String Estilo = attributes.item(j).getTextContent();
                            if (Estilo.equals(estilo) || estilo.equalsIgnoreCase("todos")) {

                                NodeList childNodes = canciones.item(a).getChildNodes(); //ELEMENTOS DE LAS CANCIONES QUE COINCIDEN CON LOS PARAMETROS SELECCIONADOS

                                ArrayList<String> descrp = new ArrayList<>();
                                String nombreC = null;
                                String duracion = null;

                                for (int k = 0; k < childNodes.getLength(); k++) {
                                    if (childNodes.item(k).getNodeName().equals("NombreT")) { //SACAR EL NOMBRE DE LA CANCION
                                        nombreC = childNodes.item(k).getTextContent();
                                    }
                                }

                                String song = nombreC;
                                lista.add(song);
                            }
                        }
                    }
                }
            }
        }

        return lista;
    }
}

class XML_DTD_ErrorHandler extends DefaultHandler {

    public boolean error;
    public boolean warning;
    public boolean fatalerror;
    public String message;

    public XML_DTD_ErrorHandler() {
        error = false;
        warning = false;
        fatalerror = false;
        message = null;
    }

    public void warning(SAXParseException spe) {
        warning = true;
        message = "Warning: " + spe.toString();
    }

    public void error(SAXParseException spe) {
        error = true;
        message = "Error: " + spe.toString();
    }

    public void fatalerror(SAXParseException spe) {
        fatalerror = true;
        message = "Fatal Error: " + spe.toString();
    }

    public boolean hasWarning() {
        return warning;
    }

    public boolean hasError() {
        return error;
    }

    public boolean hasFatalError() {
        return fatalerror;
    }

    public String getMessage() {
        return message;
    }

    public void clear() {
        warning = false;
        error = false;
        fatalerror = false;
        message = null;
    }
}
