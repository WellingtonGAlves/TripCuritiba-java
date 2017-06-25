package br.com.tripcuritiba.controller;

import java.util.List;

import br.com.tripcuritiba.dao.DaoFactory;
import br.com.tripcuritiba.dao.TripCuritibaDao;
import br.com.tripcuritiba.dto.TripCuritibaDTO;
import br.com.tripcuritiba.model.Pessoa;

public class TripCuritibaController
{
    // Método para recuperar um Evento
    public static TripCuritibaDTO recuperar(int pId)
    {
        // Lendo o objeto
        TripCuritibaDao tDao = DaoFactory.getPessoaDao();
        Pessoa pessoa = tDao.recovery(pId);

        // Verificando se houve erro de recuperação
        if (pessoa == null)
            return new TripCuritibaDTO(false, "Pessoa não existe no cadastro");

        // Retornando o indicativo de sucesso com o objeto recuperado
        return new TripCuritibaDTO(true, "Pessoa lida com sucesso", pessoa);
    }

    // Método para pesquisar todos os Pessoas
    public static TripCuritibaDTO pesquisar()
    {
        // Obtendo a lista de Pessoas
        TripCuritibaDao tDao = DaoFactory.getPessoaDao();
        List<Pessoa> tLista = tDao.search();

        // Verificando se a lista está vazia
        if (tLista.isEmpty())
            return new TripCuritibaDTO(false, "Lista de Pessoas vazia");

        // Retornando a lista obtida
        return new TripCuritibaDTO(true, "Lista de Pessoas recuperada", tLista);
    }
}
