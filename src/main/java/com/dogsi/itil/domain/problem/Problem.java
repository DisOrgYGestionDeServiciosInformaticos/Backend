package com.dogsi.itil.domain.problem;

import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import javax.persistence.Table;
import javax.persistence.FetchType;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.dogsi.itil.domain.Impact;
import com.dogsi.itil.domain.Priority;
import com.dogsi.itil.domain.State;
import com.dogsi.itil.domain.changes.Change;
import com.dogsi.itil.domain.incident.Incident;




import com.dogsi.itil.domain.knownError.KnownError;


@Getter
@Setter
@Entity
@Table(name = "problem")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private Impact impact;

    @Column(nullable = false)
    private State state;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate reportedDate;

    @Column
    private Date closedDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
        name = "problem_incident_relation", 
        joinColumns = @JoinColumn(name = "problem_id"), 
        inverseJoinColumns = @JoinColumn(name = "incident_id"))
    private List<Incident> incidents;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinTable(
        name = "change_problem_relation", 
        joinColumns = @JoinColumn(name = "problem_id"), 
        inverseJoinColumns = @JoinColumn(name = "change_id"))
    private List<Change> changes;
    
    @Column
    private String emailOfUserInCharge;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "known_error_problem_relation", 
        joinColumns = @JoinColumn(name = "problem_id"), 
        inverseJoinColumns = @JoinColumn(name = "known_error_id"))
    private List<KnownError> knownErrors;

    @Builder
    public Problem(String name, String category, Priority priority, Impact impact, State state, String description,
            LocalDate reportedDate, Date closedDate, String emailOfUserInCharge) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.impact = impact;
        this.state = state;
        this.description = description;
        this.reportedDate = reportedDate;
        this.closedDate = closedDate;
        this.incidents = new ArrayList<>();
        this.emailOfUserInCharge = emailOfUserInCharge;
    }

    public void addIncidents(List<Incident> incidents) {
        this.incidents.addAll(incidents);
    }
}
