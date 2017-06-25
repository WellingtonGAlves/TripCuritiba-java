package br.com.tripcuritiba.jdbc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import br.com.tripcuritiba.util.ExceptionUtil;

public class Conexao
{
    // Atributos estáticos
    private static Connection sConexao;

    // Métodos estáticos
    public static void main(String[] args)
    {
        System.out.println();
        System.out.println("Obtendo a conexão");
        getConexao();

        System.out.println();
        System.out.println("Fechando a conexão");
        desconectar();
    }

    // Métodos de classe
    public static Connection getConexao()
    {
        try
        {
            // Caso a conexão já exista, verifica se está aberta e retorna essa conexão
            if (sConexao != null)
            {
                if (!sConexao.isClosed())
                    return sConexao;
            }

            // Carregando o arquivo de configuração do JDBC para as propriedades
            InputStream tArqEntrada = Conexao.class.getResourceAsStream("jdbc.properties");
            ;
            if (tArqEntrada == null)
                throw new IOException("Arquivo de configuração 'jdbc.properties' não existe no diretório do pacote");
            Properties tPropriedades = new Properties();
            tPropriedades.load(tArqEntrada);
            tArqEntrada.close();

            // Recuperando as propriedades do arquivo e validando se estão ok
            String tDriver = tPropriedades.getProperty("driver");
            if (tDriver == null || tDriver.isEmpty())
                throw new InvalidPropertiesFormatException("Propriedade 'driver' não existe ou o arquivo 'jdbc.properties' está em branco");
            String tURL = tPropriedades.getProperty("url");
            if (tURL == null || tURL.isEmpty())
                throw new InvalidPropertiesFormatException("Propriedade 'url' não existe ou o arquivo 'jdbc.properties' está em branco");
            String tNomeLog = tPropriedades.getProperty("log");
            String tUsuario = tPropriedades.getProperty("usuario");
            String tSenha = tPropriedades.getProperty("senha");

            // Assinalando o arquivo de log, caso ele esteja configurado
            if (tNomeLog != null && !tNomeLog.isEmpty())
            {
                PrintWriter tLog = new PrintWriter(new FileWriter(tNomeLog, true));
                DriverManager.setLogWriter(tLog);
                DriverManager.println("|-------> Iniciando log.....");
            }

            // Carregando o driver e fazendo a conexão com o banco de dados
            DriverManager.println("|-------> Registrando o driver.....");
            Class.forName(tDriver);
            DriverManager.println("|-------> Realizando a conexão.....");
            sConexao = DriverManager.getConnection(tURL, tUsuario, tSenha);
            DriverManager.println("|-------> Conexão estabelecida.....");
        }
        catch (ClassNotFoundException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Classe do Driver não encontrada");
            System.exit(9);
        }
        catch (IOException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Problemas na criação do arquivo de log");
            System.exit(9);
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Problemas na conexão com o banco de dados");
            System.exit(9);
        }
        return sConexao;
    }

    public static void desconectar()
    {
        try
        {
            // Desconectando do banco de dados
            DriverManager.println("|-------> Fechando a conexão.....");
            sConexao.close();

            // Verificando se a desconexão foi bem realizada
            DriverManager.println("|-------> Verificando a conexão após fechamento");
            if (sConexao.isClosed())
            {
                DriverManager.println("|-------> Conexão fechada");
                sConexao = null;
            }
            else
            {
                DriverManager.println("|-------> Conexão aberta");
            }
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Problemas no fechamento da conexão com o banco de dados");
            System.exit(9);
        }
    }

    public static void iniciarTransacao()
    {
        try
        {
            sConexao.setAutoCommit(false);
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Problemas no inicio da tansação com o banco de dados");
            System.exit(9);
        }
    }

    public static void confirmarTransacao()
    {
        try
        {
            sConexao.commit();
            sConexao.setAutoCommit(true);
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Problemas na confimacão da transação no banco de dados");
            System.exit(9);
        }
    }

    public static void desfazerTransacao()
    {
        try
        {
            sConexao.rollback();
            sConexao.setAutoCommit(true);
        }
        catch (SQLException tExcept)
        {
            ExceptionUtil.mostrarErro(tExcept, "Problemas para desfazer a transação no banco de dados");
            System.exit(9);
        }
    }
}
