package br.fateczl.edu.SpringDataAGIS.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.model.Dispensa;
import br.fateczl.edu.SpringDataAGIS.repository.IDispensaRepository;

@Controller
public class SecretariaDispensaController {
	
	@Autowired
	private IDispensaRepository dispRep;
	
    @RequestMapping(name="secretariadispensa", value="/secretariadispensa", method = RequestMethod.GET)
    public ModelAndView secretariaDispensaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
        String erro = "";
        List<Dispensa> dispensas = new ArrayList<>();
        try {
            dispensas = listarDispensas(); // Obtém a lista de dispensas
        } catch (Exception e) {
            erro = e.getMessage(); 
        } finally {
            model.addAttribute("erro", erro); // Adiciona a mensagem de erro ao modelo
            model.addAttribute("dispensas", dispensas); // Adiciona a lista de dispensas ao modelo
        }
        return new ModelAndView("secretariadispensa"); // Retorna a view "secretariadispensa"
    }

    @RequestMapping(name="secretariadispensa", value="/secretariadispensa", method = RequestMethod.POST)
    public ModelAndView secretariaDispensaPost(@RequestBody Map<String, String> requestBody, ModelMap model) {
        String ra = requestBody.get("ra"); 
        String disciplina = requestBody.get("disciplina"); 
        String aprovacao = requestBody.get("aprovacao"); 
        System.out.println(ra);
        System.out.println(disciplina);
        System.out.println(aprovacao);
        List<Dispensa> dispensas = new ArrayList<>();
        Aluno a = new Aluno(); 
        Disciplina d = new Disciplina(); 
        String saida = "";
        String erro = "";
        
        try {
            saida = concluirDispensa(ra, disciplina, aprovacao); 
            dispensas = listarDispensas(); 
        } catch (Exception e) {
            erro = e.getMessage(); 
        } finally {
            model.addAttribute("saida", saida); 
            model.addAttribute("erro", erro); 
            model.addAttribute("dispensas", dispensas); 
        }
        
        return new ModelAndView("secretariadispensa"); 
    }

    
	private String concluirDispensa(String ra, String disciplina, String aprovacao) {
		return null;
	}

	private List<Dispensa> listarDispensas() {
		return dispRep.findAll();
	}
    
}
