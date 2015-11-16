import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;


public class Sint12P2 extends HttpServlet{


	public void init(){

	}

	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
			if(req.getParameter("etapa")!=null){ // si no existe el hidden "etapa" es que se ha cargado la pagina desde cero
				String etapa = req.getParameter("etapa");
				if(etapa.equals("0")){ //Comprobamos si se ha pulsado el botón de "Inicio"
					Inicio(out,req,res);
				}else{
					if(req.getParameter("anterior")==null || req.getParameter("anterior").equals("null")){ // si el hidden "anterior" es null es que no se ha pulsado el botón "Atras"
						if(req.getParameter("consultainicial")!=null){ //si no existe el hidden "consultainicial" vamos a la pantalla inicial
							if(req.getParameter("consultainicial").equals("Cantantes")){
								if(etapa.charAt(0)=='2'){
									resultado1(out,req,res);
								}else{
									etapa21(out,req,res);
								}
							}else{
								switch(etapa.charAt(0)){ //comprobar etapa
								case '1':
									etapa22(out,req,res);
									break;
								case '2':
									etapa32(out,req,res);
									break;
								case '3':
									resultado2(out,req,res);
									break;
								}
							}
						}else{ 
							if(req.getParameter("consulta").equals("1")){
								etapa11(out, req, res);
							}else{
								etapa12(out, req, res);
							}
						}
					}else{ //El hidden "anterior" no es null --> comprobar donde se ha pulsado el botón "Atras"
						if(etapa.charAt(1)=='1'){ //consulta 1
							switch(req.getParameter("anterior").charAt(0)){ //comprobar etapa
								case '1':
									etapa11(out,req,res);
									break;
								case '2':
									etapa21(out,req,res);
									break;
							}
						}else{ //consulta 2
							switch(req.getParameter("anterior").charAt(0)){ //comprobar etapa
								case '1':
									etapa12(out,req,res);
									break;
								case '2':
									etapa22(out,req,res);
									break;
								case '3':
									etapa32(out,req,res);
									break;
							}
						}
					} //fin consula atras
				} //fin existe el hidden "etapa"
			}else{
				Inicio(out, req, res);
			}
	}


	/*****************************************************************************************************************************************/


	public void Inicio(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Consulta artistas</h1>");
		out.println("<h1>Selecciona la consulta que desea hacer</h1>");
		out.println("<form method='GET' action='?etapa=1' >");
		out.println("<input type='hidden' name='etapa' value='10'>");
		if(req.getParameter("consultainicial")!=null){
			if(req.getParameter("consultainicial").equals("Cantantes")){
				out.println("<input type='radio' checked='' value='1' name='consulta'>Lista de canciones");
				out.println("<br>");
				out.println("<input type='radio' value='2' name='consulta'>Número de canciones");
				out.println("<br>");
			}else{
				out.println("<input type='radio' value='1' name='consulta'>Lista de canciones");
				out.println("<br>");
				out.println("<input type='radio' checked='' value='2' name='consulta'>Número de canciones");
				out.println("<br>");
			}
		}else{
			out.println("<input type='radio' value='1' checked='' name='consulta'>Lista de canciones");
			out.println("<br>");
			out.println("<input type='radio' value='2' name='consulta'>Número de canciones");
			out.println("<br>");
		}
		out.println("<p>");
		out.println("<input type='submit' value='Enviar' ");
		out.println("<p>");
		out.println("</form>");
		out.println("<hr></hr>");
		out.println("Ruth Guimarey Docampo");
		out.println("</body>");
		out.println("</html>");
	}

	public void etapa11(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>ETAPA 1.1</h1>");
		out.println("<h2>Cantantes</h2>");
		out.println("<form method='GET' action='?etapa=21&consultainicial=Cantantes' >");
		out.println("<input type='hidden' name='etapa' value='11'>");
		out.println("<input type='hidden' name='consultainicial' value='Cantantes'>");
		out.println("<br>");
		out.println("<input type='radio' checked='' value='Cantante 1' name='interprete'>Cantante 1");
		out.println("<br>");
		out.println("<input type='radio' value='Cantante 2' name='interprete'>Cantante 2");
		out.println("<br>");
		out.println("<input type='radio' value='Cantante 3' name='interprete'>Cantante 3");
		out.println("<br>");
		out.println("<input type='radio' value='Todos' name='interprete'>Todos");
		out.println("<br>");
		out.println("<p>");
		out.println("<input type='submit' value='Enviar' >");
		out.println("<br>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	public void etapa21(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>ETAPA 2.1</h1>");


		out.println("<h2>Cantantes --> Cantante: "+req.getParameter("interprete")+"</h2>");
		out.println("<form method='GET' action='?etapa=31&consultainicial=Cantantes&Cantante="+req.getParameter("interprete")+"' >");
		out.println("<input type='hidden' name='consultainicial' value='Cantantes'>");
		out.println("<input type='hidden' name='etapa' value='21'>");
		out.println("<input type='hidden' name='anterior' value='null'>");
		out.println("<input type='hidden' name='interprete' value='"+req.getParameter("interprete")+"'>");

		out.println("<input type='radio' checked='' value='Album 1' name='album1'>Album 1");
		out.println("<br>");
		out.println("<input type='radio' value='Album 2' name='album1'>Album 2");
		out.println("<br>");
		out.println("<input type='radio' value='Album 3' name='album1'>Album 3");
		out.println("<br>");
		out.println("<input type='radio' value='Todos' name='album1'>Todos ");
		out.println("<br>");
		out.println("<p>");
		out.println("<input type='submit' value='Enviar' >");
		out.println("<br>");
		out.println("<input type='submit' onclick='form.anterior.value=11' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}



	public void resultado1(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Consulta de informacion</h1>");
		out.println("<h2>Cantantes --> Cantante: "+req.getParameter("interprete")+"; Álbum: "+req.getParameter("album1")+"</h2>");
		out.println("<h3>Resultado de tu consulta:</h3>");
		out.println("<br>");
		out.println("<ul>");
		
		for(int i=1; i<=10; i++){
			out.println("<il>Cancion "+i+" (duracion, descripcion)</li>");
			out.println("<br>"); 
		}
		out.println("</ul>");
		out.println("<br>");
		out.println("<form method='GET'>");
		out.println("<input type='hidden' name='consultainicial' value='Cantantes'>");
		out.println("<input type='hidden' name='interprete' value='"+req.getParameter("interprete")+"'>");
		out.println("<input type='hidden' name='album-1' value='"+req.getParameter("album1")+"'>");
		out.println("<input type='hidden' name='etapa' value='31'>");

		out.println("<input type='hidden' name='anterior' value='null'>");

		out.println("<p>");
		out.println("<input type='submit' onclick='form.anterior.value=21' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

/*********************************************************************************************
***********************************************************************************************
************************************************************************************************/


	public void etapa12(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>ETAPA 1.2</h1>");
		out.println("<h2>Canciones</h2>");
		out.println("<form method='GET' action='?etapa=22&consultainicial=Canciones' >");
		out.println("<input type='hidden' name='etapa' value='12'>");
		out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
		out.println("<br>");
		out.println("<input type='radio' checked='' value='Año 1' name='anhio'>Año 1");
		out.println("<br>");
		out.println("<input type='radio' value='Año 2' name='anhio'>Año 2");
		out.println("<br>");
		out.println("<input type='radio' value='Año 3' name='anhio'>Año 3");
		out.println("<br>");
		out.println("<input type='radio' value='Todos' name='anhio'>Todos");
		out.println("<br>");
		out.println("<p>");
		out.println("<input type='submit' value='Enviar' >");
		out.println("<br>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	public void etapa22(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>ETAPA 2.2</h1>");
		out.println("<h2>Canciones --> Año: "+req.getParameter("anhio")+"</h2>");
		out.println("<form method='GET' action='?etapa=32&consultainicial=Canciones&anio="+req.getParameter("anhio")+"' >");
		out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
		out.println("<input type='hidden' name='etapa' value='22'>");

		out.println("<input type='hidden' name='anterior' value='null'>");
		
		out.println("<input type='hidden' name='anhio' value='"+req.getParameter("anhio")+"'>");

		out.println("<input type='radio' checked='' value='Album 1' name='album2'>Album 1");
		out.println("<br>");
		out.println("<input type='radio' value='Album 2' name='album2'>Album 2");
		out.println("<br>");
		out.println("<input type='radio' value='Album 3' name='album2'>Album 3");
		out.println("<br>");
		out.println("<input type='radio' value='Todos' name='album2'>Todos");
		out.println("<br>");
		out.println("<p>");
		out.println("<input type='submit' value='Enviar' >");
		out.println("<br>");
		out.println("<input type='submit' onclick='form.anterior.value=12' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	public void etapa32(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>ETAPA 3.2</h1>");
		out.println("<h2>Canciones --> Año: "+req.getParameter("anhio")+"; Álbum: "+req.getParameter("album2")+"</h2>");
		out.println("<form method='GET' action='?etapa=42&consultainicial=Canciones&anio="+req.getParameter("anhio")+"album="+req.getParameter("album2")+"' >");
		out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
		out.println("<input type='hidden' name='anhio' value='"+req.getParameter("anhio")+"'>");
		out.println("<input type='hidden' name='etapa' value='32'>");

		out.println("<input type='hidden' name='anterior' value='null'>");
		
		out.println("<input type='hidden' name='album2' value='"+req.getParameter("album2")+"'>");

		out.println("<input type='radio' checked='' value='Estilo 1' name='estilo'>Estilo 1");
		out.println("<br>");
		out.println("<input type='radio' value='Estilo 2' name='estilo'>Estilo 2");
		out.println("<br>");
		out.println("<input type='radio' value='Estilo 3' name='estilo'>Estilo 3");
		out.println("<br>");
		out.println("<input type='radio' value='Todos' name='estilo'>Todos");
		out.println("<br>");
		out.println("<p>");
		out.println("<input type='submit' value='Enviar' >");
		out.println("<br>");
		out.println("<input type='submit' onclick='form.anterior.value=22' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	public void resultado2(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Consulta de informacion</h1>");
		out.println("<h2>Canciones --> Año: "+req.getParameter("anhio")+"; Álbum: "+req.getParameter("album2")+"; Estilo: "+req.getParameter("estilo")+"</h2>");
		out.println("<h3>Resultado de tu consulta:</h3>");
		out.println("<br>");
		out.println("El número de canciones es: 10");
		out.println("<form method='GET'>");
		out.println("<input type='hidden' name='consultainicial' value='Canciones'>");
		out.println("<input type='hidden' name='anhio' value='"+req.getParameter("anhio")+"'>");
		out.println("<input type='hidden' name='album2' value='"+req.getParameter("album2")+"'>");
		out.println("<input type='hidden' name='Estilo' value='"+req.getParameter("estilo")+"'>");
		out.println("<input type='hidden' name='etapa' value='42'>");

		out.println("<input type='hidden' name='anterior' value='null'>");

		out.println("<p>");
		out.println("<input type='submit' onclick='form.anterior.value=32' value='Atrás'>");
		out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		doGet(req,res);
	}

}
