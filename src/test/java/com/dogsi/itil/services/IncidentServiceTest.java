package com.dogsi.itil.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.dogsi.itil.dto.IncidentDto;
import com.dogsi.itil.exceptions.ItemNotFoundException;
import com.dogsi.itil.repositories.IncidentRepository;
import com.dogsi.itil.services.incident.IncidentService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
@ActiveProfiles("test-h2")
public class IncidentServiceTest {
    
    @Autowired
    private IncidentRepository repository;

    @Autowired
    private IncidentService service;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void shouldSaveAnIncident(){
        var dto = new IncidentDto();
        dto.setName("Name");
        dto.setCategory("capa 8");
        dto.setPriority("P1");
        dto.setImpact("High");
        dto.setReportedDate(Instant.now());
        dto.setDescription("description");
        dto.setState("Open");
        dto.setAssignee("Nadie");
        dto.setClosedDate(new Date());

        service.saveIncident(dto);

        assertEquals(1, repository.count());

        var saved = repository.findAll().get(0);
        assertEquals("Name", saved.getName());
    }

    @Test
    void shouldReturnAllIncident(){
        var dto = new IncidentDto();
        dto.setName("Name");
        dto.setCategory("capa 8");
        dto.setPriority("P1");
        dto.setImpact("High");
        dto.setReportedDate(Instant.now());
        dto.setDescription("description");
        dto.setState("Open");
        dto.setAssignee("Nadie");
        dto.setClosedDate(new Date());

        service.saveIncident(dto);
        service.saveIncident(dto);
        service.saveIncident(dto);

        var results = service.getIncident(Pageable.unpaged());
        assertEquals(3, results.getTotalElements());
    }

    @Test
    void shouldThrowAnExceptionIfTheIncidentIsNotFoundWhenUpdating(){
        var dto = new IncidentDto();
        dto.setName("Name");
        dto.setCategory("capa 8");
        dto.setPriority("P1");
        dto.setImpact("High");
        dto.setReportedDate(Instant.now());
        dto.setDescription("description");
        dto.setState("Open");
        dto.setAssignee("Nadie");
        dto.setClosedDate(new Date());

        assertThrows(ItemNotFoundException.class, () -> {
            service.updateIncident(1L, dto);
        });
    }

    @Test
    void shouldThrowAnExceptionIfTheIncidentIsNotFoundWhenDeleting(){
        assertThrows(ItemNotFoundException.class, () -> {
            service.deleteIncident(1L);
        });
    }

    @Test
    void shouldDeleteAnIncident(){
        var dto = new IncidentDto();
        dto.setName("Name");
        dto.setCategory("capa 8");
        dto.setPriority("P1");
        dto.setImpact("High");
        dto.setReportedDate(Instant.now());
        dto.setDescription("description");
        dto.setState("Open");
        dto.setAssignee("Nadie");
        dto.setClosedDate(new Date());

        service.saveIncident(dto);
        assertEquals(1, repository.count());
        var saved = repository.findAll().get(0);

        service.deleteIncident(saved.getId());

        assertEquals(0, repository.count());
    }


    @Test
    void shouldUpdateAnIncident(){
        var dto = new IncidentDto();
        dto.setName("Name");
        dto.setCategory("capa 8");
        dto.setPriority("P1");
        dto.setImpact("High");
        dto.setReportedDate(Instant.now());
        dto.setDescription("description");
        dto.setState("Open");
        dto.setAssignee("Nadie");
        dto.setClosedDate(new Date());

        service.saveIncident(dto);
        assertEquals(1, repository.count());
        var saved = repository.findAll().get(0);

        dto.setName("Name2");
        
        service.updateIncident(saved.getId(), dto);

        assertEquals(1, repository.count());
        saved = repository.findAll().get(0);
        assertEquals("Name2", saved.getName());
    }
}