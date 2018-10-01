package application;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ContaRepository extends CrudRepository<Conta, Long> {

	Conta save(Optional<Conta> contaOptional);

}
