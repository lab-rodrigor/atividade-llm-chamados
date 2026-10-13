# Chamados com LLM

Projeto Avançado de Software I, DCX/UFPB, 2026.2.

Esta atividade roda na sua máquina, em aula. O programa é uma página onde você cadastra um chamado de suporte de TI e um LLM decide a categoria e a prioridade dele. Você lê como o código chama o LLM e depois ajusta o prompt até o modelo decidir como a central de chamados espera.

## O que você precisa

- JDK 17 ou mais novo. Confira com `java -version`.
- A chave do LLM da disciplina, que o professor enviou por mensagem no Discord.
- Internet. A primeira execução baixa o Maven e as bibliotecas, e leva cerca de 1 minuto.

## Como rodar

Defina a chave e inicie o programa, dentro da pasta do projeto.

Linux e macOS:

```bash
export LLM_API_KEY="cole-a-chave-aqui"
./mvnw compile exec:java
```

Windows (PowerShell):

```powershell
$env:LLM_API_KEY="cole-a-chave-aqui"
.\mvnw.cmd compile exec:java
```

Abra http://localhost:7070, digite um chamado e clique em Cadastrar. Para parar o programa, use Ctrl+C. A chave fica só na sua variável de ambiente e não entra em nenhum arquivo do projeto nem no commit.

## Parte 1: entender o código

Leia as 3 classes de `src/main/java/br/ufpb/dcx/chamados/`, nesta ordem. Juntas têm menos de 160 linhas, com comentários.

1. `Llm.java`: monta a requisição HTTP, envia ao LLM e extrai o texto da resposta.
2. `Classificador.java`: lê o `prompt.txt`, chama o `Llm` e confere a resposta.
3. `App.java`: a página e as duas rotas.

Responda as 4 perguntas da Parte 1 no `PROMPT.md`.

## Parte 2: medir o prompt atual

Cadastre cada chamado da tabela abaixo e compare o resultado com o esperado. Anote quantos o modelo acertou, contando como acerto os que têm categoria e prioridade certas.

| Chamado | Categoria | Prioridade |
|---|---|---|
| A internet do laboratório 2 caiu e a turma está no meio de uma prova. | rede | alta |
| O projetor da sala 5 não liga e a aula começa em 10 minutos. | equipamento | alta |
| Não consigo entrar no SIGAA, diz que minha senha expirou. | acesso | media |
| O Wi-Fi do bloco B está um pouco lento, mas consigo trabalhar normalmente. | rede | baixa |
| Gostaria de pedir a instalação do IntelliJ no computador do laboratório 1, sem pressa. | software | baixa |
| O sistema de notas está retornando erro 500 para todos os professores. | software | alta |
| Uma tecla do teclado do laboratório 3 falha de vez em quando, mas dá para usar. | equipamento | baixa |
| Esqueci minha senha do e-mail institucional e preciso enviar um trabalho hoje. | acesso | media |

Clique em "ver" em cada linha para ler a resposta bruta do LLM, os tokens gastos e o tempo.

## Parte 3: melhorar o prompt

A central de chamados decide assim.

Categorias:

- rede: internet, Wi-Fi, conexão
- acesso: login, senha, permissão de usuário
- equipamento: hardware, como projetor, computador, teclado e impressora
- software: instalação de programa e erro de sistema
- outro: o que não se encaixa nas anteriores

Prioridade:

- alta: impede uma aula ou prova em andamento, ou afeta muitas pessoas ao mesmo tempo
- media: impede o trabalho de uma única pessoa, que não consegue contornar
- baixa: pedido sem urgência, ou incômodo que não impede o trabalho

Um prazo curto de uma pessoa sozinha, como entregar um trabalho hoje, é media.

Edite o `prompt.txt` para que o modelo siga essas regras e cadastre os 8 chamados de novo. O programa lê o arquivo a cada chamado, então você não precisa reiniciar. A meta é 8 de 8. Repare no que acontece com a lista quando o modelo responde algo que o `Classificador` não aceita.

## Desafio

Escreva 3 chamados seus que ainda enganam o modelo, mesmo com o seu melhor prompt, e descreva o que ele respondeu.

## Entrega

Preencha o `PROMPT.md`, confira que o `prompt.txt` tem a sua versão final e envie os dois arquivos ao professor, na forma combinada em aula.
