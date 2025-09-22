package br.com.gundam.repository;

import br.com.gundam.model.GundamKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface GundamKitRepository
        extends JpaRepository<GundamKit, Long>, JpaSpecificationExecutor<GundamKit> {

    // NOVOS MÉTODOS PARA O RELATÓRIO
    @Query("SELECT SUM(k.preco) FROM GundamKit k")
    BigDecimal findCustoTotal();

    @Query("SELECT AVG(k.preco) FROM GundamKit k")
    Double findCustoMedio();

    @Query("FROM GundamKit WHERE preco = (SELECT MAX(preco) FROM GundamKit)")
    List<GundamKit> findKitsMaisCaros();

    @Query("FROM GundamKit WHERE preco = (SELECT MIN(preco) FROM GundamKit)")
    List<GundamKit> findKitsMaisBaratos();
}