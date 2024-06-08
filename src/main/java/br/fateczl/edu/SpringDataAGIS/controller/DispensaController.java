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

import br.fateczl.edu.SpringDataAGIS.model.Aluno;
import br.fateczl.edu.SpringDataAGIS.model.Disciplina;
import br.fateczl.edu.SpringDataAGIS.model.Dispensa;
import br.fateczl.edu.SpringDataAGIS.model.MatriculaDisciplina;
import br.fateczl.edu.SpringDataAGIS.repository.IAlunoRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IDisciplinaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IDispensaRepository;
import br.fateczl.edu.SpringDataAGIS.repository.IMatriculaDisciplinaRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class DispensaController {
    
    @Autowired
    private IAlunoRepository aRep;
    
    @Autowired
    private IMatriculaDisciplinaRepository mdRep;
    
    @Autowired
    private IDispensaRepository dispRep;
    
    @Autowired
    private IDisciplinaRepository discRep;
    
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
                disciplinas = listarDisciplinas(ra); 
                dispensas = listarDispensas(ra); 
                found = true;
            }
            if(cmd.contains("Solicitar Dispensa")) {
                d.setCodigo(Integer.parseInt(disciplina));
                a = buscarAluno(ra);
                d = buscarDisciplina(d); 
                saida = solicitarDispensa(a, d, motivo); 
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
    
    private String solicitarDispensa(Aluno a, Disciplina d, String motivo) {
        return dispRep.sp_alunodispensa(a.getRa(), d.getCodigo(), motivo);
    }

    private Disciplina buscarDisciplina(Disciplina d) {
        return discRep.findById(d.getCodigo()).get();
    }

    private List<Dispensa> listarDispensas(String ra) {
        List<Dispensa> dis = dispRep.findAll();
        List<Dispensa> aux = new ArrayList<>();
        for(Dispensa d : dis) {
            if(d.getAluno().getRa().contains(ra)) {
                aux.add(d);
            }
        }
        return aux;
    }

    private List<Disciplina> listarDisciplinas(String ra) {
        List<MatriculaDisciplina> md = mdRep.fn_listarultimamatricula(ra);
        List<Disciplina> d = new ArrayList<>();
        for(MatriculaDisciplina m : md) {
            d.add(m.getDisciplina());
        }
        return d;
    }

    private Aluno buscarAluno(String ra) throws Exception {
        return aRep.findById(ra).orElseThrow(() -> new Exception("Aluno não encontrado"));
    }
}
