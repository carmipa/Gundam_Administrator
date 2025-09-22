package br.com.gundam.repo;

import br.com.gundam.model.AlturaPadrao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlturaRepo extends JpaRepository<AlturaPadrao, Long> {}
