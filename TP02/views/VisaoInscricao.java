package TP02.views;

import TP02.models.*;
import java.util.Scanner;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VisaoInscricao {
    private Scanner scanner = new Scanner(System.in);
    private static final int ITENS_POR_PAGINA = 10;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int paginaAtual = 1;
    private Curso[] cursosPaginados;
    private int totalPaginas;

    /**
     * Menu principal de inscrições
     */
    public void mostrarMenuInscricoes() {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println("========================================");
        System.out.println(" > Início > Minhas inscrições         ");
        System.out.println("");
        System.out.println(" (A) Buscar curso por código          ");
        System.out.println(" (B) Buscar curso por palavras-chave  ");
        System.out.println(" (C) Listar todos os cursos           ");
        System.out.println(" (R) Retornar ao menu anterior        ");
        System.out.print("Opção: ");
    }

    /**
     * Mostra a tela de Minhas Inscrições com lista e opções de ação
     */
    public void mostrarTelaMinhasInscricoes(Curso[] cursos) {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println("========================================");
        System.out.println(" > Início > Minhas inscrições         ");
        System.out.println("");

        if (cursos == null || cursos.length == 0) {
            System.out.println(" Você não tem inscrições registradas.");
        } else {
            System.out.println(" Inscrições atuais:");
            System.out.println("");
            for (int i = 0; i < Math.min(cursos.length, 10); i++) {
                String estado = obterEstadoCurso(cursos[i].getEstado());
                System.out.println(" (" + (i + 1) + ") " + cursos[i].getNome() + " - " + cursos[i].getDataInicio().format(FORMATO_DATA) + " " + estado);
            }
        }

        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println(" Opções:");
        System.out.println(" (A) Buscar curso por código          ");
        System.out.println(" (B) Buscar curso por palavras-chave  ");
        System.out.println(" (C) Listar todos os cursos           ");
        System.out.println(" (R) Retornar ao menu anterior        ");
        System.out.print("Opção: ");
    }

    /**
     * Exibe lista de cursos com paginação
     */
    public void mostrarListaCursos(Curso[] cursos, String titulo) {
        if (cursos == null || cursos.length == 0) {
            System.out.println("\n       EntrePares 1.0                  ");
            System.out.println("========================================");
            System.out.println(" > Início > Minhas inscrições > " + titulo);
            System.out.println("\n Nenhum curso encontrado.");
            return;
        }

        // Calcular paginação
        this.cursosPaginados = cursos;
        this.totalPaginas = (int) Math.ceil((double) cursos.length / ITENS_POR_PAGINA);

        if (paginaAtual > totalPaginas) {
            paginaAtual = totalPaginas;
        }

        // Exibir página atual
        exibirPagina(titulo);
    }

    /**
     * Exibe a página atual
     */
    private void exibirPagina(String titulo) {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println("========================================");
        System.out.println(" > Início > Minhas inscrições > " + titulo);
        System.out.println("\nPágina " + paginaAtual + " de " + totalPaginas + "\n");

        int indiceInicio = (paginaAtual - 1) * ITENS_POR_PAGINA;
        int indiceFim = Math.min(indiceInicio + ITENS_POR_PAGINA, cursosPaginados.length);

        for (int i = indiceInicio; i < indiceFim; i++) {
            int numeroItem = i - indiceInicio;
            Curso c = cursosPaginados[i];
            String estado = obterEstadoCurso(c.getEstado());
            System.out.println("(" + numeroItem + ") " + c.getNome() + " - " + c.getDataInicio().format(FORMATO_DATA) + " " + estado);
        }

        if (totalPaginas > 1) {
            System.out.println("\n----------------------------------------");
            System.out.println(" (A) Página anterior");
            System.out.println(" (B) Próxima página");
        } else {
            System.out.println("\n----------------------------------------");
        }
        System.out.println(" (R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
    }

    /**
     * Retorna o estado do curso como string
     */
    private String obterEstadoCurso(byte estado) {
        switch (estado) {
            case Curso.ATIVO_INSCRICOES:
                return "";
            case Curso.ATIVO_SEM_INSCRICOES:
                return "(INSCRIÇÕES ENCERRADAS)";
            case Curso.CONCLUIDO:
                return "(CONCLUÍDO)";
            case Curso.CANCELADO:
                return "(CANCELADO)";
            default:
                return "";
        }
    }

    /**
     * Mostra detalhes de um curso para inscrição
     */
    public void mostrarDetalheCursoParaInscricao(Curso curso, String nomeAutor) {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println("========================================");
        System.out.println(" > Início > Minhas inscrições > Lista de cursos > " + curso.getNome());
        System.out.println("");
        System.out.println(" CÓDIGO......: " + curso.getCodigoCompartilhavel());
        System.out.println(" CURSO.......: " + curso.getNome());
        System.out.println(" AUTOR.......: " + nomeAutor);
        System.out.println(" DESCRIÇÃO...: " + curso.getDescricao());
        System.out.println(" DATA INÍCIO.: " + curso.getDataInicio().format(FORMATO_DATA));
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println(" (A) Fazer minha inscrição no curso");
        System.out.println(" (R) Retornar ao menu anterior");
        System.out.print("Opção: ");
    }

    /**
     * Mostra as inscrições do usuário
     */
    public void mostrarMinhasInscricoes(Curso[] cursos) {
        if (cursos == null || cursos.length == 0) {
            System.out.println("\n       EntrePares 1.0                  ");
            System.out.println("========================================");
            System.out.println(" > Início > Minhas inscrições        ");
            System.out.println("\n Você não tem inscrições.");
            System.out.println("\n (R) Retornar");
            System.out.print("Opção: ");
            return;
        }

        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println("========================================");
        System.out.println(" > Início > Minhas inscrições        ");
        System.out.println("");

        for (int i = 0; i < Math.min(cursos.length, 10); i++) {
            String estado = obterEstadoCurso(cursos[i].getEstado());
            System.out.println("(" + (i + 1) + ") " + cursos[i].getNome() + " - " + cursos[i].getDataInicio().format(FORMATO_DATA) + " " + estado);
        }

        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println(" (R) Retornar");
        System.out.print("Opção: ");
    }

    /**
     * Mostra detalhes da inscrição do usuário
     */
    public void mostrarDetalheInscricao(Curso curso, String nomeAutor) {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println("========================================");
        System.out.println(" > Início > Minhas inscrições > " + curso.getNome());
        System.out.println("");
        System.out.println(" CÓDIGO......: " + curso.getCodigoCompartilhavel());
        System.out.println(" CURSO.......: " + curso.getNome());
        System.out.println(" AUTOR.......: " + nomeAutor);
        System.out.println(" DESCRIÇÃO...: " + curso.getDescricao());
        System.out.println(" DATA INÍCIO.: " + curso.getDataInicio().format(FORMATO_DATA));
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println(" (A) Cancelar minha inscrição no curso");
        System.out.println(" (R) Retornar ao menu anterior");
        System.out.print("Opção: ");
    }

    /**
     * Pede o código de um curso para busca
     */
    public String pegarCodigoCurso() {
        System.out.print("Digite o código do curso: ");
        return scanner.nextLine().trim();
    }

    /**
     * Pega opção do usuário
     */
    public char pegarOpcao() {
        String entrada = scanner.nextLine().toUpperCase().trim();
        if (entrada.length() > 0) {
            return entrada.charAt(0);
        }
        return ' ';
    }

    /**
     * Pega número (para seleção de item)
     */
    public int pegarNumero() {
        try {
            String entrada = scanner.nextLine().trim();
            return Integer.parseInt(entrada);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retorna a página atual
     */
    public int getPaginaAtual() {
        return paginaAtual;
    }

    /**
     * Avança para a próxima página
     */
    public void proximaPagina() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
        }
    }

    /**
     * Volta para a página anterior
     */
    public void paginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
        }
    }

    /**
     * Reseta o índice de página
     */
    public void resetarPagina() {
        paginaAtual = 1;
    }

    // Mensagens de sucesso/erro

    public void mostrarMensagemInscricaoRealizada() {
        System.out.println("\n ✓ Inscrição realizada com sucesso!");
    }

    public void mostrarMensagemJaInscrito() {
        System.out.println("\n ✗ Você já está inscrito neste curso.");
    }

    public void mostrarMensagemInscricoesEncerradas() {
        System.out.println("\n ✗ As inscrições para este curso foram encerradas.");
    }

    public void mostrarMensagemCursoNaoEncontrado() {
        System.out.println("\n ✗ Curso não encontrado.");
    }

    public void mostrarMensagemCancelamentoRealizado() {
        System.out.println("\n ✓ Inscrição cancelada com sucesso!");
    }

    public void mostrarMensagemErro(String mensagem) {
        System.out.println("\n ✗ Erro: " + mensagem);
    }

    public void mostrarMensagemOpcaoInvalida() {
        System.out.println("\n ✗ Opção inválida!");
    }

    public void mostrarMensagemFuncionalidadeEmDesenvolvimento() {
        System.out.println("\n Funcionalidade em desenvolvimento (TP3).");
    }
}

