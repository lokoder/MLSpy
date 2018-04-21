package org.hackstyle.crawler;

import java.net.URL;
import org.hackstyle.vo.Acompanhamento;
import org.hackstyle.vo.Produto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {

    
    public Produto consultaProduto(String link) {
    
        try {

            Document doc = Jsoup.connect(link).get();
            if (doc != null && doc.hasText()) {

                String id = filtraID(doc.select("span.item-info__id-number").first().text());
                String nome = doc.select("h1.item-title__primary").first().text();
                String preco = doc.select("span.price-tag-fraction").first().text();
                String qtdeVendidos = getQuantidade(doc.select("div.item-conditions").first().text());
                String linkVendedor = doc.select(".reputation-view-more").first().attr("href");
                String nomeVendedor = new URL(linkVendedor).getPath();
                nomeVendedor = nomeVendedor.substring(1, nomeVendedor.length());
                
                
                Produto produto = new Produto();
                produto.setId(id);
                produto.setNome(nome);
                produto.setPreco(Double.parseDouble(preco));
                produto.setLink(link);
                produto.setQtdeVendidos(Integer.parseInt(qtdeVendidos));
                produto.setNomeVendedor(nomeVendedor);

                System.out.println("Crawler.getProduto() -> " + produto);
                return produto;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        return null;        
    }
    
    public Acompanhamento consultaAcompanhamento(String link) {

        try {

            Document doc = Jsoup.connect(link).get();
            if (doc != null && doc.hasText()) {

                String id = filtraID(doc.select("span.item-info__id-number").first().text());
                String nome = doc.select("h1.item-title__primary").first().text();
                String preco = doc.select("span.price-tag-fraction").first().text();
                String qtdeVendidos = getQuantidade(doc.select("div.item-conditions").first().text());

                Acompanhamento acompanhamento = new Acompanhamento();
                acompanhamento.setId(id);
                acompanhamento.setPreco(Double.parseDouble(preco));
                acompanhamento.setQtdeVendidos(Integer.parseInt(qtdeVendidos));

                System.out.println("Crawler.getProduto() -> " + acompanhamento);
                return acompanhamento;

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        return null;

    }

    private String getQuantidade(String item) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < item.length(); i++) {

            char chr = item.charAt(i);
            if (chr >= '0' && chr <= '9') {
                sb.append(chr);
            }
        }

        if (sb.length() < 1) {
            sb.append("0");
        }

        return sb.toString();
    }

    private String filtraID(String id) {

        return id.substring(1, id.length());
    }

}
