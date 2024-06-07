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
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;

@Controller
public class StatusFaltaController {
	
	@Autowired
	private IMatriculaDisciplinaRepository mdRep;

	@RequestMapping(name="statusfalta", value="/statusfalta", method = RequestMethod.GET)
	public ModelAndView statusNotaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		return new ModelAndView("statusfalta");
	}
	
	@RequestMapping(name="statusfalta", value="/statusfalta", method = RequestMethod.POST)
	public ModelAndView statusNotaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String disciplina = allRequestParam.get("disciplina");
		
		String saida = "";
		String erro = "";
		List<Disciplina> disciplinas = new ArrayList<>();
		List<MatriculaDisciplina> alunos = new ArrayList<>();
		
		try {
			if(cmd.contains("Listar")) {
				alunos = buscarAlunos(disciplina);
			}
			if(cmd.contains("Gerar")) {
				
			}
		} catch(Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("alunos", alunos);
			model.addAttribute("disciplinas", disciplinas);
		}
		
		return new ModelAndView("statusfalta");
	}

	private List<MatriculaDisciplina> buscarAlunos(String disciplina) {
		
		return null;
	}
	
}
