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

import br.fateczl.edu.SpringDataAGIS.model.Avaliacao;
import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.repository.IDisciplinaRepository;

@Controller
public class AvaliacaoController {
	
	@Autowired 
	private IDisciplinaRepository dRep;
	
	@RequestMapping(name="avaliacao", value="/avaliacao", method = RequestMethod.GET)
	public ModelAndView avaliacaoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String erro = "";
		List<Disciplina> disciplinas = new ArrayList<>();
		
		try {
			disciplinas = dRep.findAll();
		} catch(Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("erro", erro);
		}
		return new ModelAndView("avaliacao");
	}
	
	@RequestMapping(name="avaliacao", value="/avaliacao", method = RequestMethod.POST)
	public ModelAndView avaliacaoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String disciplina = allRequestParam.get("disciplina");
		String codigoAvaliacao = allRequestParam.get("codigo");
		String nome = allRequestParam.get("nome");
		String peso = allRequestParam.get("peso");
		
		
		String saida = "";
		String erro = "";
		Disciplina d = new Disciplina();
		Avaliacao a = new Avaliacao();
		List<Disciplina> disciplinas = new ArrayList<>();
		
		try {
			disciplinas = dRep.findAll();
		} catch(Exception e){
			erro = e.getMessage();
		} finally {
			
		}
		return new ModelAndView("avaliacao");
	}
	
}
