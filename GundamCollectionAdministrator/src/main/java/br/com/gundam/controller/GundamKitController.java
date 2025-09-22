package br.com.gundam.controller;

import br.com.gundam.model.GundamKit;
import br.com.gundam.service.FileStorageService;
import br.com.gundam.service.GundamKitService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/kits")
public class GundamKitController {

    private final GundamKitService svc;
    private final FileStorageService storage;

    public GundamKitController(GundamKitService svc, FileStorageService storage) {
        this.svc = svc;
        this.storage = storage;
    }

    /* ===================== LISTAGEM COM FILTROS ===================== */
    @GetMapping
    public String list(
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "universoId", required = false) Long universoId,
            @RequestParam(value = "de", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam(value = "ate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<GundamKit> p = svc.search(modelo, gradeId, universoId, de, ate, pageable);

        model.addAttribute("page", p);
        model.addAttribute("fModelo", modelo);
        model.addAttribute("fGradeId", gradeId);
        model.addAttribute("fUniversoId", universoId);
        model.addAttribute("fDe", de);
        model.addAttribute("fAte", ate);

        // catálogos para filtros
        model.addAttribute("grades", svc.findAllGrades());
        model.addAttribute("escalas", svc.findAllEscalas());
        model.addAttribute("universos", svc.findAllUniversos());

        model.addAttribute("pageName", "kits");
        return "kits/list";
    }

    /* ===================== FORM NOVO ===================== */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("kit", new GundamKit());

        // catálogos para selects
        model.addAttribute("grades", svc.findAllGrades());
        model.addAttribute("escalas", svc.findAllEscalas());
        model.addAttribute("alturas", svc.findAllAlturas());
        model.addAttribute("universos", svc.findAllUniversos());

        model.addAttribute("pageName", "kits");
        return "kits/form";
    }

    /* ===================== SALVAR (CREATE) ===================== */
    @PostMapping
    public String salvar(
            @Valid @ModelAttribute("kit") GundamKit kit,
            @RequestParam(value = "fotoCaixaFile", required = false) MultipartFile fotoCaixaFile,
            @RequestParam(value = "fotoMontagemFile", required = false) MultipartFile fotoMontagemFile,
            Model model
    ) throws IOException {

        // uploads opcionais (armazenamento padronizado)
        if (fotoCaixaFile != null && !fotoCaixaFile.isEmpty()) {
            String filename = storage.save(fotoCaixaFile, "caixa");
            kit.setFotoCaixaUrl("/uploads/" + filename);
        }
        if (fotoMontagemFile != null && !fotoMontagemFile.isEmpty()) {
            String filename = storage.save(fotoMontagemFile, "montagem");
            kit.setFotoMontagemUrl("/uploads/" + filename);
        }

        svc.save(kit);
        return "redirect:/kits";
    }

    /* ===================== DETALHES ===================== */
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("kit", svc.getById(id));
        model.addAttribute("pageName", "kits");
        return "kits/details";
    }

    /* ===================== EDITAR (CARREGAR FORM) ===================== */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("kit", svc.getById(id));
        model.addAttribute("grades", svc.findAllGrades());
        model.addAttribute("escalas", svc.findAllEscalas());
        model.addAttribute("alturas", svc.findAllAlturas());
        model.addAttribute("universos", svc.findAllUniversos());
        model.addAttribute("pageName", "kits");
        return "kits/form";
    }

    /* ===================== ATUALIZAR (UPDATE) ===================== */
    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("kit") GundamKit form,
            @RequestParam(value = "fotoCaixaFile", required = false) MultipartFile fotoCaixaFile,
            @RequestParam(value = "fotoMontagemFile", required = false) MultipartFile fotoMontagemFile
    ) throws IOException {

        var kit = svc.getById(id);

        // campos básicos
        kit.setModelo(form.getModelo());
        kit.setFabricante(form.getFabricante());
        kit.setPreco(form.getPreco());
        kit.setDataCompra(form.getDataCompra());
        kit.setCapaUrl(form.getCapaUrl());
        kit.setVideoMontagemUrl(form.getVideoMontagemUrl());
        kit.setHorasMontagem(form.getHorasMontagem());

        // relacionamentos
        kit.setGrade(form.getGrade());
        kit.setEscala(form.getEscala());
        kit.setAlturaPadrao(form.getAlturaPadrao());

        // uploads (se enviados)
        if (fotoCaixaFile != null && !fotoCaixaFile.isEmpty()) {
            String filename = storage.save(fotoCaixaFile, "caixa");
            kit.setFotoCaixaUrl("/uploads/" + filename);
        }
        if (fotoMontagemFile != null && !fotoMontagemFile.isEmpty()) {
            String filename = storage.save(fotoMontagemFile, "montagem");
            kit.setFotoMontagemUrl("/uploads/" + filename);
        }

        svc.save(kit);
        return "redirect:/kits";
    }

    /* ===================== DELETAR ===================== */
    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id) {
        svc.deleteById(id);
        return "redirect:/kits";
    }

    // Armazenamento passou a ser responsabilidade de FileStorageService
}
