package application;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conta")
public class Conta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String nome;
    private LocalDateTime dt_criacao;
    private long cnpj;
    private String razao_social;
    private String nome_fantasia;
    private long cpf;
    private String nome_completo;
    private LocalDateTime dt_nascimento;
    private long id_conta_pai;
    @Enumerated(EnumType.STRING)
    private TipoConta tipo_conta;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime dt_status;
    private Double saldo;    
    
    public enum Status {
    	ativo, bloqueada, cancelada;
    }

    public enum TipoConta {
    	matriz, filial;
    }
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDateTime getDt_criacao() {
		return dt_criacao;
	}

	public void setDt_criacao(LocalDateTime dt_criacao) {
		this.dt_criacao = dt_criacao;
	}

	public long getCnpj() {
		return cnpj;
	}

	public void setCnpj(long cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public String getNome_fantasia() {
		return nome_fantasia;
	}

	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}

	public long getCpf() {
		return cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public String getNome_completo() {
		return nome_completo;
	}

	public void setNome_completo(String nome_completo) {
		this.nome_completo = nome_completo;
	}

	public LocalDateTime getDt_nascimento() {
		return dt_nascimento;
	}

	public void setDt_nascimento(LocalDateTime dt_nascimento) {
		this.dt_nascimento = dt_nascimento;
	}

	public long getId_conta_pai() {
		return id_conta_pai;
	}

	public void setId_conta_pai(long id_conta_pai) {
		this.id_conta_pai = id_conta_pai;
	}

	public TipoConta getTipo_conta() {
		return tipo_conta;
	}

	public void setTipo_conta(TipoConta tipo_conta) {
		this.tipo_conta = tipo_conta;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getDt_status() {
		return dt_status;
	}

	public void setDt_status(LocalDateTime dt_status) {
		this.dt_status = dt_status;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	@Override    
	public String
    toString() {
        return "conta" +
                "id=" + id +
                "nome=" + nome +
                "dt_criacao=" + dt_criacao +
                "cnpj=" + String.valueOf(cnpj) +
                "razao_social=" + razao_social +
                "nome_fantasia=" + nome_fantasia +
                "cpf=" + String.valueOf(cpf) +
                "nome_completo=" + nome_completo +
                "dt_nascimento=" + dt_nascimento +
                "id_conta_pai=" + id_conta_pai +
                "tipo_conta=" + String.valueOf(tipo_conta) +
                "status=" + status +
                "dt_status=" + dt_status +
                ", saldo=" + String.valueOf(saldo) + "}";
    }
    
}