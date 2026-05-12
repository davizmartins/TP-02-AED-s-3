# Relatório do Trabalho Prático 02 - AEDS III

Link do Vídeo da Plataforma no YouTube: [TP02-AEDS 3](https://youtu.be/JTHIV_Fhdbs)

## Participantes

- Davi Rafael de Oliveira Gurgel Martins:
- Pedro Augusto Gomes de Araújo

## Descrição geral do sistema

O sistema desenvolvido é uma aplicação Java em modo console chamada `EntrePares 1.0`, voltada para o gerenciamento de usuários, cursos e inscrições em cursos. A proposta principal do trabalho é permitir que usuários cadastrem cursos, divulguem esses cursos por meio de um código compartilhável e que outros usuários possam localizar esses cursos e realizar inscrições.

O projeto segue uma organização em camadas, separando modelos, armazenamento, regras de controle e visões de tela. A classe `Main` inicializa os objetos principais do sistema, instancia as visões, os arquivos de armazenamento e os controladores, além de injetar as dependências entre cursos, usuários e inscrições.

O sistema permite:

- cadastrar, autenticar, editar, recuperar senha e excluir usuários;
- cadastrar cursos associados ao usuário logado;
- listar e gerenciar os cursos criados pelo usuário;
- alterar dados, data e estado de um curso;
- buscar cursos de outros usuários por código compartilhável;
- listar cursos disponíveis de outros usuários;
- realizar inscrições em cursos;
- listar as inscrições feitas pelo usuário;
- cancelar inscrições;
- gerenciar os usuários inscritos em um curso criado pelo usuário;
- exportar a lista de inscritos de um curso para arquivo CSV;
- manter índices em arquivos usando Hash Extensível e Árvore B+.

## Classes criadas

### Classe principal

- `TP02.Main`

### Models

- `TP02.models.Registro`
- `TP02.models.Usuario`
- `TP02.models.Curso`
- `TP02.models.CursoUsuario`

### Controllers

- `TP02.controllers.ControleUsuario`
- `TP02.controllers.ControleCurso`
- `TP02.controllers.ControleInscricao`

### Views

- `TP02.views.VisaoUsuario`
- `TP02.views.VisaoCurso`
- `TP02.views.VisaoInscricao`
- `TP02.views.VisaoInscritos`

### Storage e índices

- `TP02.storage.Arquivo`
- `TP02.storage.ArquivoUsuario`
- `TP02.storage.ArquivoCurso`
- `TP02.storage.ArquivoInscricao`
- `TP02.storage.HashExtensivel`
- `TP02.storage.ArvoreBMais`
- `TP02.storage.RegistroHashExtensivel`
- `TP02.storage.RegistroArvoreBMais`
- `TP02.storage.ParIDEndereco`
- `TP02.storage.ParEmailID`
- `TP02.storage.ParCodigoID`
- `TP02.storage.ParUsuarioCurso`
- `TP02.storage.ParCursoID`
- `TP02.storage.ParUsuarioID`

### Testes utilitários

- `TP02.utils.TesteUsuario`
- `TP02.utils.TesteCurso`

## Organização dos dados

Os dados são armazenados em arquivos binários no diretório `TP02/dados`. Cada entidade principal possui seu próprio arquivo de dados e arquivos auxiliares de índice.

Usuários:

- arquivo principal: `TP02/dados/usuarios/usuarios.db`;
- índice direto por ID: `usuarios.d.db` e `usuarios.c.db`;
- índice indireto por e-mail: `indiceEmail.d.db` e `indiceEmail.c.db`.

Cursos:

- arquivo principal: `TP02/dados/cursos/cursos.db`;
- índice direto por ID: `cursos.d.db` e `cursos.c.db`;
- índice indireto por código compartilhável: `indiceCodigo.d.db` e `indiceCodigo.c.db`;
- Árvore B+ para associar usuário e curso: `arvoreCursos.db`.

Inscrições:

- arquivo principal: `TP02/dados/inscricoes/inscricoes.db`;
- índice direto por ID: `inscricoes.d.db` e `inscricoes.c.db`;
- Árvore B+ por curso: `arvoreCurso.db`;
- Árvore B+ por usuário: `arvoreUsuario.db`.

## Funcionamento das principais entidades

### Usuário

A entidade `Usuario` representa uma pessoa cadastrada no sistema. Ela possui ID, nome, e-mail, hash da senha, pergunta secreta e hash da resposta secreta. O controle de usuários permite cadastro, login, edição de dados, alteração de senha, recuperação de senha e exclusão da conta.

O e-mail é indexado por Hash Extensível, permitindo localizar rapidamente um usuário pelo e-mail durante o login e a recuperação de senha.

### Curso

A entidade `Curso` representa um curso cadastrado por um usuário. Ela possui ID, nome, data de início, descrição, código compartilhável, estado e ID do usuário criador.

Os estados implementados são:

- `ATIVO_INSCRICOES`: curso ativo e recebendo inscrições;
- `ATIVO_SEM_INSCRICOES`: curso ativo, mas sem aceitar novas inscrições;
- `CONCLUIDO`: curso finalizado;
- `CANCELADO`: curso cancelado.

Cada curso recebe um código compartilhável gerado automaticamente. Esse código funciona como o NanoID exigido no trabalho, permitindo que outros usuários encontrem o curso diretamente.

### CursoUsuario

A entidade `CursoUsuario` representa a associação N:N entre usuários e cursos. Cada registro possui:

- ID próprio da inscrição;
- ID do curso;
- ID do usuário;
- data da inscrição.

Essa classe é armazenada pelo `ArquivoInscricao`, que implementa as operações de criação, leitura e exclusão das inscrições, além de consultas por curso e por usuário.

## Operações especiais implementadas

### Hash Extensível como índice direto

A classe genérica `Arquivo<T>` usa `HashExtensivel<ParIDEndereco>` como índice direto. Esse índice associa o ID de cada registro ao endereço físico do registro no arquivo binário. Isso evita a necessidade de percorrer todo o arquivo para localizar um registro por ID.

### Reaproveitamento de espaço com lápides

Os registros removidos são marcados com lápide (`*`) e seus espaços entram em uma lista de removidos. Ao criar ou atualizar registros, o sistema tenta reaproveitar esses espaços antes de gravar no final do arquivo.

### Índice por e-mail

A classe `ArquivoUsuario` usa `HashExtensivel<ParEmailID>` para associar o e-mail do usuário ao seu ID. Isso é usado principalmente no login, recuperação de senha e exclusão por e-mail.

### Índice por código compartilhável do curso

A classe `ArquivoCurso` usa `HashExtensivel<ParCodigoID>` para associar o código compartilhável do curso ao ID do curso. Essa estrutura permite a busca direta de cursos por código.

### Árvore B+ para cursos por usuário

A classe `ArquivoCurso` usa uma `ArvoreBMais<ParUsuarioCurso>` para relacionar o ID do usuário criador com os IDs dos cursos criados por ele. Isso permite listar os cursos pertencentes ao usuário logado.

### Duas Árvores B+ para inscrições

A classe `ArquivoInscricao` usa duas árvores B+:

- `ArvoreBMais<ParCursoID>`: permite encontrar as inscrições de um determinado curso;
- `ArvoreBMais<ParUsuarioID>`: permite encontrar as inscrições de um determinado usuário.

Essa solução atende ao relacionamento N:N, pois permite consultar a associação nos dois sentidos: cursos de um usuário e usuários inscritos em um curso.

### Integridade das inscrições

O sistema impede que um usuário se inscreva em um curso próprio. Também verifica se o curso está com inscrições abertas antes de permitir a inscrição. Além disso, verifica se o usuário já está inscrito no curso, evitando inscrições duplicadas.

### Gestão de inscritos pelo criador do curso

Na visão de cursos, o usuário criador pode abrir a lista de inscritos de um curso, visualizar os dados dos inscritos, cancelar uma inscrição e exportar a lista de inscritos para CSV.

### Exportação para CSV

A classe `ControleCurso` possui a operação de exportação da lista de inscritos para o arquivo `TP02/inscritos.csv`. O arquivo contém, pelo menos, nome e e-mail dos usuários inscritos.

### Ordenação e paginação

Na visão de inscrições, a listagem geral de cursos apresenta paginação de 10 itens por página. Os cursos são ordenados por data de início. Na visão de cursos do usuário, os cursos são ordenados alfabeticamente.

## Telas do sistema

As capturas abaixo devem ser inseridas no relatório final entregue ao professor. Elas foram escolhidas por mostrarem as funcionalidades principais do TP02.

### Figura 1 - Tela de minhas inscrições

![](img/minhasInscricoes.png)

### Figura 2 - Lista de cursos disponíveis

![](img/cursosDisponiveis.png)

### Figura 3 - Detalhes de curso para inscrição

![](img/cursoDetalhes.png)

### Figura 4 - Meus cursos

![](img/meusCursos.png)

### Figura 5 - Gerenciamento de inscritos

![](img/gerenciamentoInscritos.png)

## Checklist obrigatório

**Há um CRUD da entidade de associação CursoUsuario (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?**

Sim, com ressalva de nomenclatura. A entidade de associação foi implementada como `CursoUsuario`, e seu armazenamento foi implementado em `ArquivoInscricao`, que estende a classe genérica `Arquivo<CursoUsuario>`. No projeto, a classe base se chama `Arquivo`, não `ArquivoIndexado`. Ela possui índice direto com Hash Extensível por ID e o `ArquivoInscricao` acrescenta duas Árvores B+ para consultas por curso e por usuário. As operações implementadas incluem criar inscrição, ler por ID, listar por curso, listar por usuário, listar todas, verificar duplicidade e excluir inscrição.

**A visão de inscrições está corretamente implementada e permite consultas aos cursos em que um usuário está inscrito?**

Sim. A classe `VisaoInscricao`, junto com `ControleInscricao`, mostra as inscrições do usuário logado, permite consultar detalhes de um curso inscrito e permite cancelar a inscrição. As consultas são feitas a partir da Árvore B+ por usuário em `ArquivoInscricao.readByUsuario`.

**A visão de cursos funciona corretamente e permite a gestão dos usuários inscritos em um curso?**

Sim. A classe `VisaoCurso`, junto com `ControleCurso` e `VisaoInscritos`, permite ao criador do curso visualizar seus cursos, selecionar um curso, gerenciar inscritos, ver detalhes de inscritos, cancelar inscrições e exportar a lista de inscritos em CSV.

**Há uma visualização dos cursos de outras pessoas por meio de um código NanoID?**

Sim. O sistema gera um código compartilhável para cada curso na classe `Curso`. Esse código é usado como identificador semelhante ao NanoID. A busca por código é implementada em `ControleInscricao.buscarPorCodigo`, usando o método `ArquivoCurso.read(String codigoCompartilhavel)` e o índice `HashExtensivel<ParCodigoID>`.

**A integridade do relacionamento entre cursos e usuários está mantida em todas as operações?**

Sim, com observações. O sistema mantém a integridade principal do relacionamento N:N: não permite inscrição em curso próprio, não permite inscrição duplicada, só permite inscrição quando o curso está aberto, remove os índices das árvores ao cancelar uma inscrição e impede excluir usuário que possui inscrições ou cursos ativos. Também remove cursos concluídos ou cancelados ao excluir conta. Uma observação é que algumas consultas filtram resultados após ler todos os pares da Árvore B+, em vez de buscar diretamente pela chave específica, o que funciona, mas não é a forma mais eficiente.

**O trabalho compila corretamente?**

Sim. O projeto foi compilado com o comando:

```bash
javac TP02\Main.java TP02\models\*.java TP02\storage\*.java TP02\controllers\*.java TP02\views\*.java TP02\utils\*.java
```

A compilação terminou sem erros.

**O trabalho está completo e funcionando sem erros de execução?**

Sim, considerando as funcionalidades principais exigidas para o TP02. O sistema possui cadastro/login de usuários, criação e gerenciamento de cursos, busca por código, listagem de cursos, inscrições, cancelamento, gestão de inscritos e exportação CSV. Há uma opção de busca por palavras-chave marcada como funcionalidade em desenvolvimento, o que está de acordo com a especificação, pois essa busca ficou prevista para o TP03.

**O trabalho é original e não a cópia de um trabalho de outro grupo?**

Sim. O trabalho foi desenvolvido pelo grupo, com classes próprias para modelos, controladores, visões e armazenamento, utilizando as estruturas de dados exigidas na disciplina.

## Conclusão

O trabalho implementa o relacionamento N:N entre usuários e cursos por meio da entidade `CursoUsuario`, armazenada em arquivo próprio e indexada por Hash Extensível e Árvores B+. O sistema permite que usuários criem cursos, que outros usuários encontrem esses cursos por código compartilhável ou listagem, realizem inscrições e consultem suas inscrições. Também permite que o criador do curso gerencie os inscritos e exporte a lista para CSV.
