package org.hackstyle.crawler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hackstyle.vo.Acompanhamento;
import org.hackstyle.vo.Produto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    public List<Produto> pesquisaProduto(String produto) {

        List<Produto> lista = new ArrayList();

        String next = getListagemProdutos("https://lista.mercadolivre.com.br/" + produto, lista);
        while (next != null) {
            next = getListagemProdutos(next, lista);
        }

        return lista;
    }

    public Produto consultaProduto(String link) {

        try {

            Document doc = Jsoup.connect(link).get();
            if (doc != null && doc.hasText()) {

                /* checar valores */
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

                System.out.println("Crawler.consultaProduto() -> " + produto);
                return produto;
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());

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

    private String getListagemProdutos(String url, List<Produto> lista) {

        try {

            Document doc = Jsoup.connect(url).get();
            if (doc != null && doc.hasText()) {

                Crawler crawler = new Crawler();

                Elements elements = doc.select(".results-item");
                if (elements.size() > 0) {

                    for (Element el : elements) {

                        Element el_link = el.select(".item__info-title").first();
                        if (el_link != null) {

                            String link = el.select(".item__info-title").attr("href");

                            Produto produto = crawler.consultaProduto(link);
                            if (produto != null) {
                                lista.add(produto);
                            }
                        }
                    }

                    Element pager = doc.select("li.pagination__next").first();
                    if (pager != null) {

                        String link = pager.select("a").attr("href");

                        if (!link.equals("#")) {
                            return link;
                        }
                    }

                }
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return null;
    }
}
