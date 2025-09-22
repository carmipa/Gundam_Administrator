package br.com.gundam.spec;

import br.com.gundam.model.GundamKit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class GundamKitSpecifications {

    private GundamKitSpecifications() {}

    public static Specification<GundamKit> modeloLike(String modelo) {
        return (root, query, cb) -> {
            if (modelo == null || modelo.isBlank()) return cb.conjunction();
            String like = "%" + modelo.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("modelo")), like);
        };
    }

    public static Specification<GundamKit> gradeIdEquals(Long gradeId) {
        return (root, query, cb) -> {
            if (gradeId == null) return cb.conjunction();
            return cb.equal(root.get("grade").get("id"), gradeId);
        };
    }

    public static Specification<GundamKit> dataCompraBetween(LocalDate de, LocalDate ate) {
        return (root, query, cb) -> {
            if (de == null && ate == null) return cb.conjunction();
            if (de != null && ate != null) {
                return cb.between(root.get("dataCompra"), de, ate);
            }
            if (de != null) {
                return cb.greaterThanOrEqualTo(root.get("dataCompra"), de);
            }
            return cb.lessThanOrEqualTo(root.get("dataCompra"), ate);
        };
    }

    public static Specification<GundamKit> universoIdEquals(Long universoId) {
        return (root, query, cb) -> {
            if (universoId == null) return cb.conjunction();
            return cb.equal(root.get("universo").get("id"), universoId);
        };
    }
}
