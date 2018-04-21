package org.hackstyle.servlet;

import org.hackstyle.vo.Produto;
import org.hackstyle.database.DAOProduto;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        DAOProduto dAOProduto = new DAOProduto();
        try {
            /*List<Produto> lista = dAOProduto.getAll();
            for (Produto p : lista) {
                
                System.out.println(p.toString());
                
            }*/
            System.out.println(dAOProduto.getByID("MLB1009501018"));
            
        } catch (SQLException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
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

                for (Element li : lis) {

                    String id = li.select("div.rowItem").attr("id");
                    Element title = li.select(".item__title .main-title").first();
                    Element price = li.select(".item__price .price__fraction").first();
                    Element vendidos = li.select(".item__condition").first();
                    String link = li.select(".item__info-title").attr("href");

                    Produto p = new Produto();
                    p.setId(id);
                    p.setNome(title.text());
                    p.setPreco(Float.parseFloat(price.text()));
                    p.setQtdeVendidos(Integer.parseInt(getQuantidade(vendidos.text())));
                    p.setLink(link);
                    
                    String vendedor = getNomeVendedor(p);
                    if (vendedor != null)
                        p.setNomeVendedor(vendedor);
                    
                    lista.add(p);
                    
                    DAOProduto dao = new DAOProduto();
                    dao.insert(p);
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
}






/* tabela produtos

CREATE TABLE produtos (
    id varchar(32) not null, 
    nome varchar(128) not null, 
    preco real not null, 
    qtde_vendidos integer not null, 
    link varchar(256) not null, 
    id_vendedor int not null,
    data_cadastro date not null,
    PRIMARY KEY(id));

SELECT id, nome, preco, qtde_vendidos, link, id_vendedor, data_cadastro FROM produtos;

CREATE TABLE acompanhamento (
    id integer not null autoincrement, 
    id_produto varchar(32) not null, 
    data_consulta datetime not null, 
    preco real not null, 
    qtde_vendidos int not null,
    PRIMARY KEY(id));

CREATE TABLE vendedor (id int not null primary key, nome varchar(64) not null, data_cadastro datetime);

*/