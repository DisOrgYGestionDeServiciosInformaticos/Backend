package com.dogsi.itil.services.problem.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dogsi.itil.domain.problem.Problem;
import com.dogsi.itil.dto.ProblemDto;
import com.dogsi.itil.exceptions.ItemNotFoundException;
import com.dogsi.itil.repositories.ProblemRepository;
import com.dogsi.itil.repositories.IncidentRepository;
import com.dogsi.itil.repositories.WorkaroundRepository;
import com.dogsi.itil.services.problem.ProblemService;

import com.dogsi.itil.domain.incident.Incident;
import com.dogsi.itil.domain.problem.workaround.Workaround;

@Service
public class ProblemServiceImpl implements ProblemService {

    private ProblemRepository repository;
    private IncidentRepository incidentRepository;
    private WorkaroundRepository workaroundRepository;

    public ProblemServiceImpl(ProblemRepository repository, IncidentRepository incidentRepository,
    WorkaroundRepository workaroundRepository) {
        this.repository = repository;
        this.incidentRepository = incidentRepository;
        this.workaroundRepository = workaroundRepository;
    }

    @Override
    public void saveProblem(ProblemDto dto) {
        var problem = Problem.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .priority(dto.getPriority())
                .impact(dto.getImpact())
                .state(dto.getState())
                .description(dto.getDescription())
                .reportedDate(dto.getReportedDate())
                .closedDate(dto.getClosedDate())
                .rootCause(dto.getRootCause())
                .emailOfUserInCharge(dto.getEmailOfUserInCharge())
                .build();

        var ids = dto.getIncidentIds();
        if(ids!=null && !ids.isEmpty()){
            var incidents = incidentRepository.findAllById(dto.getIncidentIds());
            if(incidents.size() != dto.getIncidentIds().size()) {
                throw new ItemNotFoundException("Incident not found");
            }
            problem.addIncidents(incidents);
        }

        repository.save(problem);
    }

    @Override
    public Page<Problem> getProblem(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void updateProblem(Long id, ProblemDto dto) {
        var problem = repository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Problem with id " + id + " not found");
        });

        var ids = dto.getIncidentIds();
        if(ids!=null && !ids.isEmpty()){
            var incidents = incidentRepository.findAllById(dto.getIncidentIds());
            if(incidents.size() != dto.getIncidentIds().size()) {
                throw new ItemNotFoundException("Incident not found");
            }
            problem.addIncidents(incidents);
        }

        var workaround_ids = dto.getWorkaroundIds();
        if(workaround_ids!=null && !workaround_ids.isEmpty()){
            var workarounds = workaroundRepository.findAllById(dto.getWorkaroundIds());
            if(workarounds.size() != dto.getWorkaroundIds().size()) {
                throw new ItemNotFoundException("Workaround not found");
            }
            problem.addWorkarounds(workarounds);
        }
        
        problem.setName(dto.getName());
        problem.setCategory(dto.getCategory());
        problem.setPriority(dto.getPriority());
        problem.setImpact(dto.getImpact());
        problem.setState(dto.getState());
        problem.setDescription(dto.getDescription());
        problem.setReportedDate(dto.getReportedDate());
        problem.setClosedDate(dto.getClosedDate());
        problem.setRootCause(dto.getRootCause());
        problem.setEmailOfUserInCharge(dto.getEmailOfUserInCharge());

        repository.save(problem);

    }

    @Override
    public void deleteProblem(Long id) {
        int deleted = repository.deleteProblemById(id);
        if (deleted == 0) {
            throw new ItemNotFoundException("Problem with id " + id + " not found");
        }
    }

    @Override
    public Problem getProblemById(Long id) {
        return repository.findById(id).orElseThrow(() -> {throw new ItemNotFoundException("Problem with id " + id + " not found");});
    }

}