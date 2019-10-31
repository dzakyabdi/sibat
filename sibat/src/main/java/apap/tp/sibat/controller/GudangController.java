package apap.tp.sibat.controller;

import apap.tp.sibat.model.GudangModel;
import apap.tp.sibat.model.GudangObatModel;
import apap.tp.sibat.model.ObatModel;
import apap.tp.sibat.service.GudangObatService;
import apap.tp.sibat.service.GudangService;
import apap.tp.sibat.service.ObatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class GudangController {
    @Autowired
    private GudangService gudangService;

    @Autowired
    private ObatService obatService;

    @Autowired
    private GudangObatService gudangObatService;

    @RequestMapping(value = "/gudang", method = RequestMethod.GET)
    public String gudang(Model model) {
        List<GudangModel> listGudang = gudangService.getListGudang();
        model.addAttribute("listGudang", listGudang);
        return "daftar-gudang";
    }

    @RequestMapping(value = "/gudang/view", method = RequestMethod.GET)
    public String detailGudang(@RequestParam("idGudang") Long idGudang, Model model) {
        GudangModel gudang = gudangService.getGudangById(idGudang);

        List<GudangObatModel> listGudangObat = gudang.getGudangObatList();
        List<ObatModel> listObatExist = new ArrayList<>();
        for(GudangObatModel i : listGudangObat) {
            listObatExist.add(i.getObat());
        }

        List<ObatModel> listSemuaObat = obatService.getListObat();
        for(ObatModel i : listObatExist) {
            if(listSemuaObat.contains(i)) {
                listSemuaObat.remove(i);
            }
        }

        GudangObatModel dummyObject = new GudangObatModel();
        dummyObject.setGudang(gudang);

        int jumlah = listObatExist.size();
        model.addAttribute("listObatExist", listObatExist);
        model.addAttribute("listObatNoExist", listSemuaObat);
        model.addAttribute("dummy", dummyObject);
        model.addAttribute("jumlah", jumlah);
        model.addAttribute("gudang", gudang);

        return "view-gudang";
    }

    @RequestMapping(value = "/gudang/tambah", method = RequestMethod.GET)
    public String tambahGudangFormPage(Model model) {
        GudangModel gudang = new GudangModel();
        model.addAttribute("gudang", gudang);
        return "form-tambah-gudang";
    }

    @RequestMapping(value = "/gudang/tambah", method = RequestMethod.POST)
    public String tambahGudangSubmit(@ModelAttribute GudangModel gudang, Model model) {
        gudangService.addGudang(gudang);

        model.addAttribute("gudang", gudang);
        return "tambah-gudang";
    }

    @RequestMapping(value = "/gudang/hapus/{idGudang}")
    public String deleteGudang(
            @PathVariable("idGudang") Long idGudang, Model model
    ) {

        try{
            String nama = gudangService.getGudangById(idGudang).getNama();
            gudangService.deleteGudang(idGudang);
            model.addAttribute("message", "Gudang dengan nama " + nama + " berhasil dihapus!");
            return "delete-gudang";
        }catch (NoSuchElementException e) {
            model.addAttribute("errorMessage", "Id " +  idGudang.toString() + " tidak ditemukan");
            return "delete-gudang-error";
        }catch (UnsupportedOperationException e) {
            model.addAttribute("errorMessage", "Tidak berhasil dihapus karena Gudang memiliki Obat!");
            return "delete-gudang-error";
        }
    }

    @RequestMapping(value = "/gudang/tambah-obat", method = RequestMethod.POST)
    public String tambahObat(@ModelAttribute GudangObatModel dummy, Model model) {
        System.out.println(dummy.getObat() + " ini objek obat");
        System.out.println(dummy.getGudang() + " ini objek gudang");
        gudangObatService.addGudangObat(dummy);

        GudangModel gudang = dummy.getGudang();

        List<GudangObatModel> listGudangObat = gudang.getGudangObatList();
        List<ObatModel> listObatExist = new ArrayList<>();
        for(GudangObatModel i : listGudangObat) {
            listObatExist.add(i.getObat());
        }

        List<ObatModel> listSemuaObat = obatService.getListObat();
        for(ObatModel i : listObatExist) {
            if(listSemuaObat.contains(i)) {
                listSemuaObat.remove(i);
            }
        }

        GudangObatModel dummyObject = new GudangObatModel();
        dummyObject.setGudang(gudang);

        int jumlah = listObatExist.size();
        model.addAttribute("listObatExist", listObatExist);
        model.addAttribute("listObatNoExist", listSemuaObat);
        model.addAttribute("dummy", dummyObject);
        model.addAttribute("jumlah", jumlah);
        model.addAttribute("gudang", gudang);
        return "view-gudang";
    }

    @RequestMapping(value = "/gudang/expired-obat", method = RequestMethod.GET)
    public String kadaluarsa(@RequestParam(required = false) Long idGudang, Model model) {
        List<GudangModel> listGudang = gudangService.getListGudang();
        List<ObatModel> listObat = new ArrayList<>();

        if(idGudang == null) {
            model.addAttribute("listObat", listObat);
            model.addAttribute("listGudang", listGudang);
            return "kadaluarsa";
        }

        List<GudangObatModel> listGudangObat = gudangService.getGudangById(idGudang).getGudangObatList();
        Date today = new Date();

        for(GudangObatModel obj : listGudangObat) {
            long diffInMillies = Math.abs(today.getTime() - obj.getObat().getTanggalTerbit().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            System.out.println(obj.getObat().getNama());
            System.out.println(diff);

            if(diff > 1825) {
                listObat.add(obj.getObat());
            }
        }

        model.addAttribute("listObat", listObat);
        model.addAttribute("listGudang", listGudang);
        return "kadaluarsa";
    }

}
