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

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.model.Dispensa;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class DispensaController {
	
	@Autowired
	private IAlunoRepository aRep;
	
	@Autowired
	
	
	@RequestMapping(name="dispensa", value="/dispensa", method = RequestMethod.GET)
	public ModelAndView dispensaGet(@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
		HttpSession session = request.getSession();
		session.invalidate();
		return new ModelAndView("dispensa");
	}
	
	@RequestMapping(name="dispensa", value="/dispensa", method = RequestMethod.POST)
	public ModelAndView dispensaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String ra = allRequestParam.get("ra");
		String disciplina = allRequestParam.get("disciplina");
		String motivo = allRequestParam.get("motivo");
		
		String saida = "";
		String erro = "";
		boolean found = false;
		Aluno a = new Aluno();
		Disciplina d = new Disciplina();
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Dispensa> dispensas = new ArrayList<>();
		
		try {
			a.setRa(ra);
			if(cmd.contains("Consultar Aluno")) {
				a = buscarAluno(ra);
				disciplinas = listarDisciplinas(ra); //fazer a logica de achar as disciplinas que um aluno esta cursando
				dispensas = listarDispensas(ra); //achar suas dispensas
				found = true;
			}
			if(cmd.contains("Solicitar Dispensa")) {
				d.setCodigo(Integer.parseInt(disciplina));
				a = buscarAluno(ra);
				d = buscarDisciplina(d); //fazer a logica pra buscar disciplina, n sei '-'
				saida = solicitarDispensa(a, d, motivo); //fazer a logica pra solicitar a dispensa
			}
		} catch(Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("disciplina", d);
			model.addAttribute("aluno", a);
			model.addAttribute("found", found);
			model.addAttribute("dispensas", dispensas);
		}
		return new ModelAndView("dispensa");
	}
	
	
	private Aluno buscarAluno(String ra) throws Exception {
		if(aRep.findById(ra).isEmpty()) {
			throw new Exception("Aluno não encontrado");
		}else {
			return aRep.findById(ra).get();
		}
	}
}
