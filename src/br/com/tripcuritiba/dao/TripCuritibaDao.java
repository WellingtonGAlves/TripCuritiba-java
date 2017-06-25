package br.com.tripcuritiba.dao;

import java.util.List;

import br.com.tripcuritiba.model.Pessoa;

public interface TripCuritibaDao
{
    // Método para recuperar um objeto da base de dados (SELECT WHERE PRIMARY KEY)
    public abstract Pessoa recovery(int pId);

    // Método para pesquisar todos os objetos da base de dados (SELECT)
    public abstract List<Pessoa> search();
}
