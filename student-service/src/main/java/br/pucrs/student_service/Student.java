package br.pucrs.student_service;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long registrationNumber;
    
	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	String documentNumber;

	@ManyToMany(mappedBy = "students")
	@JsonIgnoreProperties("students")
	List<Discipline> disciplines;
}
