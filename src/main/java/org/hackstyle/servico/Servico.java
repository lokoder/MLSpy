package org.hackstyle.servico;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hackstyle.crawler.Crawler;
import org.hackstyle.database.DAOAcompanhamento;
import org.hackstyle.database.DAOProduto;
import org.hackstyle.vo.Acompanhamento;
import org.hackstyle.vo.Produto;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Servico implements org.quartz.Job {

    private void iniciaServico() throws ParseException {

        try {
            
            DAOProduto daoProduto = new DAOProduto();
            List<Produto> listaProduto = daoProduto.getAll();

            for (Produto produto : listaProduto) {

                Crawler crawler = new Crawler();
                Acompanhamento acompanhamento = crawler.consultaAcompanhamento(produto.getLink());

                DAOAcompanhamento daoAcompanhamento = new DAOAcompanhamento();
                daoAcompanhamento.insert(acompanhamento);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Servico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        
        try {
            System.out.println("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}");
            iniciaServico();
        } catch (ParseException ex) {
            Logger.getLogger(Servico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
