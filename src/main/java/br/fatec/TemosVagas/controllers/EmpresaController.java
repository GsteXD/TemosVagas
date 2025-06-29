package br.fatec.TemosVagas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.fatec.TemosVagas.dtos.empresa.EmpresaDTO;
import br.fatec.TemosVagas.dtos.empresa.EmpresaEditarDTO;
import br.fatec.TemosVagas.entities.Empresa;
import br.fatec.TemosVagas.services.EmpresaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {
    
    @Autowired
    EmpresaService empresaService;

    @PostMapping("/cadastrar")
    public @ResponseBody ResponseEntity<EmpresaDTO> cadastrar(@RequestBody @Valid EmpresaDTO empresaDTO) {
        Empresa empresa = empresaService.cadastrar(EmpresaDTO.toEmpresa(empresaDTO));
        return ResponseEntity.ok().body(EmpresaDTO.valueOf(empresa));
    }

    @PutMapping("/atualizar")
    public @ResponseBody ResponseEntity<EmpresaEditarDTO> atualizar(@RequestBody @Valid EmpresaEditarDTO empresaEditarDTO) {
        Empresa empresa = empresaService.atualizar(EmpresaEditarDTO.toEmpresa(empresaEditarDTO));
        return ResponseEntity.ok().body(EmpresaEditarDTO.valueOf(empresa));
    }

    @GetMapping("/carregar")
    public @ResponseBody ResponseEntity<EmpresaEditarDTO> carregar() {
        Empresa empresa = empresaService.carregarEmpresaLogada();
        return ResponseEntity.ok().body(EmpresaEditarDTO.valueOf(empresa));
    }

    @GetMapping("/procurar/{id}")
    public @ResponseBody ResponseEntity<EmpresaDTO> procurar(@PathVariable Long id) {
        Empresa empresa = empresaService.findById(id);
        return ResponseEntity.ok().body(EmpresaDTO.valueOf(empresa));
    }

}
