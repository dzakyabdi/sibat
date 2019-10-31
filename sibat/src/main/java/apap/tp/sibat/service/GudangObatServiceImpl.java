package apap.tp.sibat.service;

import apap.tp.sibat.model.GudangObatModel;
import apap.tp.sibat.repository.GudangObatDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GudangObatServiceImpl implements GudangObatService{
    @Autowired
    private GudangObatDB gudangObatDb;

    @Override
    public void addGudangObat(GudangObatModel gudangObat) {
        gudangObatDb.save(gudangObat);
    }
}
