package TP02.utils;

import TP02.models.*;
import TP02.storage.*;
import java.time.LocalDate;
import java.util.ArrayList;

import java.time.LocalDate;
import java.util.ArrayList;

public class TesteCurso {

    public static void main(String[] args) throws Exception {
        // Criação do arquivo de cursos
        ArquivoCurso arquivoCurso = new ArquivoCurso();

        // 1. Testando a criação de cursos
        System.out.println("Criando cursos...");

        // Criar curso
        Curso curso1 = new Curso(-1, "Curso de Java", LocalDate.now(), "Descrição do curso", "codigo123", (byte) 0, 1);
        int idCurso1 = arquivoCurso.create(curso1);
        System.out.println("Curso criado: " + curso1.getNome());

        System.out.println("\nCurso criado com todos os atributos:");
        System.out.println(curso1);

        // Criar outro curso
        Curso curso2 = new Curso(-1, "Curso de Python", LocalDate.now(), "Descrição do curso Python", "codigo456", (byte) 0, 2);
        int idCurso2 = arquivoCurso.create(curso2);
        System.out.println("Curso criado: " + curso2.getNome());

        // 2. Testando a leitura de curso por ID
        System.out.println("\nTestando leitura de curso por ID...");
        Curso cursoLidoPorID = arquivoCurso.read(idCurso1);
        System.out.println("Curso lido por ID: " + cursoLidoPorID.getNome());

        // 3. Testando a leitura de curso por código compartilhável
        System.out.println("\nTestando leitura de curso por código compartilhável...");
        Curso cursoLidoPorCodigo = arquivoCurso.read("codigo123");
        System.out.println("Curso lido por código: " + cursoLidoPorCodigo.getNome());

        // 4. Testando a leitura de cursos por usuário
        System.out.println("\nTestando leitura de cursos por usuário (idUsuario = 1)...");
        ArrayList<Curso> cursosUsuario1 = arquivoCurso.readByUser(1);
        System.out.println("Cursos do usuário 1:");
        for (Curso c : cursosUsuario1) {
            System.out.println(c.getNome());
        }

        // 5. Testando a atualização de curso
        System.out.println("\nTestando a atualização de curso...");
        curso1.setNome("Curso de Java Atualizado");
        arquivoCurso.update(curso1);
        Curso cursoAtualizado = arquivoCurso.read(idCurso1);
        System.out.println("Curso atualizado: " + cursoAtualizado.getNome());

        // 6. Testando a exclusão de curso
        System.out.println("\nTestando a exclusão de curso...");
        boolean excluido = arquivoCurso.delete(idCurso2);
        System.out.println("Curso excluído: " + excluido);

        // Verificar se o curso foi realmente excluído
        Curso cursoExcluido = arquivoCurso.read(idCurso2);
        System.out.println("Curso com id " + idCurso2 + " após exclusão: " + (cursoExcluido == null ? "não encontrado" : cursoExcluido.getNome()));

        // 7. Testando leitura após exclusão (para garantir que o curso foi excluído)
        System.out.println("\nTestando leitura de cursos restantes após exclusão...");
        ArrayList<Curso> cursosRestantes = arquivoCurso.readByUser(1);
        System.out.println("Cursos restantes do usuário 1:");
        for (Curso c : cursosRestantes) {
            System.out.println(c.getNome());
        }
    }
}

