package com.dogsi.itil.controllers.incident;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dogsi.itil.domain.incident.Incident;
import com.dogsi.itil.dto.IdWithName;
import com.dogsi.itil.dto.IncidentDto;
import com.dogsi.itil.dto.IncidentMetricsDto;
import com.dogsi.itil.services.incident.IncidentMetricsService;
import com.dogsi.itil.services.incident.IncidentService;

@RestController
@RequestMapping("api/v1/incident")
public class IncidentController {

    private IncidentService service;
    private IncidentMetricsService metrics;
    
    public IncidentController(IncidentService service, IncidentMetricsService metrics) {
        this.service = service;
        this.metrics = metrics;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIncident(@PathVariable Long id, @RequestBody @Valid IncidentDto incidentDto){
        service.updateIncident(id, incidentDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Incident getIncident(@PathVariable Long id){
        return service.getIncidentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addIncident( @RequestBody @Valid IncidentDto incidentDto){
        service.saveIncident(incidentDto);
    }

    @GetMapping
    public Page<Incident> getIncidents(Pageable pageable){
        return service.getIncident(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIncident(@PathVariable Long id){
        service.deleteIncident(id);
    }

    @GetMapping("/ids-with-names")
    public Page<IdWithName> getIncidentIdsWithNames(Pageable pageable){
        return service.getIncidentIdsWithNames(pageable);
    }

    @GetMapping("/metrics")
    public IncidentMetricsDto getIncidentIdsWithNames(){
        return metrics.getMetrics();
    }
}
