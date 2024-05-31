package br.fateczl.edu.SpringDataAGIS.controller;

import java.sql.Date;
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
import br.fateczl.edu.SpringDataAGIS.model.Matricula;
import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MatriculaController {
	
	@Autowired
	IAlunoRepository aRep;
	
	@Autowired
	IMatriculaRepository mRep;
	
	@Autowired
	IMatriculaDisciplinaRepository mdRep;
	
	@RequestMapping(name = "matricula", value="/matricula", method = RequestMethod.GET)
	public ModelAndView matriculaGet (@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		LocalDate dataAtual = LocalDate.now();
		boolean intervaloSemestre = validarDataSemestral(dataAtual);
		
		model.addAttribute("intervalo", intervaloSemestre);
		return new ModelAndView("matricula");
	}
	
	@RequestMapping(name = "matricula", value="/matricula", method = RequestMethod.POST)
	public ModelAndView matriculaPost (@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
		Map<String, String[]> parametros = request.getParameterMap();
		String cmd = allRequestParam.get("botao");
		String ra = allRequestParam.get("ra") ;
		Matricula matricula = new Matricula();	
		String[] disciplinasSelecionadas = new String[50];
		
		String saida ="";
		String erro="";
		boolean listar = false;
		Aluno a = new Aluno();
		List<MatriculaDisciplina> matriculaDisciplinas = new ArrayList<>();
		
		try {
			if(cmd.contains("Iniciar Matricula")) { 
				matricula = ultimaMatricula(ra);
				if(validarDataMatricula(matricula.getDataMatricula())) {
					a.setRa(ra);
					matriculaDisciplinas = listarDisciplinas(ra);
				}else {
					erro = "Matricula já foi realizada";
				}
			}
			if(cmd.contains("Confirmar Matricula")) {
				for(String key : parametros.keySet()) {
					if(key.startsWith("disciplinasSelecionadas")) {
						disciplinasSelecionadas = parametros.get(key);
					}
				}
				inserirMatricula(disciplinasSelecionadas, ra);
				saida = "Matricula finalizada";
			}
			if(cmd.contains("Consultar Matricula")) {
				a.setRa(ra);
				matriculaDisciplinas = listarDisciplinas(ra);
				listar = true;
			}
		} catch(Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", matriculaDisciplinas);
			model.addAttribute("aluno", a);
			model.addAttribute("listar", listar);
		}
		return new ModelAndView("matricula");
	}
	
	private String inserirMatricula(String[] disciplinasSelecionadas, String ra) {
		int codigo = mRep.sp_gerarmatricula(ra);
		String saida = null;
		for(String str : disciplinasSelecionadas) {
			saida = mRep.sp_inserirMatricula(ra, codigo, Integer.parseInt(str));
		}
		return saida;
	}

	private List<MatriculaDisciplina> listarDisciplinas(String ra) {
		return mdRep.fn_listarultimamatricula(ra);
	}

	private boolean validarDataMatricula(LocalDate dataMatricula) {
		Date dataSql = Date.valueOf(dataMatricula);
		boolean validacao = false;
		LocalDate data = dataSql.toLocalDate();
		LocalDate semestre1Inicio = LocalDate.of(LocalDate.now().getYear(), 1, 14);
		LocalDate semestre1Final = LocalDate.of(LocalDate.now().getYear(), 1, 22);
		LocalDate semestre2Inicio = LocalDate.of(LocalDate.now().getYear(), 7, 14);
		LocalDate semestre2Final = LocalDate.of(LocalDate.now().getYear(), 7, 22);
		
		if((data.isAfter(semestre1Inicio)) && data.isBefore(semestre1Final)) {
			validacao = false;
		}else{
			if(data.isAfter(semestre2Inicio) && data.isBefore(semestre2Final)) {				
				validacao = false;				
			}else {
				validacao = true;
			}
		}
		return validacao;
	}

	public Matricula ultimaMatricula(String ra) {
		List<Matricula> temp = new ArrayList<>();
		List<Matricula> aux = new ArrayList<>();
		temp = mRep.findAll();
		for(Matricula m : temp) {
			if(m.getAluno().getRa() == ra) {
				aux.add(m);
			}
		}
		
		int ultimo = aux.size();
		Matricula mTemp = temp.get(ultimo);
		System.out.println(mTemp.toString());
		return mTemp;
	}
	
	private boolean validarDataSemestral(LocalDate dataAtual) {
		LocalDate semestre1Inicio = LocalDate.of(LocalDate.now().getYear(), 1, 14);
		LocalDate semestre1Final = LocalDate.of(LocalDate.now().getYear(), 1, 22);
		LocalDate semestre2Inicio = LocalDate.of(LocalDate.now().getYear(), 7, 14);
		LocalDate semestre2Final = LocalDate.of(LocalDate.now().getYear(), 7, 22);
		
		if((dataAtual.isAfter(semestre1Inicio) && dataAtual.isBefore(semestre1Final)) || (dataAtual.isAfter(semestre2Inicio) && dataAtual.isBefore(semestre2Final))) {
			return true;
		}else{
			return false;
		}
	}
}
