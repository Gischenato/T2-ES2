package br.pucrs.student_service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDisciplinesDTO {
    private String name;
    private String horario;
    private String turma;
    String classCode;
    
    public StudentDisciplinesDTO(Discipline discipline) {
        this.name = discipline.getName();
        this.horario = discipline.getHorario();
        this.turma = discipline.getTurma();
        this.classCode = discipline.getClassCode();
    }
}
