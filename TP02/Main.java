package TP02;

import TP02.models.*;
import TP02.storage.*;
import TP02.controllers.*;
import TP02.views.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Instanciar os componentes
            VisaoUsuario visaoUsuario = new VisaoUsuario();
            ArquivoUsuario arquivoUsuario = new ArquivoUsuario();
            
            VisaoCurso visaoCurso = new VisaoCurso();
            ArquivoCurso arquivoCurso = new ArquivoCurso();
            ControleCurso controleCurso = new ControleCurso(visaoCurso, arquivoCurso);
            
            // Componentes de Inscrição (TP02)
            VisaoInscricao visaoInscricao = new VisaoInscricao();
            ArquivoInscricao arquivoInscricao = new ArquivoInscricao();
            arquivoInscricao.setArquivoUsuario(arquivoUsuario); // Injetar dependência
            
            ControleInscricao controleInscricao = new ControleInscricao(visaoInscricao, arquivoInscricao, arquivoCurso, arquivoUsuario);
            controleCurso.setControleInscricao(controleInscricao); // Injetar dependência
            
            // Controle de usuários
            ControleUsuario controleUsuario = new ControleUsuario(visaoUsuario, arquivoUsuario, controleCurso, controleInscricao);

            // Iniciar o sistema
            controleUsuario.mostrarMenu();

        } catch (Exception e) {
            System.out.println("✗ Erro crítico ao inicializar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}