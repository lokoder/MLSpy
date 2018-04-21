package org.hackstyle.vo;

import java.util.Date;

public class Acompanhamento {

    private String id;
    private Date data;
    private double preco;
    private int qtdeVendidos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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
    
    
    public String toString() {
        
        return id + " - " + preco + " - " + qtdeVendidos + " - " + data;
    }
    
}
