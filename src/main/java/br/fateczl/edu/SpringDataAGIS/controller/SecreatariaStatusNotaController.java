package br.fateczl.edu.SpringDataAGIS.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;
import br.fateczl.edu.SpringDataAGIS.repository.IDisciplinaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class SecreatariaStatusNotaController {
	
	@Autowired
	private IMatriculaDisciplinaRepository mdRep;
	
	@Autowired
	private IDisciplinaRepository dRep;
	
	@Autowired
	DataSource ds;

	@RequestMapping(name="relatorionota", value="/relatorionota", method = RequestMethod.GET)
	public ModelAndView secretariaStatusNotaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String erro = "";
		List<Disciplina> disciplinas = new ArrayList<>();
		try {
			disciplinas = listarDisciplinas();
		} catch(Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", disciplinas);
		}
		return new ModelAndView("relatorionota");
	}
	
	@RequestMapping(name="relatorionota", value="/relatorionota", method = RequestMethod.POST)
	public ModelAndView secretariaStatusNotaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String disciplina = allRequestParam.get("disciplina");
		
		String saida = "";
		String erro = "";
		List<Disciplina> disciplinas = new ArrayList<>();
		List<MatriculaDisciplina> alunos = new ArrayList<>();
		
		try {
			disciplinas = listarDisciplinas();
			if(cmd.contains("Listar")) {
				alunos = buscarAlunos(disciplina);
			}
		} catch(Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("alunos", alunos);
			model.addAttribute("disciplinas", disciplinas);
		}
		return new ModelAndView("relatorionota");
	}
	
	private List<Disciplina> listarDisciplinas() {
		return dRep.findAll();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(name="relatorionotas", value="/relatorionotas", method = RequestMethod.POST)
	public ResponseEntity secreatariaRelatorioNotaPost(@RequestParam Map<String, String> allRequestParam) {
		String erro = "";
		Map<String, Object> paramRelatorio = new HashMap<String, Object>();
		paramRelatorio.put("disciplina_codigo", allRequestParam.get("disciplina"));
		
		byte[] bytes = null;
		InputStreamResource resource = null;
		HttpStatus status = null;
		HttpHeaders header = new HttpHeaders();
		
		try {
			Connection c = DataSourceUtils.getConnection(ds);
			File arquivo = ResourceUtils.getFile("classpath:reports/relatorionota.jasper");
			JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(arquivo.getAbsolutePath());
			bytes = JasperRunManager.runReportToPdf(report, paramRelatorio, c);
		} catch(FileNotFoundException | JRException e) {
			erro = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		} finally {
			if(erro.equals("")) {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
				resource = new InputStreamResource(inputStream);
				header.setContentLength(bytes.length);
				header.setContentType(MediaType.APPLICATION_PDF);
				status = HttpStatus.OK;
			}
		}
		return new ResponseEntity(resource, header, status);
	}
	
	private List<MatriculaDisciplina> buscarAlunos(String disciplina) throws Exception {
		if(mdRep.listarAlunos(Integer.parseInt(disciplina)).isEmpty()) {
			throw new Exception("Não há alunos cadastrados nessa disciplina");
		}else {
			return mdRep.listarAlunos(Integer.parseInt(disciplina));			
		}
	}
	
}
