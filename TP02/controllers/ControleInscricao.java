package TP02.controllers;

import TP02.models.*;
import TP02.storage.*;
import TP02.views.*;
import java.util.*;
import java.time.LocalDate;

import java.time.LocalDate;
import java.util.ArrayList;

public class ControleInscricao {
    private VisaoInscricao visaoInscricao;
    private ArquivoInscricao arquivoInscricao;
    private ArquivoCurso arquivoCurso;
    private ArquivoUsuario arquivoUsuario;

    public ControleInscricao(VisaoInscricao visaoInscricao, ArquivoInscricao arquivoInscricao,
            ArquivoCurso arquivoCurso, ArquivoUsuario arquivoUsuario) {
        this.visaoInscricao = visaoInscricao;
        this.arquivoInscricao = arquivoInscricao;
        this.arquivoCurso = arquivoCurso;
        this.arquivoUsuario = arquivoUsuario;
    }

    public ArquivoInscricao getArquivoInscricao() {
        return arquivoInscricao;
    }

    /**
     * Exibe o menu principal de inscrições
     */
    public void mostrarMenu(Usuario usuarioAtivo) {
        while (true) {
            try {
                ArrayList<CursoUsuario> inscricoes = arquivoInscricao.readByUsuario(usuarioAtivo.getId());
                Curso[] cursosInscritos = cursosDasInscricoes(inscricoes);

                visaoInscricao.mostrarTelaMinhasInscricoes(cursosInscritos);
                char opcao = visaoInscricao.pegarOpcao();

                if (opcao >= '1' && opcao <= '9' && cursosInscritos.length > 0) {
                    int indice = Character.getNumericValue(opcao) - 1;
                    if (indice >= 0 && indice < cursosInscritos.length) {
                        mostrarDetalheInscricao(cursosInscritos[indice], usuarioAtivo);
                        continue;
                    }
                }

                switch (opcao) {
                    case 'A': // Buscar por código
                        buscarPorCodigo(usuarioAtivo);
                        break;
                    case 'B': // Buscar por palavras-chave (TP3)
                        visaoInscricao.mostrarMensagemFuncionalidadeEmDesenvolvimento();
                        break;
                    case 'C': // Listar todos
                        listarTodosCursos(usuarioAtivo);
                        break;
                    case 'R': // Retornar
                        return;
                    default:
                        visaoInscricao.mostrarMensagemOpcaoInvalida();
                }
            } catch (Exception e) {
                visaoInscricao.mostrarMensagemErro("Erro ao acessar inscrições: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Curso[] cursosDasInscricoes(ArrayList<CursoUsuario> inscricoes) throws Exception {
        if (inscricoes == null || inscricoes.isEmpty()) {
            return new Curso[0];
        }
        ArrayList<Curso> cursos = new ArrayList<>();
        for (CursoUsuario inscricao : inscricoes) {
            Curso curso = arquivoCurso.read(inscricao.getIdCurso());
            if (curso != null) {
                cursos.add(curso);
            }
        }
        return cursos.toArray(new Curso[0]);
    }

    /**
     * Busca um curso por código compartilhável
     */
    private void buscarPorCodigo(Usuario usuarioAtivo) {
        try {
            String codigo = visaoInscricao.pegarCodigoCurso();
            Curso curso = arquivoCurso.read(codigo);

            if (curso == null) {
                visaoInscricao.mostrarMensagemCursoNaoEncontrado();
                return;
            }

            // Não permitir inscrição em seus próprios cursos
            if (curso.getIdUsuario() == usuarioAtivo.getId()) {
                visaoInscricao.mostrarMensagemErro("Você não pode se inscrever em seus próprios cursos.");
                return;
            }

            mostrarDetalheCursoParaInscricao(curso, usuarioAtivo);
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro ao buscar curso: " + e.getMessage());
        }
    }

    /**
     * Lista todos os cursos com paginação
     */
    private void listarTodosCursos(Usuario usuarioAtivo) {
        try {
            // Ler todos os cursos do arquivo diretamente
            ArrayList<Curso> todosCursos = new ArrayList<>();

            try {
                // Buscar todos os cursos iterando pelo ID
                for (int i = 1; i <= 1000; i++) {
                    try {
                        Curso c = arquivoCurso.read(i);
                        if (c != null && c.getIdUsuario() != usuarioAtivo.getId()) {
                            todosCursos.add(c);
                        }
                    } catch (Exception e) {
                        // Curso não encontrado, continua
                    }
                }
            } catch (Exception e) {
                // Se não conseguir ler, pula
            }

            // Ordenar por data de início
            Curso[] cursos = todosCursos.toArray(new Curso[0]);
            cursos = ordenarPorData(cursos);

            visaoInscricao.resetarPagina();
            visaoInscricao.mostrarListaCursos(cursos, "Lista de cursos");

            navegarListaCursos(cursos, usuarioAtivo);
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro ao listar cursos: " + e.getMessage());
        }
    }

    /**
     * Mostra detalhes de um curso para inscrição
     */
    private void mostrarDetalheCursoParaInscricao(Curso curso, Usuario usuarioAtivo) {
        try {
            // Buscar nome do autor do curso
            String nomeAutor = "Desconhecido";
            try {
                Usuario autor = arquivoUsuario.read(curso.getIdUsuario());
                if (autor != null) {
                    nomeAutor = autor.getNome();
                }
            } catch (Exception e) {
                // Se não conseguir encontrar o autor, usa "Desconhecido"
            }

            while (true) {
                visaoInscricao.mostrarDetalheCursoParaInscricao(curso, nomeAutor);
                char opcao = visaoInscricao.pegarOpcao();

                switch (opcao) {
                    case 'A': // Fazer inscrição
                        fazerInscricao(curso, usuarioAtivo);
                        return;
                    case 'R': // Retornar
                        return;
                    default:
                        visaoInscricao.mostrarMensagemOpcaoInvalida();
                }
            }
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro: " + e.getMessage());
        }
    }

    /**
     * Realiza a inscrição de um usuário em um curso
     */
    private void fazerInscricao(Curso curso, Usuario usuarioAtivo) {
        try {
            // Verificar se as inscrições estão abertas
            if (curso.getEstado() != Curso.ATIVO_INSCRICOES) {
                visaoInscricao.mostrarMensagemInscricoesEncerradas();
                return;
            }

            // Verificar se já está inscrito
            if (arquivoInscricao.jaInscrito(curso.getId(), usuarioAtivo.getId())) {
                visaoInscricao.mostrarMensagemJaInscrito();
                return;
            }

            // Criar a inscrição
            CursoUsuario inscricao = new CursoUsuario(curso.getId(), usuarioAtivo.getId(), LocalDate.now());
            arquivoInscricao.create(inscricao);

            visaoInscricao.mostrarMensagemInscricaoRealizada();
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro ao realizar inscrição: " + e.getMessage());
        }
    }

    /**
     * Navega pela lista de cursos (paginação)
     */
    private void navegarListaCursos(Curso[] cursos, Usuario usuarioAtivo) {
        while (true) {
            try {
                char opcao = visaoInscricao.pegarOpcao();

                switch (opcao) {
                    case 'A': // Página anterior
                        visaoInscricao.paginaAnterior();
                        visaoInscricao.mostrarListaCursos(cursos, "Lista de cursos");
                        break;
                    case 'B': // Próxima página
                        visaoInscricao.proximaPagina();
                        visaoInscricao.mostrarListaCursos(cursos, "Lista de cursos");
                        break;
                    case 'R': // Retornar
                        return;
                    default:
                        // Tentar como número de item
                        if (opcao >= '0' && opcao <= '9') {
                            int numero = Character.getNumericValue(opcao);
                            selecionarCursoDaLista(cursos, numero, usuarioAtivo);
                            return;
                        } else {
                            visaoInscricao.mostrarMensagemOpcaoInvalida();
                        }
                }
            } catch (Exception e) {
                visaoInscricao.mostrarMensagemErro("Erro: " + e.getMessage());
            }
        }
    }

    /**
     * Seleciona um curso da lista para mais detalhes
     */
    private void selecionarCursoDaLista(Curso[] cursos, int numero, Usuario usuarioAtivo) {
        try {
            // Calcular o índice real considerando a página
            int indiceInicio = (visaoInscricao.getPaginaAtual() - 1) * 10;
            int indiceReal = indiceInicio + numero;

            if (indiceReal >= 0 && indiceReal < cursos.length) {
                mostrarDetalheCursoParaInscricao(cursos[indiceReal], usuarioAtivo);
            } else {
                visaoInscricao.mostrarMensagemOpcaoInvalida();
            }
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro: " + e.getMessage());
        }
    }

    /**
     * Mostra as inscrições do usuário
     */
    public void mostrarMinhasInscricoes(Usuario usuarioAtivo) {
        try {
            ArrayList<CursoUsuario> inscricoes = arquivoInscricao.readByUsuario(usuarioAtivo.getId());

            if (inscricoes == null || inscricoes.isEmpty()) {
                visaoInscricao.mostrarMinhasInscricoes(null);
                visaoInscricao.pegarOpcao();
                return;
            }

            // Construir array de cursos a partir das inscrições
            ArrayList<Curso> cursosList = new ArrayList<>();
            for (CursoUsuario inscricao : inscricoes) {
                Curso curso = arquivoCurso.read(inscricao.getIdCurso());
                if (curso != null) {
                    cursosList.add(curso);
                }
            }

            Curso[] cursos = cursosList.toArray(new Curso[0]);
            cursos = ordenarPorData(cursos);

            visaoInscricao.mostrarMinhasInscricoes(cursos);

            // Menu de seleção de inscrições
            while (true) {
                char opcao = visaoInscricao.pegarOpcao();

                if (opcao == 'R') {
                    return;
                } else if (opcao >= '1' && opcao <= '9') {
                    int numero = Character.getNumericValue(opcao) - 1;
                    if (numero >= 0 && numero < cursos.length) {
                        mostrarDetalheInscricao(cursos[numero], usuarioAtivo);
                    } else {
                        visaoInscricao.mostrarMensagemOpcaoInvalida();
                    }
                } else {
                    visaoInscricao.mostrarMensagemOpcaoInvalida();
                }
            }
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro ao carregar inscrições: " + e.getMessage());
        }
    }

    /**
     * Mostra detalhes de uma inscrição do usuário
     */
    private void mostrarDetalheInscricao(Curso curso, Usuario usuarioAtivo) {
        try {
            // Buscar nome do autor do curso
            String nomeAutor = "Desconhecido";
            try {
                Usuario autor = arquivoUsuario.read(curso.getIdUsuario());
                if (autor != null) {
                    nomeAutor = autor.getNome();
                }
            } catch (Exception e) {
                // Se não conseguir encontrar o autor, usa "Desconhecido"
            }

            while (true) {
                visaoInscricao.mostrarDetalheInscricao(curso, nomeAutor);
                char opcao = visaoInscricao.pegarOpcao();

                switch (opcao) {
                    case 'A': // Cancelar inscrição
                        cancelarInscricao(curso, usuarioAtivo);
                        return;
                    case 'R': // Retornar
                        return;
                    default:
                        visaoInscricao.mostrarMensagemOpcaoInvalida();
                }
            }
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro: " + e.getMessage());
        }
    }

    /**
     * Cancela uma inscrição
     */
    private void cancelarInscricao(Curso curso, Usuario usuarioAtivo) {
        try {
            ArrayList<CursoUsuario> inscricoes = arquivoInscricao.readByCurso(curso.getId());

            for (CursoUsuario inscricao : inscricoes) {
                if (inscricao.getIdUsuario() == usuarioAtivo.getId()) {
                    arquivoInscricao.delete(inscricao.getId());
                    visaoInscricao.mostrarMensagemCancelamentoRealizado();
                    return;
                }
            }

            visaoInscricao.mostrarMensagemErro("Inscrição não encontrada.");
        } catch (Exception e) {
            visaoInscricao.mostrarMensagemErro("Erro ao cancelar inscrição: " + e.getMessage());
        }
    }

    /**
     * Ordena cursos por data de início
     */
    private Curso[] ordenarPorData(Curso[] cursos) {
        for (int i = 0; i < cursos.length - 1; i++) {
            for (int j = 0; j < cursos.length - i - 1; j++) {
                if (cursos[j].getDataInicio().isAfter(cursos[j + 1].getDataInicio())) {
                    Curso temp = cursos[j];
                    cursos[j] = cursos[j + 1];
                    cursos[j + 1] = temp;
                }
            }
        }
        return cursos;
    }
}

