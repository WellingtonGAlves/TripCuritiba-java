package br.com.tripcuritiba.model;

import java.io.Reader;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class Pessoa implements Comparable<Pessoa>
{
    // Atributos da classe
    private int              id_pessoa;
    private String           nome;
    private Date        data_cadastro;
    private String           email;

    // Métodos construtores
    public Pessoa()
    {
        super();
    }

	public int getId_pessoa() {
		return id_pessoa;
	}

	public void setId_pessoa(int id_pessoa) {
		this.id_pessoa = id_pessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData_cadastro() {
		return data_cadastro;
	}

	public void setData_cadastro(Date data_cadastro) {
		this.data_cadastro = data_cadastro;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	@Override
	public int compareTo(Pessoa o) {
		// TODO Auto-generated method stub
		return 0;
	}

    
}
