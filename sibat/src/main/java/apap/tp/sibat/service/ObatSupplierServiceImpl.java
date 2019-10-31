package apap.tp.sibat.service;

import apap.tp.sibat.model.ObatSupplierModel;
import apap.tp.sibat.repository.ObatSupplierDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObatSupplierServiceImpl implements ObatSupplierService {
    @Autowired
    ObatSupplierDB obatSupplierDb;

    @Override
    public void addObatSupplier(ObatSupplierModel obatSupplier) {
        obatSupplierDb.save(obatSupplier);
    }
}
