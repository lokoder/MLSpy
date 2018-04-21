package org.hackstyle.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.hackstyle.vo.Produto;


public class DAOProduto {

    
    public Produto getByID(String id) throws SQLException, ClassNotFoundException {
    
        Connection conn = SQLite.getInstance().getConnection();
        String query = "SELECT id, nome, preco, qtde_vendidos, link, id_vendedor, data_cadastro FROM produtos WHERE id = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, id);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
        
            Produto produto = new Produto();
            produto.setId(rs.getString("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco(rs.getDouble("preco"));
            produto.setLink(rs.getString("link"));
            produto.setNomeVendedor(rs.getString("id_vendedor"));
            produto.setPreco(rs.getInt("preco"));
            produto.setQtdeVendidos(rs.getInt("qtde_vendidos"));
            produto.setDataCadastro(rs.getDate("data_cadastro"));
                
            return produto;
        }
        
        return null;        
    }
    
    
    public List<Produto> getAll() throws SQLException, ClassNotFoundException, ParseException {
        
        Connection conn = SQLite.getInstance().getConnection();
        String query = "SELECT id, nome, preco, qtde_vendidos, link, id_vendedor, data_cadastro FROM produtos";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        List<Produto> listaProdutos = new ArrayList<>();
        
        while (rs.next()) {
        
            Produto produto = new Produto();
            produto.setId(rs.getString("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco(rs.getDouble("preco"));
            produto.setLink(rs.getString("link"));
            produto.setNomeVendedor(rs.getString("id_vendedor"));
            produto.setPreco(rs.getInt("preco"));
            produto.setQtdeVendidos(rs.getInt("qtde_vendidos"));
            //produto.setDataCadastro(rs.getDate("data_cadastro"));
            String data = rs.getString("data_cadastro");
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            produto.setDataCadastro(format.parse(data));
            listaProdutos.add(produto);            
        }
        
        return listaProdutos;
    }
    
    
    public void insert(Produto produto) throws SQLException, ClassNotFoundException {
        
        Connection conn = SQLite.getInstance().getConnection();
        String query = "INSERT INTO produtos (id, nome, preco, qtde_vendidos, link, id_vendedor) " +
                "VALUES (?,?,?,?,?,?)";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, produto.getId());
        stmt.setString(2, produto.getNome());
        stmt.setDouble(3, produto.getPreco());
        stmt.setInt(4, produto.getQtdeVendidos());
        stmt.setString(5, produto.getLink());
        stmt.setInt(6, 1); /***********************************id_vendedor */
        //stmt.setString(7, "2018-01-01"); /******************************** data_cadastro*/
        
        stmt.executeUpdate();
    }
    
}
