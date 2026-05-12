package TP02.controllers;

import TP02.models.*;
import TP02.storage.*;
import TP02.views.*;
import java.util.*;
import java.time.LocalDate;

import java.util.ArrayList;

public class ControleUsuario {
    private VisaoUsuario visaoUsuario;
    private ArquivoUsuario arquivoUsuario;
    private ControleCurso controleCurso;
    private ControleInscricao controleInscricao;

    public ControleUsuario(VisaoUsuario visaoUsuario, ArquivoUsuario arquivoUsuario, ControleCurso controleCurso, ControleInscricao controleInscricao) {
        this.visaoUsuario = visaoUsuario;
        this.arquivoUsuario = arquivoUsuario;
        this.controleCurso = controleCurso;
        this.controleInscricao = controleInscricao;
    }

    public void mostrarMenu() {
        while (true) {
            visaoUsuario.mostrarMenuPrincipal();
            char opcao = visaoUsuario.pegarOpcao();

            switch (opcao) {
                case 'A': // Login
                    Usuario usuarioLogado = realizarLogin();
                    if (usuarioLogado != null) {
                        mostrarMenuPrincipais(usuarioLogado);
                    }
                    break;
                case 'B': // Novo usuário
                    cadastrarNovoUsuario();
                    break;
                case 'C': // Recuperar senha
                    recuperarSenha();
                    break;
                case 'S': // Sair
                    visaoUsuario.mostrarMensagemSaida();
                    System.exit(0);
                    break;
                default:
                    visaoUsuario.mostrarMensagemOpcaoInvalida();
            }
        }
    }

    // Realizar login de usuário
    private Usuario realizarLogin() {
        String[] dadosLogin = visaoUsuario.pegarDadosLogin();
        String email = dadosLogin[0];
        String senha = dadosLogin[1];

        try {
            Usuario usuario = arquivoUsuario.read(email);
            if (usuario != null && usuario.getHashSenha().equals(Integer.toString(senha.hashCode()))) {
                visaoUsuario.mostrarMensagemLoginSucesso(usuario.getNome());
                return usuario;
            } else {
                visaoUsuario.mostrarMensagemErroLogin();
                return null;
            }
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErroLogin();
            return null;
        }
    }

    // Cadastrar novo usuário
    private void cadastrarNovoUsuario() {
        Usuario novoUsuario = visaoUsuario.pegarDadosNovoUsuario();
        try {
            arquivoUsuario.create(novoUsuario);
            visaoUsuario.mostrarMensagemSucessoCadastro();
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErroCadastro();
        }
    }

    private boolean excluirUsuario(Usuario usuario) {
        try {
            ArrayList<CursoUsuario> inscricoes = controleInscricao.getArquivoInscricao().readByUsuario(usuario.getId());
            if (inscricoes != null && !inscricoes.isEmpty()) {
                visaoUsuario.mostrarMensagemErro("Não é possível excluir a conta. Você possui inscrições em cursos.");
                return false;
            }

            ArrayList<Curso> cursos = controleCurso.getArquivoCurso().readByUser(usuario.getId());

            boolean temCursosAtivos = false;
            for (Curso c : cursos) {
                if (c.getEstado() == Curso.ATIVO_INSCRICOES || c.getEstado() == Curso.ATIVO_SEM_INSCRICOES) {
                    temCursosAtivos = true;
                    break;
                }
            }

            if (temCursosAtivos) {
                visaoUsuario.mostrarMensagemErro("Não é possível excluir a conta. Você possui cursos ativos.");
                return false;
            }

            for (Curso c : cursos) {
                if (c.getEstado() == Curso.CONCLUIDO || c.getEstado() == Curso.CANCELADO) {
                    controleCurso.getArquivoCurso().delete(c.getId());
                }
            }

            if (arquivoUsuario.delete(usuario.getId())) {
                visaoUsuario.mostrarMensagemSucesso("Conta excluída com sucesso!");
                return true;
            } else {
                visaoUsuario.mostrarMensagemErro("Erro ao excluir conta. Tente novamente.");
                return false;
            }
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErro("Erro ao excluir conta: " + e.getMessage());
            return false;
        }
    }

    // Menu principal após login
    private void mostrarMenuPrincipais(Usuario usuarioAtivo) {
        while (true) {
            visaoUsuario.mostrarMenuPrincipal(usuarioAtivo);
            char opcao = visaoUsuario.pegarOpcao();

            switch (opcao) {
                case 'A': // Meus dados
                    boolean voltarAoLogin = mostrarMenuMeusDados(usuarioAtivo);
                    if (voltarAoLogin) {
                        return; // Conta foi excluida, volta para o menu de login
                    }
                    break;
                case 'B': // Meus cursos
                    controleCurso.mostrarMenu(usuarioAtivo);
                    break;
                case 'C': // Minhas inscrições (TP2)
                    controleInscricao.mostrarMenu(usuarioAtivo);
                    break;
                case 'S': // Logout
                    visaoUsuario.mostrarMensagemLogout();
                    return;
                default:
                    visaoUsuario.mostrarMensagemOpcaoInvalida();
            }
        }
    }

    // Menu Meus Dados
    private boolean mostrarMenuMeusDados(Usuario usuarioAtivo) {
        while (true) {
            visaoUsuario.mostrarMenuMeusDados(usuarioAtivo);
            char opcao = visaoUsuario.pegarOpcao();

            switch (opcao) {
                case 'A': // Editar nome
                    editarNome(usuarioAtivo);
                    break;
                case 'B': // Editar e-mail
                    editarEmail(usuarioAtivo);
                    break;
                case 'C': // Alterar senha
                    alterarSenha(usuarioAtivo);
                    break;
                case 'D': // Excluir conta
                    boolean deveSair = excluirUsuario(usuarioAtivo);
                    if (deveSair) {
                        return true; // Conta excluída, volta para login
                    }
                    break;
                case 'R': // Retornar
                    return false; // Volta para menu principal
                default:
                    visaoUsuario.mostrarMensagemOpcaoInvalida();
            }
        }
    }

    // Editar nome do usuário
    private void editarNome(Usuario usuarioAtivo) {
        try {
            String novoNome = visaoUsuario.pegarNovoNome(usuarioAtivo.getNome());
            if (novoNome.isEmpty()) {
                visaoUsuario.mostrarMensagemErro("Nome não pode estar vazio!");
                return;
            }
            usuarioAtivo.setNome(novoNome);
            arquivoUsuario.update(usuarioAtivo);
            visaoUsuario.mostrarMensagemSucesso("Nome atualizado com sucesso!");
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErro("Erro ao atualizar nome: " + e.getMessage());
        }
    }

    // Editar e-mail do usuário
    private void editarEmail(Usuario usuarioAtivo) {
        try {
            String novoEmail = visaoUsuario.pegarNovoEmail(usuarioAtivo.getEmail());
            if (novoEmail.isEmpty() || !novoEmail.contains("@")) {
                visaoUsuario.mostrarMensagemErro("E-mail inválido!");
                return;
            }
            usuarioAtivo.setEmail(novoEmail);
            arquivoUsuario.update(usuarioAtivo);
            visaoUsuario.mostrarMensagemSucesso("E-mail atualizado com sucesso!");
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErro("Erro ao atualizar e-mail: " + e.getMessage());
        }
    }

    // Alterar senha do usuário
    private void alterarSenha(Usuario usuarioAtivo) {
        try {
            String[] dados = visaoUsuario.pegarDadosAlteracaoSenha();
            String senhaAtual = dados[0];
            String novaSenha = dados[1];
            String confirmaSenha = dados[2];

            if (!usuarioAtivo.getHashSenha().equals(Integer.toString(senhaAtual.hashCode()))) {
                visaoUsuario.mostrarMensagemErro("Senha atual incorreta!");
                return;
            }

            if (!novaSenha.equals(confirmaSenha)) {
                visaoUsuario.mostrarMensagemErro("As novas senhas não coincidem!");
                return;
            }

            if (novaSenha.isEmpty()) {
                visaoUsuario.mostrarMensagemErro("Nova senha não pode estar vazia!");
                return;
            }

            usuarioAtivo.setHashSenha(Integer.toString(novaSenha.hashCode()));
            arquivoUsuario.update(usuarioAtivo);
            visaoUsuario.mostrarMensagemSucesso("Senha alterada com sucesso!");
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErro("Erro ao alterar senha: " + e.getMessage());
        }
    }

    // Recuperar senha via pergunta secreta
    private void recuperarSenha() {
        try {
            String[] dadosRecuperacao = visaoUsuario.pegarDadosRecuperacaoSenha();
            String email = dadosRecuperacao[0];

            Usuario usuario = arquivoUsuario.read(email);
            if (usuario == null) {
                visaoUsuario.mostrarMensagemErro("Usuário não encontrado!");
                return;
            }

            String respostaUsuario = visaoUsuario.pegarRespostaSegurança(usuario.getPerguntaSecreta());
            if (usuario.getHashRespostaSecreta().equals(Integer.toString(respostaUsuario.hashCode()))) {
                System.out.println("\n");
                System.out.println("--- Redefinição de Senha ---");
                System.out.print("Nova senha: ");
                String novaSenha = new java.util.Scanner(System.in).nextLine();
                
                usuario.setHashSenha(Integer.toString(novaSenha.hashCode()));
                arquivoUsuario.update(usuario);
                visaoUsuario.mostrarMensagemSucesso("Senha alterada com sucesso!");
            } else {
                visaoUsuario.mostrarMensagemErro("Resposta incorreta!");
            }
        } catch (Exception e) {
            visaoUsuario.mostrarMensagemErro("Erro ao recuperar senha: " + e.getMessage());
        }
    }
}

