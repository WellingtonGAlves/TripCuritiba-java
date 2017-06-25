package br.com.tripcuritiba.dao.jdbc;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.tripcuritiba.dao.TripCuritibaDao;
import br.com.tripcuritiba.jdbc.Conexao;
import br.com.tripcuritiba.model.Pessoa;
import br.com.tripcuritiba.util.ExceptionUtil;

public class TripCuritibaDaoJdbc implements TripCuritibaDao
{
    // Atributos estÃ¡ticos
    private static String sTabela = "PESSOA";
    private static String sCampos1 = "ID_PESSOA, NOME, EMAIL, DATA_CADASTRO";
    private static String sPrimaryKey = "ID_PESSOA = ?";
    private static String sOrdem = "ORDER BY UPPER(NOME)";

    // Conexão com o Oracle
    private Connection sConexao = Conexao.getConexao();

    // Método para recuperar um objeto da base de dados (SELECT WHERE PRIMARY KEY)
    @Override
    public Pessoa recovery(int pId)
    {
        // Definindo o objeto de retorno
        Pessoa tObjeto = null;

        try
        {
            // Criando o comando SQL e o comando JDBC
            String tComandoSQL = "SELECT " + sCampos1 +
                                 " FROM " + sTabela +
                                 " WHERE " + sPrimaryKey;
            PreparedStatement tComandoJDBC = sConexao.prepareStatement(tComandoSQL);

            // Colocando o parâmetro recebido no comando JDBC
            tComandoJDBC.setInt(1, pId);

            // Executando o comando e salvando o ResultSet para processar
            ResultSet tResultSet = tComandoJDBC.executeQuery();

            // Verificando se um registro foi lido
            if (tResultSet.next())
            {
                // Salvando o objeto para retornar depois
                tObjeto = carregarObjeto(tResultSet);
            }

            // Liberando os recursos JDBC
            tResultSet.close();
            tComandoJDBC.close();
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Erro no método de recuperação do objeto");
        }

        // Retornando o objeto
        return tObjeto;
    }

    // Método para pesquisar todos os objetos da base de dados (SELECT)
    @Override
    public List<Pessoa> search()
    {
        // Criando a lista de objetos vazia
        List<Pessoa> tLista = new ArrayList<>();

        try
        {
            // Criando o comando SQL e o comando JDBC
            String tComandoSQL = "SELECT " + sCampos1 +
                                 " FROM " + sTabela;
            PreparedStatement tComandoJDBC = sConexao.prepareStatement(tComandoSQL);

            // Executando o comando e salvando o ResultSet para processar
            ResultSet tResultSet = tComandoJDBC.executeQuery();

            // Enquanto houver registros, processa
            while (tResultSet.next())
            {
                // Salvando o objeto retornado para adicionar na lista
                Pessoa tObjeto = carregarObjeto(tResultSet);

                // Adicionando o objeto na lista
                tLista.add(tObjeto);
            }

            // Liberando os recursos JDBC
            tResultSet.close();
            tComandoJDBC.close();
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Erro no método de recuperação da lista de objetos");
        }

        // Retornando a lista de objetos
        return tLista;
    }

    // MÃ©todo para processar o ResultSet e retornar um objeto
    private Pessoa carregarObjeto(ResultSet tResultSet) throws SQLException
    {
        // Criando um novo objeto para armazenar as informaÃ§Ãµes lidas
        Pessoa pessoa = new Pessoa();
		String data = null;

        // Recuperando as informaÃ§Ãµes do ResultSet e colocando no objeto criado
        
        pessoa.setNome(tResultSet.getString("NOME"));
        pessoa.setEmail(tResultSet.getString("EMAIL"));
     // convertendo a data do banco para mostrar na view
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH mm ss ");
		data = sdf.format(tResultSet.getTimestamp("DATA_CADASTRO"));
		try {
			pessoa.setData_cadastro(sdf.parse(data));
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        // Retornando o objeto criado
        return pessoa;
    }
}
