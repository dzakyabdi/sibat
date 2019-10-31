package apap.tp.sibat.service;

import apap.tp.sibat.model.SupplierModel;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    SupplierModel getSupplierByIdSupplier(Long id);
    List<SupplierModel> getSupplierList();
}
