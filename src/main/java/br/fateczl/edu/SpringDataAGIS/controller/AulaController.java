package br.fateczl.edu.SpringDataAGIS.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.repository.IAulaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IDisciplinaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AulaController {
	
	@Autowired
	private IAulaRepository auRep;
	
	@Autowired
	private IMatriculaDisciplinaRepository mdRep;
	
	@Autowired
	private IDisciplinaRepository dRep;
	
	@RequestMapping(name = "aula", value="/aula", method = RequestMethod.GET)
	public ModelAndView aulaGet (@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
		HttpSession session = request.getSession();
		String erro = "";	
		List<Disciplina> disciplinas = new ArrayList<>();
		
		try {
			disciplinas = listarDisciplinas();
		} catch(Exception e){
			erro = e.getMessage();
		} finally {
			session.setAttribute("disciplinas", disciplinas);
			model.addAttribute("erro", erro);
		}
		return new ModelAndView("aula");
	}

	private List<Disciplina> listarDisciplinas() {
		return dRep.findAll();
	}
	
}
