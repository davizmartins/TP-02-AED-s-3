package TP02.views;

import TP02.models.*;
import java.util.Scanner;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.Scanner;

public class VisaoInscritos {
    private Scanner scanner = new Scanner(System.in);

    /**
     * Mostra a lista de inscritos em um curso
     */
    public void mostrarListaInscritos(Usuario[] inscritos, String nomeCurso) {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println(" > Início > Meus Cursos > " + nomeCurso + " > Inscrições");
        System.out.println("");

        if (inscritos == null || inscritos.length == 0) {
            System.out.println(" Nenhum usuário inscrito neste curso.");
        } else {
            for (int i = 0; i < inscritos.length; i++) {
                System.out.println("(" + (i + 1) + ") " + inscritos[i].getNome() + " (" + inscritos[i].getEmail() + ")");
            }
        }

        System.out.println("");
        System.out.println(" (A) Exportar lista");
        System.out.println(" (R) Retornar");
        System.out.print("Opção: ");
    }

    /**
     * Mostra detalhes de um inscrito
     */
    public void mostrarDetalheInscrito(Usuario usuario, String dataInscricao) {
        System.out.println("\n       EntrePares 1.0                  ");
        System.out.println(" > Início > Meus Cursos > Inscrições > " + usuario.getNome());
        System.out.println("");
        System.out.println(" NOME.....: " + usuario.getNome());
        System.out.println(" E-MAIL...: " + usuario.getEmail());
        System.out.println(" INSCRITO.: " + dataInscricao);
        System.out.println("");
        System.out.println(" (A) Cancelar inscrição deste usuário");
        System.out.println(" (R) Retornar");
        System.out.print("Opção: ");
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
     * Pega número (para seleção de inscrito)
     */
    public int pegarNumero() {
        try {
            String entrada = scanner.nextLine().trim();
            return Integer.parseInt(entrada);
        } catch (Exception e) {
            return -1;
        }
    }

    // Mensagens

    public void mostrarMensagemExportacaoRealizada() {
        System.out.println("\n ✓ Lista exportada com sucesso em 'inscritos.csv'!");
    }

    public void mostrarMensagemInscricaoCancelada() {
        System.out.println("\n ✓ Inscrição cancelada com sucesso!");
    }

    public void mostrarMensagemErro(String mensagem) {
        System.out.println("\n ✗ Erro: " + mensagem);
    }

    public void mostrarMensagemOpcaoInvalida() {
        System.out.println("\n ✗ Opção inválida!");
    }
}

