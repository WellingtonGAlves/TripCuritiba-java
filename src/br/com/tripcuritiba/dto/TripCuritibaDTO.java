package br.com.tripcuritiba.dto;

import java.util.List;

import br.com.tripcuritiba.model.Pessoa;

public class TripCuritibaDTO extends AbstractDTO<Pessoa>
{
    // Métodos construtores
    public TripCuritibaDTO()
    {
        super();
    }

    public TripCuritibaDTO(boolean pOk, String pMensagem)
    {
        super(pOk, pMensagem);
    }

    public TripCuritibaDTO(boolean pOk, String pMensagem, Pessoa pPessoa)
    {
        super(pOk, pMensagem, pPessoa);
    }

    public TripCuritibaDTO(boolean pOk, String pMensagem, List<Pessoa> pLista)
    {
        super(pOk, pMensagem, pLista);
    }

    // Métodos de acesso
    public Pessoa getPessoa()
    {
        return getObjeto();
    }

    public void setPessoa(Pessoa pPessoa)
    {
        setObjeto(pPessoa);
    }
}
