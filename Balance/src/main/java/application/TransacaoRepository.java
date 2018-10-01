package application;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface TransacaoRepository extends CrudRepository<Transacao, Long> {
	
	Optional<Transacao> findByCodigotransacao(UUID uuid);
}
