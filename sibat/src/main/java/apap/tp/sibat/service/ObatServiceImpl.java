package apap.tp.sibat.service;

import apap.tp.sibat.model.ObatModel;
import apap.tp.sibat.repository.ObatDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObatServiceImpl implements ObatService {
    @Autowired
    private ObatDB obatDb;

    @Override
    public List<ObatModel> getListObat() {
        return obatDb.findAll();
    }

    @Override
    public void addObat(ObatModel obat) {
        obatDb.save(obat);
    }

    @Override
    public ObatModel getObatByNomorRegistrasi(String noReg) {
        return obatDb.findByNomorRegistrasi(noReg).get();
    }

    @Override
    public ObatModel getObatById(Long id) {
        return obatDb.findById(id).get();
    }

    @Override
    public ObatModel changeObat(ObatModel obat) {
        ObatModel targetObat = obatDb.findById(obat.getId()).get();

        try {
            targetObat.setNama(obat.getNama());
            targetObat.setTanggalTerbit(targetObat.getTanggalTerbit());
            targetObat.setHarga(targetObat.getHarga());
            targetObat.setBentuk(targetObat.getBentuk());
            obatDb.save(targetObat);
            return targetObat;
        } catch (NullPointerException nullException) {
            return null;
        }
    }
}
