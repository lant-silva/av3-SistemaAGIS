package br.fateczl.edu.SpringDataAGIS.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;
import br.fateczl.edu.SpringDataAGIS.model.NotaAvaliacao;
import br.fateczl.edu.SpringDataAGIS.repository.INotaAvaliacaoRepository;

@Controller
public class StatusNotaController {
	
	@Autowired
	private INotaAvaliacaoRepository aRep;
	
	@RequestMapping(name="statusnota", value="/statusnota", method = RequestMethod.GET)
	public ModelAndView statusNotaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		return new ModelAndView("statusnota");
	}
	
	@RequestMapping(name="statusnota", value="/statusnota", method = RequestMethod.POST)
	public ModelAndView statusNotaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String ra = allRequestParam.get("ra");
		
		String saida = "";
		String erro = "";
		NotaAvaliacao na = new NotaAvaliacao();
		List<NotaAvaliacao> notas = new ArrayList<>();
		List<NotaAvaliacao> disciplinas = new ArrayList<>();
		
		try {
			if(cmd.contains("Consultar")) {
				notas = consultarNotas(ra);
			}
		} catch (Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("nota", na);
			model.addAttribute("notas", notas);
			model.addAttribute("disciplinas", disciplinas);
		}
		return new ModelAndView("statusnota");
	}
	private List<NotaAvaliacao> consultarNotas(String ra) {
		return aRep.findNotasParciais(ra);
	}
}
