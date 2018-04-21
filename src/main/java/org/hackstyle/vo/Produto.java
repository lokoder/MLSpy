package org.hackstyle.vo;

import java.util.Date;

public class Produto {
    
    private String id;
    private String nome;
    private double preco;
    private int qtdeVendidos;
    private String link;
    private String nomeVendedor;
    private Date dataCadastro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }   
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtdeVendidos() {
        return qtdeVendidos;
    }

    public void setQtdeVendidos(int qtdeVendidos) {
        this.qtdeVendidos = qtdeVendidos;
    }       

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }    

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    
    @Override
    public String toString() {
        
        return id + " - " + nome + " - " + preco + " - " + qtdeVendidos + " - " + link + " - " + nomeVendedor;
    }
}
