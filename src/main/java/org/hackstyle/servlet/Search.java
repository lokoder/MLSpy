package org.hackstyle.servlet;

import org.hackstyle.vo.Produto;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hackstyle.crawler.Crawler;

@WebServlet(name = "Search", urlPatterns = {"/Search"})
public class Search extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        Crawler crawler = new Crawler();
        
        String keyword = request.getParameter("keyword");
        List<Produto> listaProduto = crawler.pesquisaProduto(keyword);
        
        try {
            String resp = new Gson().toJson(listaProduto);
            out.print(resp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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