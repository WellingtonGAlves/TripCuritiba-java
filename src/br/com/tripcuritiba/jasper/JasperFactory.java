package br.com.tripcuritiba.jasper;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

public class JasperFactory
{
    private static JasperReport sRelacaoPessoas;

    public static JasperReport getRelatorioPessoas() throws JRException
    {
        if (sRelacaoPessoas == null)
        {
            try
            {
                // Abrindo o arquivo JRXML
                InputStream tArqEntrada = JasperFactory.class.getResourceAsStream("RelatorioPessoasTripCuritiba.jrxml");

                // Compilando o arquivo JRXML
                sRelacaoPessoas = JasperCompileManager.compileReport(tArqEntrada);

                // Fechando o arquivo JRXML
                tArqEntrada.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // Retornando o relatório compilado
        return sRelacaoPessoas;
    }
}
