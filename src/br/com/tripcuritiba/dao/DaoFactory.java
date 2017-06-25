package br.com.tripcuritiba.dao;

import br.com.tripcuritiba.dao.jdbc.TripCuritibaDaoJdbc;

public class DaoFactory
{
    public static TripCuritibaDao getPessoaDao()
    {
        return new TripCuritibaDaoJdbc();
    }
}
