package TP02.utils;

import TP02.models.*;
import TP02.storage.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;


public class TesteUsuario {
  public static void main(String[] args) {

    HashExtensivel<ParEmailID> he;
    Scanner console = new Scanner(System.in);
    String nomeArquivo = "pessoas";

    try {
      File d = new File("TP02\\dados");
      if (!d.exists())
        d.mkdirs();
      he = new HashExtensivel<>(ParEmailID.class.getConstructor(), 4, "TP02/dados/" + nomeArquivo + ".hash_d.db",
          "TP02/dados/" + nomeArquivo + ".hash_c.db");

      int opcao;
      do {
        System.out.println("\n\n-------------------------------");
        System.out.println("              MENU");
        System.out.println("-------------------------------");
        System.out.println("1 - Inserir");
        System.out.println("2 - Buscar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Imprimir");
        System.out.println("0 - Sair");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {
            System.out.println("\nINCLUSÃO");

            System.out.print("E-mail: ");
            String email = console.nextLine();
            System.out.print("ID: ");
            int id = Integer.valueOf(console.nextLine());
            he.create(new ParEmailID(email, id));
            he.print();
          }
            break;
          case 2: {
            System.out.println("\nBUSCA");

            System.out.print("E-mail: ");
            String email = console.nextLine();
            System.out.print("Dados: " + he.read(ParEmailID.hash(email)));
          }
            break;
          case 3: {
            System.out.println("\nEXCLUSÃO");

            System.out.print("E-mail: ");
            String email = console.nextLine();
            he.delete(email.hashCode());
            he.print();
          }
            break;
          case 4: {
            he.print();
          }
            break;
          case 0:
            break;
          default:
            System.out.println("Opção inválida");
        }
      } while (opcao != 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
    console.close();
  }
}

