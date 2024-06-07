package br.fateczl.edu.SpringDataAGIS.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RealizarAvaliacaoController {
    
    @GetMapping("/realizaravaliacao")
	@RequestMapping(name="realizaravaliacao", value="/realizaravaliacao", method = RequestMethod.GET)
    public ModelAndView realizaravaliacaoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
    	
    	
    	return new ModelAndView("realizaravaliacao");
      
    }
    
	@RequestMapping(name="realizaravaliacao", value="/realizaravaliacao", method = RequestMethod.POST)
	public ModelAndView realizaravaliacaoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		return new ModelAndView("realizaravaliacao");
	}
  
		
}
