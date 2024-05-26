package br.fateczl.edu.SpringDataAGIS.controller;

import java.sql.Date;
import java.sql.SQLException;
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
import br.fateczl.edu.SpringDataAGIS.model.Curso;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;
import br.fateczl.edu.SpringDataAGIS.repository.ICursoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AlunoController {
	
	@Autowired
	IAlunoRepository aRep;
	
	@Autowired
	ICursoRepository cRep;
	
	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.GET)
	public ModelAndView alunoGet(@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model) {
		HttpSession session = request.getSession();
		session.invalidate();
		String erro = "";
		List<Curso> cursos = new ArrayList<>();
		
		try {
			cursos = listarCursos();
		} catch (Exception e){
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("cursos", cursos);
		}
		
		return new ModelAndView("aluno");
	}

	@RequestMapping(name="aluno", value="/aluno", method = RequestMethod.POST)
	public ModelAndView alunoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String ra = allRequestParam.get("ra");
		String cpf = allRequestParam.get("cpf");
		String nome = allRequestParam.get("nome");
		String nomeSocial = allRequestParam.get("nomeSocial");
		String dataNasc = allRequestParam.get("dataNasc");
		String telefoneCelular = allRequestParam.get("telefoneCelular");
		String telefoneResidencial = allRequestParam.get("telefoneResidencial");
		String emailPessoal = allRequestParam.get("emailPessoal");
		String emailCorporativo = allRequestParam.get("emailCorporativo");
		String dataSegundoGrau = allRequestParam.get("dataSegundoGrau");
		String instituicaoSegundoGrau = allRequestParam.get("instituicaoSegundoGrau");
		String pontuacaoVestibular = allRequestParam.get("pontuacaoVestibular");
		String posicaoVestibular = allRequestParam.get("posicaoVestibular");
		String anoIngresso = allRequestParam.get("anoIngresso");
		String semestreIngresso = allRequestParam.get("semestreIngresso");
		String semestreGraduacao = allRequestParam.get("semestreGraduacao");
		String curso = allRequestParam.get("curso");
		String turno = allRequestParam.get("turno");
		
		String saida="";
		String erro="";
		Aluno a = new Aluno();
		List<Aluno> alunos = new ArrayList<>();
		List<Curso> cursos = new ArrayList<>();
		Curso cr = new Curso();
		
		try {
			cursos = listarCursos();
			if(!cmd.contains("Listar")) {
				cr = buscarCurso(curso);
//				a.setCurso(cr);
				a.setCpf(cpf);
			}
			if(cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				a.setCpf(cpf);
				a.setRa(ra);
				a.setNome(nome);
				a.setNomeSocial(nomeSocial);
				a.setDataNasc(LocalDate.parse(dataNasc));
				a.setTelefoneCelular(telefoneCelular);
				a.setTelefoneResidencial(telefoneResidencial);
				a.setEmailPessoal(emailPessoal);
				a.setEmailCorporativo(emailCorporativo);
				a.setDataSegundoGrau(LocalDate.parse(dataSegundoGrau));
				a.setInstituicaoSegundoGrau(instituicaoSegundoGrau);
				a.setPontuacaoVestibular(Double.parseDouble(pontuacaoVestibular));
				a.setPosicaoVestibular(Integer.parseInt(posicaoVestibular));
				a.setAnoIngresso(anoIngresso);
				a.setSemestreIngresso(semestreIngresso);
				a.setSemestreGraduacao(semestreGraduacao);
				cr = buscarCurso(curso);
//				a.setCurso(cr);
				a.setTurno(turno);
			}
			if(cmd.contains("Cadastrar")) {
				saida = cadastrarAluno(a);
				a = null;
			}
			if(cmd.contains("Alterar")) {
				saida = atualizarAluno(a);
				a = null;
			}
			if(cmd.contains("Excluir")) {
				saida = excluirAluno(a);
				a = null;
			}
			if(cmd.contains("Buscar")) {
				a = buscarAluno(a);
//				cr = a.getCurso();
			}
			if(cmd.contains("Listar")) {
				alunos = listarAlunos();
			}
		} catch(Exception e){
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("alunos", alunos);
			model.addAttribute("curso", cr);
			model.addAttribute("cursos", cursos);
		}
		
		return new ModelAndView("aluno");
	}
	
	private String cadastrarAluno(Aluno a) {
		aRep.save(a);
		return "Aluno inserido com sucesso";
	}
	
	private String atualizarAluno(Aluno a) {
		aRep.save(a);
		return "Aluno atualizado com sucesso";
	}
	
	private String excluirAluno(Aluno a) {
		aRep.delete(a);
		return "Aluno excluido com sucesso";
	}
	
	private Aluno buscarAluno(Aluno a) {
		return aRep.findById(a.getCpf()).get();
	}
	
	private List<Aluno> listarAlunos(){
		List<Aluno> alunos = new ArrayList<>();
		alunos = aRep.findAll();
		return alunos;
	}
		
	private Curso buscarCurso(String curso) {
		return cRep.findById(Integer.parseInt(curso)).get();
	}

	private List<Curso> listarCursos() {
		return cRep.findAll();
	}
}
