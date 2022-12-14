package com.dogsi.itil.services.knownError.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dogsi.itil.domain.knownError.KnownError;
import com.dogsi.itil.dto.IdWithName;
import com.dogsi.itil.dto.KnownErrorDto;
import com.dogsi.itil.exceptions.ItemNotFoundException;
import com.dogsi.itil.repositories.KnownErrorRepository;
import com.dogsi.itil.repositories.ProblemRepository;
import com.dogsi.itil.repositories.SolutionRepository;
import com.dogsi.itil.services.knownError.KnownErrorService;
import com.dogsi.itil.domain.State;
import com.dogsi.itil.domain.problem.Problem;

@Service
public class KnownErrorServiceImpl implements KnownErrorService {

    private KnownErrorRepository repository;
    private ProblemRepository problemRepository;
    private SolutionRepository solutionsRepository;

    public KnownErrorServiceImpl(KnownErrorRepository repository, ProblemRepository problemRepository,SolutionRepository solutionsRepository) {
        this.repository = repository;
        this.problemRepository = problemRepository;
        this.solutionsRepository = solutionsRepository;
    }

    @Override
    public void saveKnownError(KnownErrorDto dto) {
        var knownError = KnownError.builder()
            .name(dto.getName())
            .category(dto.getCategory())
            .description(dto.getDescription())
            .creationDate(dto.getCreationDate())
            .rootcause(dto.getRootcause())
            .build();        

        var ids = dto.getProblems();
        if(ids!=null && !ids.isEmpty()){
            var problems = problemRepository.findAllById(dto.getProblems());
            if(problems.size() != dto.getProblems().size()) {
                throw new ItemNotFoundException("Problem not found");
            }
            knownError.addProblems(problems);
        }
        
        ids = dto.getSolutions();
        if(ids!=null && !ids.isEmpty()){
            var solutions = solutionsRepository.findAllById(ids);
            if(solutions.size() != ids.size()) {
                throw new ItemNotFoundException("Solution not found");
            }
            knownError.addSolutions(solutions);
        }

        repository.save(knownError);
    }

    @Override
    public Page<KnownError> getKnownError(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void updateKnownError(Long id, KnownErrorDto dto) {
        solutionsRepository.deleteSolutionsRelationships(id);
        problemRepository.deleteProblemsKnownErrorRelationships(id);

        var knownError = repository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("KnownError with id " + id + " not found");
        });
        
        var ids = dto.getProblems();
        if(ids!=null && !ids.isEmpty()){
            var problems = problemRepository.findAllById(ids);
            if(problems.size() != ids.size()) {
                throw new ItemNotFoundException("Problem not found");
            }
            knownError.addProblems(problems);
        }

        ids = dto.getSolutions();
        if(ids!=null && !ids.isEmpty()){
            var solutions = solutionsRepository.findAllById(ids);
            if(solutions.size() != ids.size()) {
                throw new ItemNotFoundException("Solution not found");
            }
            knownError.addSolutions(solutions);
        }

        knownError.setName(dto.getName());
        knownError.setCategory(dto.getCategory());
        knownError.setDescription(dto.getDescription());
        knownError.setCreationDate(dto.getCreationDate());
        knownError.setRootcause(dto.getRootcause());

        repository.save(knownError);
    }

    @Override
    public void deleteKnownError(Long id) {
        int deleted = repository.deleteKnownErrorById(id);
        if (deleted == 0) {
            throw new ItemNotFoundException("KnownError with id " + id + " not found");
        }
    }

    @Override
    public KnownError getKnownErrorById(Long id) {
        return repository.findById(id).orElseThrow(() -> {throw new ItemNotFoundException("KnownError with id " + id + " not found");});
    }

    @Override
    public Page<IdWithName> getIdsWithNames(Pageable pageable) {
        return repository.getIdsAndNamesOfIncidents(pageable);
    }

}