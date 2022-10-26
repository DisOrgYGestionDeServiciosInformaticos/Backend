package com.dogsi.itil.services.problem.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dogsi.itil.domain.problem.Problem;
import com.dogsi.itil.dto.ProblemDto;
import com.dogsi.itil.exceptions.ItemNotFoundException;
import com.dogsi.itil.repositories.ProblemRepository;
import com.dogsi.itil.services.problem.ProblemService;

@Service
public class ProblemServiceImpl implements ProblemService {

    private ProblemRepository repository;

    public ProblemServiceImpl(ProblemRepository repository) {
        this.repository = repository;
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
                .build();
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
        problem.setName(dto.getName());
        problem.setCategory(dto.getCategory());
        problem.setPriority(dto.getPriority());
        problem.setImpact(dto.getImpact());
        problem.setState(dto.getState());
        problem.setDescription(dto.getDescription());
        problem.setReportedDate(dto.getReportedDate());
        problem.setClosedDate(dto.getClosedDate());

        repository.save(problem);
    }

    @Override
    public void deleteProblem(Long id) {
        int deleted = repository.deleteProblemById(id);
        if (deleted == 0) {
            throw new ItemNotFoundException("Problem with id " + id + " not found");
        }
    }

    public Problem getProblemById(Long id) {
        return repository.findById(id).orElseThrow(() -> {throw new ItemNotFoundException("Problem with id " + id + " not found");});
    }
}
