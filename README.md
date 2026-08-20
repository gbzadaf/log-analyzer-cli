# Log Analyzer CLI

Ferramenta de linha de comando em Java para análise de arquivos de log no formato *combined* (Apache/Nginx), gerando estatísticas como IPs mais frequentes, distribuição de status codes, e exportação dos resultados em CSV ou JSON.

## Motivação

Analisar logs de servidor rapidamente é uma tarefa comum no dia a dia de times de backend, DevOps e segurança — seja para investigar um pico de erros, identificar um IP se comportando de forma suspeita, ou gerar um relatório periódico de acessos. Este projeto simula essa necessidade de forma simples e direta, sem depender de ferramentas maiores como ELK Stack ou Splunk para análises pontuais e rápidas.

## Features

- Parsing de arquivos de log no formato *combined log* (Apache/Nginx)
- Processamento resiliente: linhas malformadas são ignoradas sem interromper a análise
- Relatório com:
    - Total de requisições
    - Top 5 IPs por frequência de acesso
    - Distribuição de requisições por status code HTTP
- Exportação do relatório em **CSV** ou **JSON**
- Leitura de arquivos via streaming (suporta arquivos grandes sem carregar tudo em memória)
- Interface de linha de comando completa, com `--help` automático

## Tecnologias

- **Java 21**
- **Maven** (gerenciamento de dependências e build)
- **Picocli** — parsing de argumentos de linha de comando
- **Jackson** — serialização JSON
- **JUnit 5** — testes automatizados
- **Maven Shade Plugin** — empacotamento em fat jar executável

## Como rodar

### Pré-requisitos

- Java 21+
- Maven (ou use o wrapper/IDE)

### Build

\`\`\`bash
mvn clean package
\`\`\`

Isso gera um jar executável em \`target/log-analyzer-cli-1.0-SNAPSHOT.jar\`.

### Uso básico

\`\`\`bash
java -jar target/log-analyzer-cli-1.0-SNAPSHOT.jar caminho/para/access.log
\`\`\`

### Opções disponíveis

| Opção | Descrição |
|---|---|
| \`-v\`, \`--verbose\` | Exibe detalhes adicionais durante o processamento |
| \`-e\`, \`--export\` | Formato de exportação: \`csv\` ou \`json\` |
| \`-o\`, \`--output\` | Caminho do arquivo de saída (usado com \`--export\`) |
| \`-h\`, \`--help\` | Exibe ajuda |
| \`-V\`, \`--version\` | Exibe a versão |

### Exemplos

**Análise simples:**
\`\`\`bash
java -jar target/log-analyzer-cli-1.0-SNAPSHOT.jar sample-logs/access.log
\`\`\`

Saída:
\`\`\`
=== RELATORIO DE LOG ===
Total de requisicoes: 10

Top IPs por frequencia:
192.168.0.10 -> 4 requisicoes
127.0.0.1 -> 4 requisicoes
10.0.0.5 -> 2 requisicoes

Requisicoes por status code:
200 -> 6
401 -> 1
404 -> 1
204 -> 1
500 -> 1
\`\`\`

**Exportando para JSON:**
\`\`\`bash
java -jar target/log-analyzer-cli-1.0-SNAPSHOT.jar sample-logs/access.log --export json --output relatorio.json
\`\`\`

**Exportando para CSV:**
\`\`\`bash
java -jar target/log-analyzer-cli-1.0-SNAPSHOT.jar sample-logs/access.log --export csv --output relatorio.csv
\`\`\`


## Testes

O projeto conta com testes unitários (JUnit 5) cobrindo casos válidos, inválidos e edge cases do parser e do gerador de relatórios.

\`\`\`bash
mvn test
\`\`\`

## Possíveis melhorias futuras

- Suporte a outros formatos de log (JSON logs, logs customizados via regex configurável)
- Filtros por intervalo de data/hora
- Testes de integração cobrindo o fluxo completo via CLI
- Pipeline de CI com GitHub Actions

