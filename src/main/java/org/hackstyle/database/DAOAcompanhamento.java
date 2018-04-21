package org.hackstyle.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hackstyle.vo.Acompanhamento;


public class DAOAcompanhamento {
    
    
    public List<Acompanhamento> getById(String id) throws SQLException, ClassNotFoundException {
        
        Connection conn = SQLite.getInstance().getConnection();
        String query = "SELECT id_produto, data_consulta, preco, qtde_vendidos FROM acompanhamento WHERE id_produto = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, id);

        ResultSet rs = stmt.executeQuery();
        
        List<Acompanhamento> listaAcompanhamento = new ArrayList<>();
        
        while (rs.next()) {

            Acompanhamento acompanhamento = new Acompanhamento();
            acompanhamento.setId(rs.getString("id"));
            acompanhamento.setData(rs.getDate("data_consulta"));
            acompanhamento.setPreco(rs.getDouble("preco"));
            acompanhamento.setQtdeVendidos(rs.getInt("qtde_vendidos"));            
            
            listaAcompanhamento.add(acompanhamento);            
        }
        
        return listaAcompanhamento;
    }
    
    
    public void insert(Acompanhamento acompanhamento) throws SQLException, ClassNotFoundException {
        
        Connection conn = SQLite.getInstance().getConnection();
        String query = "INSERT INTO acompanhamento (id_produto, data_consulta, preco, qtde_vendidos) " +
                "VALUES (?,?,?,?)";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, acompanhamento.getId());
        stmt.setDate(2, new java.sql.Date(acompanhamento.getData().getTime()));
        stmt.setDouble(3, acompanhamento.getPreco());
        stmt.setInt(4, acompanhamento.getQtdeVendidos());
        
        stmt.executeUpdate();
    }
    
}
