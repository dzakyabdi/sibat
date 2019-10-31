package apap.tp.sibat.controller;

import apap.tp.sibat.model.*;
import apap.tp.sibat.repository.GudangDB;
import apap.tp.sibat.repository.JenisDB;
import apap.tp.sibat.repository.ObatDB;
import apap.tp.sibat.repository.SupplierDB;
import apap.tp.sibat.service.GudangService;
import apap.tp.sibat.service.ObatService;
import apap.tp.sibat.service.ObatSupplierService;
import apap.tp.sibat.service.SupplierService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class ObatController {
    @Autowired
    private ObatService obatService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ObatSupplierService obatSupplierService;

    @Autowired
    private SupplierDB supplierDb;

    @Autowired
    private JenisDB jenisDb;

    @Autowired
    private GudangDB gudangDb;

    @Autowired
    private GudangService gudangService;

    @RequestMapping("/")
    public String home(Model model) {
        List<ObatModel> obatList = obatService.getListObat();
        model.addAttribute("obatList", obatList);
        return "home";
    }

    @RequestMapping(value = "/obat/tambah", method = RequestMethod.GET)
    public String tambahObatFormPage(Model model) {
        List<SupplierModel> listSupplier = supplierDb.findAll();
        ObatModel obat = new ObatModel();
        List<ObatSupplierModel> obatSupplierList = new ArrayList<>();
        ObatSupplierModel obatsupplier = new ObatSupplierModel();
        obatsupplier.setObat(obat);
        obatSupplierList.add(obatsupplier);
        obat.setObatSupplierList(obatSupplierList);

        List<JenisModel> listJenis = jenisDb.findAll();
        model.addAttribute("listJenis", listJenis);
        model.addAttribute("listSupplier", listSupplier);
        model.addAttribute("obat", obat);
        return "form-tambah-obat";
    }

    @RequestMapping(value = "/obat/tambah", method = RequestMethod.POST, params = {"submit"})
    public String tambahObatSubmit(@ModelAttribute ObatModel obat, Model model) {
        String kode = generateKode(obat);
        obat.setKode(kode);
        List<ObatSupplierModel> listObatSupplier = obat.getObatSupplierList();
        obat.setObatSupplierList(null);
        obatService.addObat(obat);
        for (ObatSupplierModel i : listObatSupplier) {
            ObatSupplierModel obatSupplierModel = new ObatSupplierModel();
            obatSupplierModel.setObat(obat);
            obatSupplierModel.setSupplier(supplierService.getSupplierByIdSupplier(i.getSupplier().getId()));
            obatSupplierService.addObatSupplier(obatSupplierModel);
        }

        model.addAttribute("obat", obat);
        return "tambah-obat";
    }

    @RequestMapping(value = "/obat/tambah", method = RequestMethod.POST, params={"tambah"})
    public String tambahRowObatSupplierFormPage(@ModelAttribute ObatModel obat,
                                                BindingResult bindingResult, Model model) {
        if(obat.getObatSupplierList() == null) {
            obat.setObatSupplierList(new ArrayList<ObatSupplierModel>());
        }
        obat.getObatSupplierList().add(new ObatSupplierModel());
        List<SupplierModel> listSupplier = supplierService.getSupplierList();
        List<JenisModel> listJenis = jenisDb.findAll();
        model.addAttribute("listJenis", listJenis);
        model.addAttribute("obat", obat);
        model.addAttribute("listSupplier", listSupplier);

        return "form-tambah-obat";
    }

    @RequestMapping(value = "/obat/view", method = RequestMethod.GET)
    public String viewObat(@RequestParam("noReg") String nomorRegistrasi, Model model) {
        ObatModel obat = obatService.getObatByNomorRegistrasi(nomorRegistrasi);

        List<ObatSupplierModel> listObatSupplier = obat.getObatSupplierList();
        String daftarSupplier = "";
        for (ObatSupplierModel obj : listObatSupplier) {
            daftarSupplier += obj.getSupplier().getNama() + ", ";
        }

        List<GudangObatModel> listGudangObat = obat.getGudangObatList();
        String daftarGudang = "";
        for (GudangObatModel obj : listGudangObat) {
            daftarGudang += obj.getGudang().getNama() + ", ";
        }

        String jenis = obat.getJenis().getNama();

        model.addAttribute("jenis", jenis);
        model.addAttribute("daftarSupplier", daftarSupplier);
        model.addAttribute("daftarGudang", daftarGudang);
        model.addAttribute("obat", obat);
        return "view-obat";
    }

    @RequestMapping(value = "/obat/ubah", method = RequestMethod.GET)
    public String ubahObatFormPage(@RequestParam("id") Long id, Model model) {
        ObatModel obat = obatService.getObatById(id);
        if(obat.getObatSupplierList() == null) {
            obat.setObatSupplierList(new ArrayList<ObatSupplierModel>());
        }
        obat.getObatSupplierList().add(new ObatSupplierModel());
        List<SupplierModel> listSupplier = supplierService.getSupplierList();
        List<JenisModel> listJenis = jenisDb.findAll();
        model.addAttribute("listJenis", listJenis);
        model.addAttribute("obat", obat);
        model.addAttribute("listSupplier", listSupplier);
        return "form-ubah-obat";
    }

    @RequestMapping(value = "/obat/ubah", method = RequestMethod.POST)
    public String ubatObatSubmit(@ModelAttribute ObatModel obat, Model model) {
        ObatModel newObatData = obatService.changeObat(obat);
        model.addAttribute("obat", newObatData);
        return "ubah-obat";
    }

    @RequestMapping(value = "/obat/filter", method = RequestMethod.GET)
    public String filterSubmit(@RequestParam(required = false) Long idGudang,
                         @RequestParam(required = false) Long idSupplier,
                         @RequestParam(required = false) Long idJenis, Model model) {
        List<GudangModel> listGudang = gudangService.getListGudang();
        List<SupplierModel> listSupplier = supplierService.getSupplierList();
        List<JenisModel> listJenis = jenisDb.findAll();
        List<ObatModel> listObat = new ArrayList<>();

        if (idGudang == null && idSupplier == null && idJenis == null) {
            model.addAttribute("listGudang", listGudang);
            model.addAttribute("listSupplier", listSupplier);
            model.addAttribute("listJenis", listJenis);
            model.addAttribute("listObat", listObat);
            return "filter-obat";
        }else if(idGudang != null && idSupplier == null && idJenis == null) {
            for(GudangObatModel obj : gudangService.getGudangById(idGudang).getGudangObatList()) {
                listObat.add(obj.getObat());
            }
        } else if(idGudang == null && idSupplier != null && idJenis == null) {
            for(ObatSupplierModel obj : supplierService.getSupplierByIdSupplier(idSupplier).getObatSupplierList()) {
                listObat.add(obj.getObat());
            }
        } else if(idGudang == null && idSupplier == null && idJenis != null) {
            for(ObatModel obj : obatService.getListObat()) {
                if(obj.getJenis().getId().equals(idJenis)) {
                    listObat.add(obj);
                }
            }
        } else if(idGudang != null && idSupplier != null && idJenis == null) {
            for(GudangObatModel obj : gudangService.getGudangById(idGudang).getGudangObatList()) {
                for(ObatSupplierModel obatSupplier : obj.getObat().getObatSupplierList()) {
                    if(obatSupplier.getSupplier().getId().equals(idSupplier)) {
                        listObat.add(obj.getObat());
                    }
                }
            }
        } else if(idGudang != null && idSupplier == null && idJenis != null) {
            for(GudangObatModel obj : gudangService.getGudangById(idGudang).getGudangObatList()) {
                if(obj.getObat().getJenis().getId().equals(idJenis)) {
                    listObat.add(obj.getObat());
                }
            }
        } else if(idGudang == null && idSupplier != null && idJenis != null) {
            for(ObatSupplierModel obj : supplierService.getSupplierByIdSupplier(idSupplier).getObatSupplierList()) {
                if (obj.getObat().getJenis().getId().equals(idJenis)) {
                    listObat.add(obj.getObat());
                }
            }
        } else {
            for(GudangObatModel obj : gudangService.getGudangById(idGudang).getGudangObatList()) {
                for(ObatSupplierModel obatSupplier : obj.getObat().getObatSupplierList()) {
                    if(obatSupplier.getSupplier().getId().equals(idSupplier) && obj.getObat().getJenis().getId().equals(idJenis)) {
                        listObat.add(obj.getObat());
                    }
                }
            }
        }

        model.addAttribute("listGudang", listGudang);
        model.addAttribute("listSupplier", listSupplier);
        model.addAttribute("listJenis", listJenis);
        model.addAttribute("listObat", listObat);
        return "filter-obat";
    }

    @RequestMapping(value = "/obat/bonus")
    public String bonus(Model model) {
        List<ObatModel> listObat = obatService.getListObat();


        model.addAttribute("listObat", listObat);
        return "bonus";
    }

    String generateKode(ObatModel obat) {
        String years = String.valueOf(obat.getTanggalTerbit()).substring(0,4);
        String resultYears = years + String.valueOf(Integer.valueOf(years) + 5);
        if(obat.getBentuk().equals("Cairan")) {
            resultYears = "01" + resultYears;
        } else if (obat.getBentuk().equals("Kapsul")) {
            resultYears = "02" + resultYears;
        } else {
            resultYears = "03" + resultYears;
        }

        return Long.toString(obat.getJenis().getId()) + resultYears + getAlphaNumericString(2);
    }

    String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
