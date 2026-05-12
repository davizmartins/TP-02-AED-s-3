package TP02.views;

import TP02.models.*;
import java.util.Scanner;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisaoCurso {
    private Scanner scanner = new Scanner(System.in);

    // Exibe os cursos do usuário
    public void mostrarCursos(Curso[] cursos) {
        System.out.println("\n");
        System.out.println(" > Início > Meus Cursos                ");
        System.out.println("");
        
        if (cursos.length == 0) {
            System.out.println(" Você não tem cursos cadastrados.");
        } else {
            for (int i = 0; i < cursos.length; i++) {
                String nome = cursos[i].getNome();
                if (nome.length() > 28) {
                    nome = nome.substring(0, 25) + "...";
                }
                System.out.println(" (" + (i + 1) + ") " + nome + " - " + cursos[i].getDataInicio());
            }
        }
        
        System.out.println("");
        System.out.println(" (A) Novo curso                        ");
        System.out.println(" (R) Retornar                          ");
        System.out.println("");
        System.out.print("Opção: ");
    }

    // Mostra detalhes de um curso
    public void mostrarDetalheCurso(Curso curso) {
        String estado = obterTextoEstado(curso.getEstado());
        System.out.println("\n");
        System.out.println(" > Início > Meus Cursos > " + curso.getNome());
        System.out.println("");
        System.out.println(" CÓDIGO......: " + curso.getCodigoCompartilhavel());
        System.out.println(" NOME........: " + curso.getNome());
        System.out.println(" DATA INÍCIO.: " + curso.getDataInicio());
        System.out.println(" ESTADO......: " + estado);
        System.out.println(" DESCRIÇÃO...: " + curso.getDescricao());
        System.out.println("");
        System.out.println(" (A) Gerenciar inscritos               ");
        System.out.println(" (B) Corrigir dados                    ");
        System.out.println(" (C) Alterar data do curso             ");
        System.out.println(" (D) Encerrar inscrições               ");
        System.out.println(" (E) Concluir curso                    ");
        System.out.println(" (F) Cancelar curso                    ");
        System.out.println(" (R) Retornar                          ");
        System.out.println("");
        System.out.print("Opção: ");
    }

    // Pega dados de novo curso
    public Curso pegarDadosNovoCurso() {
        System.out.println("\n");
        System.out.println("--- Novo Curso ---");
        System.out.println("");
        System.out.print("Nome do curso: ");
        String nome = scanner.nextLine();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Data de início (DD/MM/AAAA): ");
        String dataInicioString = scanner.nextLine();
        LocalDate dataInicio = LocalDate.parse(dataInicioString, formatter);

        System.out.print("Descrição do curso: ");
        String descricao = scanner.nextLine();
        
        String codigoCompartilhavel = gerarCodigoCompartilhavel();
        return new Curso(0, nome, dataInicio, descricao, codigoCompartilhavel, Curso.ATIVO_INSCRICOES, -1);
    }

    // Pega dados para atualizar curso
    public Curso pegarDadosAtualizacaoCurso(Curso cursoAtual) {
        System.out.println("\n");
        System.out.println("--- Atualizar Curso ---");
        System.out.println("");
        System.out.print("Novo nome (atual: " + cursoAtual.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            cursoAtual.setNome(novoNome);
        }

        System.out.print("Nova descrição (atual: " + cursoAtual.getDescricao() + "): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.isEmpty()) {
            cursoAtual.setDescricao(novaDescricao);
        }

        return cursoAtual;
    }

    // Pega nova data para alterar o curso
    public LocalDate pegarNovaDataCurso(LocalDate dataAtual) {
        System.out.println("\n");
        System.out.println("--- Alterar Data do Curso ---");
        System.out.println("Data atual: " + dataAtual);
        System.out.println("");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Nova data (DD/MM/AAAA): ");
        String novaDataString = scanner.nextLine();
        
        try {
            return LocalDate.parse(novaDataString, formatter);
        } catch (Exception e) {
            System.out.println(" Formato de data inválido!");
            return dataAtual;
        }
    }

    // Pega opção do usuário
    public char pegarOpcao() {
        String entrada = scanner.nextLine().toUpperCase().trim();
        if (entrada.length() > 0) {
            return entrada.charAt(0);
        }
        return ' ';
    }

    // Métodos auxiliares
    private String gerarCodigoCompartilhavel() {
        return UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    private String obterTextoEstado(int estado) {
        switch (estado) {
            case 0: return "Ativo e recebendo inscrições";
            case 1: return "Ativo, mas sem novas inscrições";
            case 2: return "Realizado e concluído";
            case 3: return "Cancelado";
            default: return "Desconhecido";
        }
    }

    // Mensagens
    public void mostrarMensagemSucessoCadastro() {
        System.out.println(" Curso cadastrado com sucesso!");
    }

    public void mostrarMensagemSucesso(String mensagem) {
        System.out.println(" " + mensagem);
    }

    public void mostrarMensagemErro(String mensagem) {
        System.out.println(" " + mensagem);
    }

    public void mostrarMensagemOpcaoInvalida() {
        System.out.println(" Opção inválida!");
    }
}

