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
import jakarta.servlet.http.HttpServletRequest;



@Controller
public class RealizarAvaliacaoController {
	
	@Autowired
	private INotaAvaliacaoRepository naRep;
	
    @GetMapping("/realizaravaliacao")
	@RequestMapping(name="realizaravaliacao", value="/realizaravaliacao", method = RequestMethod.GET)
    public ModelAndView realizaravaliacaoGet(@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
    	int avaliacao = Integer.parseInt(allRequestParam.get("avaliacao_codigo"));
    	int disciplina = Integer.parseInt(allRequestParam.get("disciplina_codigo"));
    	String erro = "";
    	List<NotaAvaliacao> alunos = new ArrayList<>();
    	
    	try {
    		alunos = listarAlunos(disciplina, avaliacao);
    	} catch(Exception e) {
    		erro = e.getMessage();
    	} finally {
    		model.addAttribute("alunos", alunos);
    		model.addAttribute("erro", erro);
    	}
    	
    	return new ModelAndView("realizaravaliacao");
    }
    
	private List<NotaAvaliacao> listarAlunos(int disciplina, int avaliacao) {
		return naRep.findAlunosPorAvaliacao(disciplina, avaliacao);
	}

	@RequestMapping(name="realizaravaliacao", value="/realizaravaliacao", method = RequestMethod.POST)
	public ModelAndView realizaravaliacaoPost(@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
		String cmd = allRequestParam.get("botao");
    	String saida = "";
    	String erro = "";
    	Map<String, String[]> parametros = request.getParameterMap();
    	int avaliacao = Integer.parseInt(allRequestParam.get("avaliacao_codigo"));
    	int disciplina = Integer.parseInt(allRequestParam.get("disciplina_codigo"));
    	String[] codigos = new String[100];
    	String[] nota = new String[100];

    	try {
    		if(cmd.contains("Salvar")) {
    			for(String key : parametros.keySet()) {
    				if(key.startsWith("matriculaCodigo")) {
    					codigos = parametros.get(key);
    				}
    				if(key.startsWith("nota")) {
    					nota = parametros.get(key);
    				}
    			}
    			
    			saida = salvarNotas(codigos, nota, avaliacao, disciplina);
    		}
    	} catch(Exception e) {
    		erro = e.getMessage();
    	} finally {
    		model.addAttribute("saida", saida);
    		model.addAttribute("erro", erro);
    	}
		return new ModelAndView("realizaravaliacao");
	}

	private String salvarNotas(String[] codigos, String[] nota, int avaliacao, int disciplina) throws Exception {
		int tam = codigos.length;
		String saida = "";
		for(int i=0;i<tam;i++) {
			if(Float.parseFloat(nota[i]) > 10 || Float.parseFloat(nota[i]) < 0) {
				throw new Exception("Nota inválida para a matricula " + codigos[i]);
			}else {				
				saida = naRep.sp_salvarnota(avaliacao, disciplina, Integer.parseInt(codigos[i]), Float.parseFloat(nota[i]));
			}
		}
		return "Avaliação salva";
	}	
}
