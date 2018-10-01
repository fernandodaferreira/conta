package application;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import application.Conta.TipoConta;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("conta")
public class ContaController {

	private static final Logger log = LoggerFactory.getLogger(ContaController.class);
	@Autowired
	private ContaRepository repository;

	@ApiOperation(value = "Buscar todas as contas", response = Conta.class, notes = "Essa operação busca todas as contas no banco de dados.")
	@GetMapping("/contas")
	public List<Conta> retrieveAllContas() {
		log.info("Retrieve All Contas");
		log.info("-------------------");
		return (List<Conta>) repository.findAll();
	}

	@ApiOperation(value = "Buscar uma conta", response = Conta.class, notes = "Essa operação busca uma conta no banco de dados.")
	@GetMapping("/{id}")
	public ResponseEntity<Object> retrieveConta(@PathVariable long id) {
		log.info("Retrieve Conta by id");
		log.info("--------------------");
		Optional<Conta> conta = repository.findById(id);

		if (!conta.isPresent()) {
			return new ResponseEntity<Object>("Conta não encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Conta : " + conta.toString());
		log.info("--------------------------");

		return new ResponseEntity<Object>(conta, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletar uma conta", response = Conta.class, notes = "Essa operação deleta uma conta no banco de dados.")
	@DeleteMapping("/{id}")
	public void deleteConta(@PathVariable long id) {
		log.info("Delete Conta by id : " + id);
		log.info("---------------------------");
		repository.deleteById(id);
	}

	@ApiOperation(value = "Cadastrar uma nova conta", response = Conta.class, notes = "Essa operação salva uma nova conta no banco de dados.")
	@PostMapping("/conta")
	public ResponseEntity<Object> createConta(@RequestBody Conta conta) {
		log.info("Create Conta : " + conta.toString());
		log.info("-----------------------------------");

		// validação de tipo de conta
		if ((conta.getTipo_conta() == TipoConta.matriz) && (conta.getId_conta_pai() > 0)) {
			log.info("------Conta matriz não pode ter id_conta_pai--------");
			return new ResponseEntity<Object>("Conta matriz não pode ter id_conta_pai",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else if ((conta.getTipo_conta() == TipoConta.filial) && (conta.getId_conta_pai() <= 0)) {
			log.info("----------Conta filial precisa ter um id_conta_pai-----");
			return new ResponseEntity<Object>("Conta filial precisa ter um id_conta_pai",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// validação de pelo menos um pessoa na conta
		if ((conta.getCpf() <= 0) && (conta.getCnpj() <= 0)) {
			log.info("------A Conta precisa ter pelo menos um CPF ou CNPJ--------");
			return new ResponseEntity<Object>("A Conta precisa ter pelo menos um CPF ou CNPJ",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} else if ((conta.getCpf() > 0) && (conta.getCnpj() > 0)) {
			log.info("------A Conta não pode ter CPF e CNPJ, apenas um deles--------");
			return new ResponseEntity<Object>(" Conta não pode ter CPF e CNPJ, apenas um deles",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Conta savedConta = repository.save(conta);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedConta.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

	@ApiOperation(value = "Alterar uma conta", response = Conta.class, notes = "Essa operação altera uma conta no banco de dados.")
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateConta(@RequestBody Conta conta, @PathVariable long id) {
		log.info("Update Conta - conta / id: " + conta.toString() + " / " + id);
		log.info("-----------------------------------------------------------");

		Optional<Conta> contaOptional = repository.findById(id);

		if (!contaOptional.isPresent())
			return ResponseEntity.notFound().build();

		conta.setId(id);

		repository.save(conta);

		return new ResponseEntity<Object>(conta, HttpStatus.OK);
	}

}
