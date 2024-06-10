package br.fateczl.edu.SpringDataAGIS.controller;

import java.time.LocalDate;
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
import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.model.Matricula;
import br.fateczl.edu.SpringDataAGIS.repository.IAulaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IDisciplinaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AulaController {
	
	@Autowired
	private IAulaRepository auRep;
	
	@Autowired
	private IMatriculaDisciplinaRepository mdRep;
	
	@Autowired
	private IMatriculaRepository mRep;
	
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

	@SuppressWarnings({ "unchecked"})
	@RequestMapping(name="aula", value="/aula", method = RequestMethod.POST)
	public ModelAndView aulaPost (@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
		HttpSession session = request.getSession();
		Map<String, String[]> parametros = request.getParameterMap();
		String cmd = allRequestParam.get("botao");
		String disciplina = allRequestParam.get("disciplina");
		String dataAula = allRequestParam.get("dataAula");
		
		String[] presenca = new String[50];
		String erro = "";
		String saida = "";
		Disciplina d = new Disciplina();
		int qtdaula = 0;
		boolean finalizou = false;
		List<Integer> presencas = new ArrayList<>();
		List<Aluno> alunos = new ArrayList<>();
		List<Disciplina> disciplinas = new ArrayList<>();
		
		try {
			disciplinas = listarDisciplinas();			
			if(cmd.contains("Iniciar Chamada")) {
				d.setCodigo(Integer.parseInt(disciplina));
				d = dRep.findById(Integer.parseInt(disciplina)).get();
				qtdaula = d.getQtdAulas();
				if(verificarAula(d.getCodigo(), dataAula)) {
					erro = "Aula para o dia "+dataAula+" já foi realizada";
				}else {					
					alunos = listarAlunos(Integer.parseInt(disciplina));
				}
			}
			if(cmd.contains("Finalizar Chamada")) {
				d = (Disciplina) session.getAttribute("disciplina");
				alunos = (List<Aluno>) session.getAttribute("alunos");
				for(String key : parametros.keySet()) {
					if(key.startsWith("presenca")) {
						presenca = parametros.get(key);
					}
				}
				saida = finalizarChamada(alunos, presenca, d.getCodigo(), LocalDate.parse(dataAula));
				finalizou = true;
			}
		} catch (Exception e) {
			erro = e.getMessage();
		} finally {
			session.setAttribute("alunos", alunos);
			session.setAttribute("disciplina", d);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("dataAula", dataAula);
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			if(finalizou) {
				session.removeAttribute("alunos");
			}
		}
		
		return new ModelAndView("aula");
	}
	
	
	private List<Aluno> listarAlunos(int disciplina) {
		return auRep.listarAlunos(disciplina);
	}

	private boolean verificarAula(int codigo, String dataAula) {
		return auRep.sp_verificaraula(codigo, LocalDate.parse(dataAula));
	}

	private String finalizarChamada(List<Aluno> alunos, String[] presenca, int codigo, LocalDate dataAula) {
		String saida = "Chamada finalizada";
		for(int i=0;i<=alunos.size()-1;i++) {
			Matricula matricula = mRep.consultarUltimaMatricula(alunos.get(i).getRa());
			saida = auRep.sp_inseriraula(matricula.getCodigo(), codigo, Integer.parseInt(presenca[i]), dataAula);
		}
		return saida;
	}

	private List<Disciplina> listarDisciplinas() {
		return dRep.findAll();
	}
	
}
