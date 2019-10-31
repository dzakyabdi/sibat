package apap.tp.sibat.service;

import apap.tp.sibat.model.ObatModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ObatService {
    List<ObatModel> getListObat();
    void addObat(ObatModel obat);
    ObatModel getObatByNomorRegistrasi(String noReg);
    ObatModel getObatById(Long id);
    ObatModel changeObat(ObatModel obat);
}
