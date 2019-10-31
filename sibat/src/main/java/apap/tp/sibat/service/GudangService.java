package apap.tp.sibat.service;

import apap.tp.sibat.model.GudangModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface GudangService {
    List<GudangModel> getListGudang();
    GudangModel getGudangById(Long id);
    void addGudang(GudangModel gudang);
    void deleteGudang(Long idGudang);
}
