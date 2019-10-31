package apap.tp.sibat.service;

import apap.tp.sibat.model.SupplierModel;
import apap.tp.sibat.repository.SupplierDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService{
    @Autowired
    SupplierDB supplierDb;

    @Override
    public SupplierModel getSupplierByIdSupplier(Long id) {
        return supplierDb.findById(id).get();
    }

    @Override
    public List<SupplierModel> getSupplierList(){
        return supplierDb.findAll();
    }
}
