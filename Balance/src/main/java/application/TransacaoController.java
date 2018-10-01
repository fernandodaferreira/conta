package application;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import application.Conta.TipoConta;
import application.Transacao.TipoTransacao;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("transacao")
public class TransacaoController {

    private static final Logger log = LoggerFactory.getLogger(TransacaoController.class);
    @Autowired 
    private TransacaoRepository transacaoRepository;
    @Autowired 
    private ContaRepository contaRepository;
    
	@ApiOperation(value = "Buscar todas as transacoes", response = Conta.class, notes = "Essa operação busca todas as transacoes no banco de dados.")
    @GetMapping("/transacoes")
    public List<Transacao> retrieveAllTransacoes() {
    	log.info("Retrieve All Transacoes");
    	log.info("-------------------");
    	return (List<Transacao>) transacaoRepository.findAll();
    }
    
    @PostMapping("/transacao")
    public ResponseEntity<Object> createTransacao(@RequestBody Transacao transacao) {
    	log.info("Create Transacao : " + transacao.toString());
    	log.info("-----------------------------------");
    	Optional<Conta> contaCreditada = null;
    	Optional<Conta> contaDebitada = null;
    	
    	// aporte em conta matriz
    	if (contaRepository.findById(transacao.getId_conta_creditada()).isPresent()) {
    		contaCreditada = contaRepository.findById(transacao.getId_conta_creditada());		
        	if (contaCreditada.get().getTipo_conta() == TipoConta.matriz) {
        		if (transacao.getTipo_transacao() == TipoTransacao.aporte) {
            		contaCreditada.get().setSaldo(contaCreditada.get().getSaldo() + transacao.getValor());
        		}else {
        			return new ResponseEntity<Object>("Conta do tipo Matriz não pode receber transferencia", HttpStatus.INTERNAL_SERVER_ERROR);
        		}
        	}
    	}
    	
    	// transferencia entre contas
    	if ((contaRepository.findById(transacao.getId_conta_debitada()).isPresent()) && (contaRepository.findById(transacao.getId_conta_creditada()).isPresent())) {
    		contaDebitada = contaRepository.findById(transacao.getId_conta_debitada());	
    		if ((contaDebitada.get().getTipo_conta() == TipoConta.filial) && (contaCreditada.get().getTipo_conta() != TipoConta.matriz)) {
        		contaCreditada.get().setSaldo(contaCreditada.get().getSaldo() + transacao.getValor());
        		contaDebitada.get().setSaldo(contaDebitada.get().getSaldo() - transacao.getValor());
        		contaRepository.save(contaCreditada);
        		contaRepository.save(contaDebitada);
    		}else {
    			return new ResponseEntity<Object>("A transferencia não pode ser realizada, por favor verifique as condições", HttpStatus.INTERNAL_SERVER_ERROR);
    		}
    	}
    	
    	Transacao savedTransacao = transacaoRepository.save(transacao);

    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
    			.buildAndExpand(savedTransacao.getId()).toUri();

    	return ResponseEntity.created(location).build();

    }

    @PostMapping("/transacao")
    public ResponseEntity<Object> chargeBackTransacao(@PathVariable UUID uuid) {
		log.info("ChargeBack Transacao by uuid");
		log.info("--------------------------");
    	Optional<Conta> contaCreditada = null;
    	Optional<Conta> contaDebitada = null;

		Optional<Transacao> transacao = transacaoRepository.findByCodigotransacao(uuid);

		if (!transacao.isPresent()) {
			return new ResponseEntity<Object>("Transacao não encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Transacao : " + transacao.toString());
		log.info("--------------------------");
    	
		contaCreditada = contaRepository.findById(transacao.get().getId_conta_creditada());
		contaDebitada = contaRepository.findById(transacao.get().getId_conta_debitada());	
		Transacao chargeBackTransacao = new Transacao();

    	if (contaCreditada.get().getTipo_conta() == TipoConta.matriz) {
    		if (transacao.get().getTipo_transacao() == TipoTransacao.aporte) {
        		contaCreditada.get().setSaldo(contaCreditada.get().getSaldo() - transacao.get().getValor());
        		contaRepository.save(contaCreditada);

        		chargeBackTransacao.setCodigo_transacao(java.util.UUID.randomUUID());
        		LocalDateTime dt_transacao = LocalDateTime.now();
        		chargeBackTransacao.setDt_transacao(dt_transacao);
        		chargeBackTransacao.setId_conta_debitada(contaCreditada.get().getId());
        		chargeBackTransacao.setTipo_transacao(TipoTransacao.aporte);
        		chargeBackTransacao.setValor(transacao.get().getValor());

    		}
    		
    	} else {
    		// charge back entre contas
    		contaCreditada.get().setSaldo(contaCreditada.get().getSaldo() - transacao.get().getValor());
    		contaDebitada.get().setSaldo(contaDebitada.get().getSaldo() + transacao.get().getValor());
    		contaRepository.save(contaCreditada);
    		contaRepository.save(contaDebitada);
        	
    		chargeBackTransacao.setCodigo_transacao(java.util.UUID.randomUUID());
    		LocalDateTime dt_transacao = LocalDateTime.now();
    		chargeBackTransacao.setDt_transacao(dt_transacao);
    		chargeBackTransacao.setId_conta_creditada(contaDebitada.get().getId());
    		chargeBackTransacao.setId_conta_debitada(contaCreditada.get().getId());
    		chargeBackTransacao.setTipo_transacao(TipoTransacao.transferencia);
    		chargeBackTransacao.setValor(transacao.get().getValor());
    		
    	}
		
    	Transacao savedTransacao = transacaoRepository.save(chargeBackTransacao);

    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
    			.buildAndExpand(savedTransacao.getId()).toUri();

    	return ResponseEntity.created(location).build();

    }

    @ApiOperation(value = "Buscar uma transacao", response = Conta.class, notes = "Essa operação busca uma transacao no banco de dados.")
	@GetMapping("/{uuid}")
	public ResponseEntity<Object> retrieveTransacao(@PathVariable UUID uuid) {
		log.info("Retrieve Transacao by uuid");
		log.info("--------------------------");
		Optional<Transacao> transacao = transacaoRepository.findByCodigotransacao(uuid);

		if (!transacao.isPresent()) {
			return new ResponseEntity<Object>("Transacao não encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Transacao : " + transacao.toString());
		log.info("--------------------------");

		return new ResponseEntity<Object>(transacao, HttpStatus.OK);
	}

    
}
