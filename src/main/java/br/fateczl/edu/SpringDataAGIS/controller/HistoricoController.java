package br.fateczl.edu.SpringDataAGIS.controller;

import java.sql.SQLException;
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

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;

@Controller
public class HistoricoController {

	@Autowired
	private IAlunoRepository aRep;
	
	@Autowired
	private IMatriculaDisciplinaRepository mdRep;
	
	@RequestMapping(name = "historico", value="/historico", method = RequestMethod.GET)
	public ModelAndView historicoGet (@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		return new ModelAndView("historico");
	}
	
	@RequestMapping(name = "historico", value="/historico", method = RequestMethod.POST)
	public ModelAndView historicoPost (@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String ra = allRequestParam.get("ra");
		
		Aluno a = new Aluno();
		List<MatriculaDisciplina> md = new ArrayList<>();
		String saida = "";
		String erro = "";
		
		try {
			if(cmd.contains("Consultar")) {
				a = aRep.findById(ra).get();
				md = mdRep.fn_listarultimamatricula(ra);
			}
		} catch (Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("disciplinas", md);
		}
		return new ModelAndView("historico");
	}
}
