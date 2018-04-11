package org.hackstyle.servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@WebServlet(name = "Search", urlPatterns = {"/Search"})
public class Search extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {                

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String keyword = request.getParameter("search");
        
        List<Produto> lista = new ArrayList();
        
        try {
            Document doc = Jsoup.connect("https://lista.mercadolivre.com.br/" + keyword).get();
            if (doc != null && doc.hasText()) {
                
                Elements lis = doc.select(".results-item");
                
                for (Element li : lis) {
                    
                    Element title = li.select(".item__title .main-title").first();
                    Element price = li.select(".item__price .price__fraction").first();
                    Element vendidos = li.select(".item__condition").first();
                    
                    Produto p = new Produto();
                    p.setNome(title.toString());
                    p.setPreco(price.toString());
                    p.setVendidos(vendidos.toString());
                    
                    lista.add(p);                    
                }
                
                
                String resp = new Gson().toJson(lista);
                out.print(resp);
                                
            } else {
                out.print(keyword);    
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
