package org.hackstyle.servlet;

import org.hackstyle.vo.Produto;
import org.hackstyle.database.DAOProduto;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hackstyle.crawler.Crawler;
import org.hackstyle.servico.Servico;
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

        
/*        
        Servico s = new Servico();
        try {
            s.iniciaServico();
            
        } catch (ParseException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
  */      
        
        
        
        String keyword = request.getParameter("keyword");
        List<Produto> lista = new ArrayList();

        String next = getListagemProdutos("https://lista.mercadolivre.com.br/" + keyword, lista);
        while (next != null) {

            next = getListagemProdutos(next, lista);
            System.out.println("tem next!!");
        }

        try {

            String resp = new Gson().toJson(lista);
            out.print(resp);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private String getListagemProdutos(String url, List<Produto> lista) {

        try {

            Document doc = Jsoup.connect(url).get();
            if (doc != null && doc.hasText()) {

                Elements lis = doc.select(".results-item");
                Crawler crawler = new Crawler();
                
                for (Element li : lis) {

                    /*String id = filtraID(li.select("div.rowItem").attr("id"));
                    Element title = li.select(".item__title .main-title").first();
                    Element price = li.select(".item__price .price__fraction").first();
                    Element vendidos = li.select(".item__condition").first();*/
                    
                    String link = li.select(".item__info-title").attr("href");

                    /*Produto p = new Produto();
                    p.setId(id);
                    p.setNome(title.text());
                    p.setPreco(Float.parseFloat(price.text()));
                    p.setQtdeVendidos(Integer.parseInt(getQuantidade(vendidos.text())));
                    p.setLink(link);
                    
                    String vendedor = getNomeVendedor(p);
                    if (vendedor != null)
                        p.setNomeVendedor(vendedor);
                    */
                    
                    Produto produto = crawler.consultaProduto(link);                    
                    lista.add(produto);
                    
                    DAOProduto dao = new DAOProduto();
                    dao.insert(produto);
                }

                Element pager = doc.select("li.pagination__next").first();
                if (pager != null) {
                    System.out.println("pager ***********" + pager.html());
                } else {
                    System.out.println("pager null");
                }

                String link = pager.select("a").attr("href");
                return link;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        return null;
    }

    
    private String getQuantidade(String item) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {

            char chr = item.charAt(i);
            if (chr >= '0' && chr <= '9') {
                sb.append(chr);
            } else {
                break;
            }
        }

        if (sb.length() < 1) {
            sb.append("0");
        }

        return sb.toString();
    }

    
    private String getNomeVendedor(Produto produto) {

        try {

            Document doc = Jsoup.connect(produto.getLink()).get();
            if (doc != null && doc.hasText()) {

                String linkVendedor = doc.select(".reputation-view-more").first().attr("href");
                String nomeVendedor = new URL(linkVendedor).getPath();
                
                nomeVendedor = nomeVendedor.substring(1, nomeVendedor.length());
                
                System.out.println(linkVendedor);
                System.out.println(nomeVendedor);
                
                return nomeVendedor;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
    private String filtraID(String id) {
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {

            char chr = id.charAt(i);
            if (chr >= '0' && chr <= '9') {
                sb.append(chr);
            }
        }
        return sb.toString();        
    }
}






/* tabela produtos

CREATE TABLE produtos (
    id VARCHAR(32) NOT NULL PRIMARY KEY, 
    nome VARCHAR(128) NOT NULL, 
    preco REAL NOT NULL, 
    qtde_vendidos INTEGER NOT NULL, 
    link VARCHAR(256) NOT NULL, 
    id_vendedor INTEGER NOT NULL,
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);

SELECT id, nome, preco, qtde_vendidos, link, id_vendedor, data_cadastro FROM produtos;


CREATE TABLE acompanhamento (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
    id_produto VARCHAR(32) NOT NULL,  
    preco REAL NOT NULL, 
    qtde_vendidos INTEGER NOT NULL,
    data DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE vendedor (id int not null primary key, nome varchar(64) not null, data_cadastro datetime);

*/