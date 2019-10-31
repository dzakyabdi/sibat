package apap.tp.sibat.repository;

import apap.tp.sibat.model.JenisModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JenisDB extends JpaRepository<JenisModel, Long> {
}
