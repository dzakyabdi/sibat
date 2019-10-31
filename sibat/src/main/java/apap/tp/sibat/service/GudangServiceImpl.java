package apap.tp.sibat.service;

import apap.tp.sibat.model.GudangModel;
import apap.tp.sibat.model.GudangObatModel;
import apap.tp.sibat.model.ObatModel;
import apap.tp.sibat.repository.GudangDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GudangServiceImpl implements GudangService {
    @Autowired
    private GudangDB gudangDb;

    @Override
    public List<GudangModel> getListGudang(){
        return gudangDb.findAll();
    }

    @Override
    public GudangModel getGudangById(Long id) {
        return gudangDb.getGudangById(id).get();
    }

    @Override
    public void addGudang(GudangModel gudang) {
        gudangDb.save(gudang);
    }

    @Override
    public void deleteGudang(Long idGudang) {
        GudangModel gudang = getGudangById(idGudang);
        List<ObatModel> listObat = new ArrayList<>();
        for(GudangObatModel i : gudang.getGudangObatList()) {
            listObat.add(i.getObat());
        }

        if(listObat.size() == 0) {
            gudangDb.delete(gudang);
        } else {
            UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException();
            throw unsupportedOperationException;
        }
    }
}
