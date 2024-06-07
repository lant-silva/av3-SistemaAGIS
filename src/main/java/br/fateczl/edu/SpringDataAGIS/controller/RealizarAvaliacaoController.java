package br.fateczl.edu.SpringDataAGIS.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.fateczl.edu.SpringDataAGIS.model.NotaAvaliacao;
import br.fateczl.edu.SpringDataAGIS.repository.INotaAvaliacaoRepository;

@Controller
public class RealizarAvaliacaoController {
	
	@Autowired
	private INotaAvaliacaoRepository naRep;
    
    @GetMapping("/realizaravaliacao")
	@RequestMapping(name="realizaravaliacao", value="/realizaravaliacao", method = RequestMethod.GET)
    public ModelAndView realizaravaliacaoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
    	int avaliacao = Integer.parseInt(allRequestParam.get("avaliacao_codigo"));
    	int disciplina = Integer.parseInt(allRequestParam.get("disciplina_codigo"));
    	String saida = "";
    	String erro = "";
    	List<NotaAvaliacao> alunos = new ArrayList<>();
    	try {
    		alunos = listarAlunos(disciplina, avaliacao);
    	} catch(Exception e) {
    		erro = e.getMessage();
    	} finally {
    		
    	}
    	
    	return new ModelAndView("realizaravaliacao");
    }
    
	private List<NotaAvaliacao> listarAlunos(int disciplina, int avaliacao) {
		return naRep.findAlunosPorAvaliacao(disciplina, avaliacao);
	}

	@RequestMapping(name="realizaravaliacao", value="/realizaravaliacao", method = RequestMethod.POST)
	public ModelAndView realizaravaliacaoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		return new ModelAndView("realizaravaliacao");
	}	
}
