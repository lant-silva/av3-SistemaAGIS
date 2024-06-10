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
import br.fateczl.edu.SpringDataAGIS.repository.IAvaliacaoRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IDisciplinaRepository;

@Controller
public class AvaliacaoController {
	
	@Autowired 
	private IDisciplinaRepository dRep;
	
	@Autowired
	private IAvaliacaoRepository aRep;
	
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
		String codigoAvaliacao = allRequestParam.get("avaliacao_codigo");
		String nome = allRequestParam.get("nome");
		String peso = allRequestParam.get("peso");
		
		
		String saida = "";
		String erro = "";
		Disciplina d = new Disciplina();
		Avaliacao a = new Avaliacao();
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		try {
			disciplinas = dRep.findAll();
			if(!cmd.contains("Listar")) {
				d.setCodigo(Integer.parseInt(disciplina));
				a.setAvaliacao_codigo(Integer.parseInt(codigoAvaliacao));
			}
			if(cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				d = dRep.findById(d.getCodigo()).get();
				a.setAvaliacao_codigo(Integer.parseInt(codigoAvaliacao));
				a.setDisciplina(d);
				a.setNome(nome);
				a.setPeso(Float.parseFloat(peso));
			}
			if(cmd.contains("Cadastrar")) {
				saida = cadastrarAvaliacao(a);
				a = null;
			}
			if(cmd.contains("Alterar")) {
				saida = alterarAvaliacao(a);
				a = null;
			}
			if(cmd.contains("Excluir")) {
				saida = excluirAvaliacao(a);
				a = null;
			}
			if(cmd.contains("Buscar")) {
				a = buscarAvaliacao(a);
			}
			if(cmd.equals("Listar")) {
				avaliacoes = listarAvaliacoes();
			}
			if(cmd.equals("Listar Av. por Disciplina")) {
				d.setCodigo(Integer.parseInt(disciplina));
				avaliacoes = listarAvaliacoesDisciplina(d);
			}
		} catch(Exception e){
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplina", d);
			model.addAttribute("avaliacao", a);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("avaliacoes", avaliacoes);
		}
		
		return new ModelAndView("avaliacao");
	}

	private String cadastrarAvaliacao(Avaliacao a) throws Exception{
		if(validarPesos(a)) {			
			aRep.save(a);
		}else {
			throw new Exception("A soma dos pesos das avaliações devem resultar em 1");
		}
		return "Avaliacao cadastrada.";
	}

	private String alterarAvaliacao(Avaliacao a) throws Exception {
		if(validarPesos(a)) {
			aRep.save(a);
		}else {
			throw new Exception("A soma dos pesos das avaliações devem resultar em 1");
		}
		return "Avaliação atualizada.";
	}

	private String excluirAvaliacao(Avaliacao a) {
		aRep.delete(a);
		return "Avaliação excluida.";
	}

	private Avaliacao buscarAvaliacao(Avaliacao a) {
		return aRep.findById(a.getAvaliacao_codigo()).get();
	}

	private List<Avaliacao> listarAvaliacoes() throws Exception {
		if(aRep.findAll().isEmpty()) {
			throw new Exception("Não há avaliações cadastradas");
		}else {			
			return aRep.findAll();
		}
	}

	private List<Avaliacao> listarAvaliacoesDisciplina(Disciplina d) throws Exception {
		List<Avaliacao> av = aRep.findAll();
		List<Avaliacao> aux = new ArrayList<>();
		for(Avaliacao a : av) {
			if(a.getDisciplina().getCodigo() == d.getCodigo()) {
				aux.add(a);
			}
		}
		if(aux.isEmpty()) {
			throw new Exception("A disciplina selecionada não possui avaliações cadastradas");
		}else {
			return aux;
		}
	}

	private boolean validarPesos(Avaliacao a) {
		Disciplina d = a.getDisciplina();
		List<Avaliacao> avaliacoes = aRep.findAll();
		float soma = 0;
		for(Avaliacao av : avaliacoes) {
			if(av.getDisciplina().getCodigo() == d.getCodigo()) {
				soma += av.getPeso();
			}
		}
		soma += a.getPeso();
		if(soma <= 1.0) {
			return true;
		}else {
			return false;
		}
	}

}
