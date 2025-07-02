package br.fatec.TemosVagas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.fatec.TemosVagas.dtos.vaga.VagaRelatorioDTO;
import br.fatec.TemosVagas.entities.Empresa;
import br.fatec.TemosVagas.repositories.VagaRepository;

@Service
public class RelatorioService {

    @Autowired
    private VagaRepository vagaRepository;
    
    // Cria um relatório apenas para a empresa logada.
    public List<VagaRelatorioDTO> relatorioVagaEmpresa() {
        try {
            Empresa empresaLogada = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return vagaRepository.gerarRelatorioEmpresa(empresaLogada.getId());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    //Gera um relatório global baseado no grupo da empresa Logada.
    public List<VagaRelatorioDTO> relatorioVagaGrupo() {
        try {
            Empresa empresaLogada = (Empresa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (empresaLogada.getGrupo() == null || empresaLogada.getGrupo().isBlank()) {
                throw new RuntimeException("A empresa não pertence a um grupo.");
            }
            return vagaRepository.gerarRelatorioGlobalPorGrupo(empresaLogada.getGrupo());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o relatório: " + e.getMessage());
        }
    }
}
