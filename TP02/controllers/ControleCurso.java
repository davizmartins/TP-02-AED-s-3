package TP02.controllers;

import TP02.models.*;
import TP02.storage.*;
import TP02.views.*;
import java.util.*;
import java.time.LocalDate;

import java.util.ArrayList;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;

public class ControleCurso {
    private VisaoCurso visaoCurso;
    private ArquivoCurso arquivoCurso;
    private VisaoInscritos visaoInscritos;
    private ControleInscricao controleInscricao;

    public ControleCurso(VisaoCurso visaoCurso, ArquivoCurso arquivoCurso) {
        this.visaoCurso = visaoCurso;
        this.arquivoCurso = arquivoCurso;
        this.visaoInscritos = new VisaoInscritos();
    }

    public void setControleInscricao(ControleInscricao controleInscricao) {
        this.controleInscricao = controleInscricao;
    }

    public ArquivoCurso getArquivoCurso() {
        return arquivoCurso;
    }

    // Exibe o menu de cursos do usuário ativo
    public void mostrarMenu(Usuario usuarioAtivo) {
        while (true) {
            try {
                ArrayList<Curso> listaCursos = arquivoCurso.readByUser(usuarioAtivo.getId());

                Curso[] cursos = listaCursos.toArray(new Curso[0]);
                cursos = ordenarCursosAlfabeticamente(cursos);

                visaoCurso.mostrarCursos(cursos);
                char opcao = visaoCurso.pegarOpcao();

                switch (opcao) {
                    case 'A': // Novo curso
                        criarNovoCurso(usuarioAtivo);
                        break;
                    case 'R': // Retornar
                        return;
                    default:

                        if (opcao >= '0' && opcao <= '9') {
                            int indiceCurso = Character.getNumericValue(opcao) - 1;
                            if (indiceCurso >= 0 && indiceCurso < cursos.length) {
                                selecionarCurso(usuarioAtivo, cursos[indiceCurso]);
                            } else {
                                visaoCurso.mostrarMensagemOpcaoInvalida();
                            }
                        } else {
                            visaoCurso.mostrarMensagemOpcaoInvalida();
                        }
                }
            } catch (Exception e) {
                visaoCurso.mostrarMensagemErro("Erro ao acessar os cursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Curso[] ordenarCursosAlfabeticamente(Curso[] cursos) {
        // Bubble Sort simples - ordena do A ao Z
        for (int i = 0; i < cursos.length - 1; i++) {
            for (int j = 0; j < cursos.length - i - 1; j++) {
                // Se o nome do curso[j] vem DEPOIS do curso[j+1] na ordem alfabética
                if (cursos[j].getNome().compareToIgnoreCase(cursos[j + 1].getNome()) > 0) {
                    // Troca os dois cursos de lugar
                    Curso temp = cursos[j];
                    cursos[j] = cursos[j + 1];
                    cursos[j + 1] = temp;
                }
            }
        }
        return cursos;
    }

    // Criar novo curso
    private void criarNovoCurso(Usuario usuarioAtivo) {
        try {
            Curso novoCurso = visaoCurso.pegarDadosNovoCurso();
            novoCurso.setIdUsuario(usuarioAtivo.getId());
            arquivoCurso.create(novoCurso);
            visaoCurso.mostrarMensagemSucessoCadastro();
        } catch (Exception e) {
            visaoCurso.mostrarMensagemErro("Erro ao criar curso: " + e.getMessage());
        }
    }

    // Selecionar um curso específico
    private void selecionarCurso(Usuario usuarioAtivo, Curso curso) {
        while (true) {
            visaoCurso.mostrarDetalheCurso(curso);
            char opcao = visaoCurso.pegarOpcao();

            switch (opcao) {
                case 'A': // Gerenciar inscritos
                    gerenciarInscritos(curso);
                    break;
                case 'B': // Corrigir dados
                    atualizarCurso(curso);
                    break;
                case 'C': // Alterar data do curso
                    alterarDataCurso(curso);
                    break;
                case 'D': // Encerrar inscrições
                    alterarEstadoCurso(curso, Curso.ATIVO_SEM_INSCRICOES);
                    break;
                case 'E': // Concluir curso
                    alterarEstadoCurso(curso, Curso.CONCLUIDO);
                    break;
                case 'F': // Cancelar curso
                    alterarEstadoCurso(curso, Curso.CANCELADO);
                    break;
                case 'R': // Retornar
                    return;
                default:
                    visaoCurso.mostrarMensagemOpcaoInvalida();
            }
        }
    }

    // Atualizar dados do curso
    private void atualizarCurso(Curso curso) {
        try {
            Curso cursoAtualizado = visaoCurso.pegarDadosAtualizacaoCurso(curso);
            if (arquivoCurso.update(cursoAtualizado)) {
                visaoCurso.mostrarMensagemSucesso("Curso atualizado com sucesso!");
            } else {
                visaoCurso.mostrarMensagemErro("Erro ao atualizar curso.");
            }
        } catch (Exception e) {
            visaoCurso.mostrarMensagemErro("Erro ao atualizar curso: " + e.getMessage());
        }
    }

    // Alterar data do curso
    private void alterarDataCurso(Curso curso) {
        try {
            java.time.LocalDate novaData = visaoCurso.pegarNovaDataCurso(curso.getDataInicio());
            curso.setDataInicio(novaData);
            if (arquivoCurso.update(curso)) {
                visaoCurso.mostrarMensagemSucesso("Data do curso alterada com sucesso!");
            } else {
                visaoCurso.mostrarMensagemErro("Erro ao alterar data do curso.");
            }
        } catch (Exception e) {
            visaoCurso.mostrarMensagemErro("Erro ao alterar data: " + e.getMessage());
        }
    }

    // Alterar estado do curso
    private void alterarEstadoCurso(Curso curso, byte novoEstado) {
        try {
            curso.setEstado(novoEstado);
            if (arquivoCurso.update(curso)) {
                visaoCurso.mostrarMensagemSucesso("Estado do curso alterado!");
            }
        } catch (Exception e) {
            visaoCurso.mostrarMensagemErro("Erro ao alterar estado: " + e.getMessage());
        }
    }

    // Gerenciar inscritos em um curso
    private void gerenciarInscritos(Curso curso) {
        try {
            if (controleInscricao == null) {
                visaoCurso.mostrarMensagemErro("Sistema de inscrições não inicializado.");
                return;
            }

            while (true) {
                // Buscar todas as inscrições do curso (recarregar a cada iteração)
                ArrayList<CursoUsuario> inscricoes = controleInscricao.getArquivoInscricao().readByCurso(curso.getId());

                if (inscricoes == null || inscricoes.isEmpty()) {
                    visaoInscritos.mostrarListaInscritos(null, null, curso.getNome());
                    char opcao = visaoInscritos.pegarOpcao();
                    if (opcao == 'R' || opcao == 'A') {
                        return;
                    }
                    continue;
                }

                // Construir array de usuários a partir das inscrições
                ArrayList<Usuario> usuariosValidos = new ArrayList<>();
                ArrayList<CursoUsuario> inscricoesValidas = new ArrayList<>();

                for (int i = 0; i < inscricoes.size(); i++) {
                    Usuario usuario = controleInscricao.getArquivoInscricao().getArquivoUsuario()
                            .read(inscricoes.get(i).getIdUsuario());
                    if (usuario == null) {
                        controleInscricao.getArquivoInscricao().delete(inscricoes.get(i).getId());
                    } else {
                        usuariosValidos.add(usuario);
                        inscricoesValidas.add(inscricoes.get(i));
                    }
                }

                Usuario[] usuarios = usuariosValidos.toArray(new Usuario[0]);
                CursoUsuario[] inscricoesSalvas = inscricoesValidas.toArray(new CursoUsuario[0]);

                if (usuarios.length == 0) {
                    visaoInscritos.mostrarListaInscritos(null, null, curso.getNome());
                    char opcao = visaoInscritos.pegarOpcao();
                    if (opcao == 'R' || opcao == 'A') {
                        return;
                    }
                    continue;
                }

                visaoInscritos.mostrarListaInscritos(usuarios, inscricoesSalvas, curso.getNome());
                char opcao = visaoInscritos.pegarOpcao();

                if (opcao == 'A') {
                    exportarListaInscritos(usuarios, curso.getNome());
                } else if (opcao == 'R') {
                    return;
                } else if (opcao >= '1' && opcao <= '9') {
                    int numero = Character.getNumericValue(opcao) - 1;
                    if (numero >= 0 && numero < usuarios.length) {
                        verDetalhesInscrito(usuarios[numero], inscricoesSalvas[numero], curso);
                    } else {
                        visaoInscritos.mostrarMensagemOpcaoInvalida();
                    }
                } else {
                    visaoInscritos.mostrarMensagemOpcaoInvalida();
                }
            }
        } catch (Exception e) {
            visaoCurso.mostrarMensagemErro("Erro ao gerenciar inscritos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Ver detalhes de um inscrito
    private void verDetalhesInscrito(Usuario usuario, CursoUsuario inscricao, Curso curso) {
        try {
            visaoInscritos.mostrarDetalheInscrito(usuario, inscricao.getDataInscricao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            char opcao = visaoInscritos.pegarOpcao();

            if (opcao == 'A') {
                // Cancelar inscrição
                controleInscricao.getArquivoInscricao().delete(inscricao.getId());
                visaoInscritos.mostrarMensagemInscricaoCancelada();
            }
        } catch (Exception e) {
            visaoInscritos.mostrarMensagemErro("Erro ao processar inscrição: " + e.getMessage());
        }
    }

    // Exportar lista de inscritos em CSV
    private void exportarListaInscritos(Usuario[] usuarios, String nomeCurso) {
        try {
            FileWriter writer = new FileWriter("TP02\\inscritos.csv");
            writer.write("Nome,Email\n");

            for (Usuario u : usuarios) {
                if (u != null) {
                    writer.write(u.getNome() + "," + u.getEmail() + "\n");
                }
            }

            writer.close();
            visaoInscritos.mostrarMensagemExportacaoRealizada();
        } catch (Exception e) {
            visaoInscritos.mostrarMensagemErro("Erro ao exportar lista: " + e.getMessage());
        }
    }

    // Excluir um curso
    public void excluirCurso(int idCurso) {
        try {
            if (arquivoCurso.delete(idCurso)) {
                visaoCurso.mostrarMensagemSucesso("Curso excluído com sucesso.");
            } else {
                visaoCurso.mostrarMensagemErro("Erro ao excluir o curso.");
            }
        } catch (Exception e) {
            visaoCurso.mostrarMensagemErro("Erro ao excluir curso: " + e.getMessage());
        }
    }
}

