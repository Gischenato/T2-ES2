package br.pucrs.matricula_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Discipline {
    private long id;
    private String name;
    private String horario;
    private String turma;
    private String classCode;
}
