package apap.tp.sibat.repository;

import apap.tp.sibat.model.GudangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GudangDB extends JpaRepository<GudangModel, Long> {
    Optional<GudangModel> getGudangById(Long id);
}
