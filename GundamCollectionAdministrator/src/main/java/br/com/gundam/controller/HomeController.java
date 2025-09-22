package br.com.gundam.web;

import br.com.gundam.service.GundamKitService; // Importar o service
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importar Model
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // INJEÇÃO DE DEPENDÊNCIA DO SERVICE
    private final GundamKitService kitService;

    public HomeController(GundamKitService kitService) {
        this.kitService = kitService;
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/sobre")
    public String sobre() {
        return "sobre";
    }

    // MÉTODO ATUALIZADO
    @GetMapping("/relatorios")
    public String relatorios(Model model) { // Adicionar Model
        // Buscar os dados do relatório e adicionar ao model
        model.addAttribute("report", kitService.getFinancialReport());
        return "relatorios";
    }
}