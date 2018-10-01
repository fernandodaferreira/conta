package application;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transacao")
public class Transacao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo_transacao;
    private LocalDateTime dt_transacao;
    private long id_conta_debitada;
    private long id_conta_creditada;
    private Double valor;
    @Column(name="codigo_transacao")
    private UUID codigotransacao;    

    public enum TipoTransacao {
    	aporte, transferencia;
    }
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TipoTransacao getTipo_transacao() {
		return tipo_transacao;
	}

	public void setTipo_transacao(TipoTransacao tipo_transacao) {
		this.tipo_transacao = tipo_transacao;
	}

	public LocalDateTime getDt_transacao() {
		return dt_transacao;
	}

	public void setDt_transacao(LocalDateTime dt_transacao) {
		this.dt_transacao = dt_transacao;
	}

	public long getId_conta_debitada() {
		return id_conta_debitada;
	}

	public void setId_conta_debitada(long id_conta_debitada) {
		this.id_conta_debitada = id_conta_debitada;
	}

	public long getId_conta_creditada() {
		return id_conta_creditada;
	}

	public void setId_conta_creditada(long id_conta_creditada) {
		this.id_conta_creditada = id_conta_creditada;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public UUID getCodigo_transacao() {
		return codigotransacao;
	}

	public void setCodigo_transacao(UUID codigo_transacao) {
		this.codigotransacao = codigo_transacao;
	}

	@Override    
	public String
    toString() {
        return "conta" +
                "tipo_transacao=" + id +
                "dt_transacao=" + dt_transacao +
                "id_conta_debitada=" + id_conta_debitada +
                "id_conta_creditada=" + id_conta_creditada +
                "valor=" + valor +
                ", codigo_transacao=" + codigotransacao + "}";
    }
    
}