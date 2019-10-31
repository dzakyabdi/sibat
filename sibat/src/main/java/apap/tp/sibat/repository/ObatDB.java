package apap.tp.sibat.repository;

import apap.tp.sibat.model.ObatModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObatDB extends JpaRepository<ObatModel, Long> {
    Optional<ObatModel> findByNomorRegistrasi(String noReg);
    Optional<ObatModel> findById(Long id);
}