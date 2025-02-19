# PicPay - Desafio Android

<img src="https://github.com/mobilepicpay/desafio-android/blob/master/desafio-picpay.gif" width="300"/>

Um dos desafios de qualquer time de desenvolvimento é lidar com código legado e no PicPay isso não é diferente. Um dos objetivos de trazer os melhores desenvolvedores do Brasil é atacar o problema. Para isso, essa etapa do processo consiste numa proposta de solução para o desafio abaixo e você pode escolher a melhor forma de resolvê-lo, de acordo com sua comodidade e disponibilidade de tempo:
- Resolver o desafio previamente, e explicar sua abordagem no momento da entrevista.
- Discutir as possibilidades de solução durante a entrevista, fazendo um pair programming (bate-papo) interativo com os nossos devs.

Com o passar do tempo identificamos alguns problemas que impedem esse aplicativo de escalar e acarretam problemas de experiência do usuário. A partir disso elaboramos a seguinte lista de requisitos que devem ser cumpridos ao melhorar nossa arquitetura:

- Em mudanças de configuração o aplicativo perde o estado da tela. Gostaríamos que o mesmo fosse mantido.
- Nossos relatórios de crash têm mostrado alguns crashes relacionados a campos que não deveriam ser nulos sendo nulos e gerenciamento de lifecycle. Gostaríamos que fossem corrigidos.
- Gostaríamos de cachear os dados retornados pelo servidor.
- Haverá mudanças na lógica de negócios e gostaríamos que a arquitetura reaja bem a isso.
- Haverá mudanças na lógica de apresentação. Gostaríamos que a arquitetura reaja bem a isso.
- Com um grande número de desenvolvedores e uma quantidade grande de mudanças ocorrendo testes automatizados são essenciais.
  - Gostaríamos de ter testes unitários testando nossa lógica de apresentação, negócios e dados independentemente, visto que tanto a escrita quanto execução dos mesmos são rápidas.
  - Por outro lado, testes unitários rodam em um ambiente de execução diferenciado e são menos fiéis ao dia-a-dia de nossos usuários, então testes instrumentados também são importantes.

Boa sorte! =)

Ps.: Fique à vontade para editar o projeto inteiro, organização de pastas e módulos, bem como as dependências utilizadas

____________

# Desafio PicPay

## Mudanças realizadas:

## 🛠 Arquitetura
O projeto foi atualizado para utilizar o padrão mvvm + clean architecture, seguindo os padrões de divisão de responsabilidade de cada camada:
- `app` - classe application
- `presentation` - views, viewModels
- `domain` - contratos dos repositories e domain models
- `data` - implementações dos repositories, classes de network e de armazenamento local

# 🔧 Ajustes 
Como citado no readme, alguns ajustes precisavam ser feitos no projeto, dito isso, foi atualizado:
- Foi utilizado o Koin como injeção de dependências, uma ferramenta de configuração mais simples e ao mesmo tempo robusta.
- Para fazer o cache da chamada foi utilizado o room database, com a lógica de remoto/local no repository
- Para as chamadas de rede, a BASE_URL foi movida para um arquivo local e consumida no BuildConfig
- Foram corrigidos dois problemas no recycler view, o de fazer uma nova requisição sempre que rotacionasse, e o de não manter a posição do scroll também quando rotacionasse
- Para atualizar a lista sempre que necessário ao invés de pegar do cache, foi adicionado um swipeRefreshLayout
- Foram adicionados testes instrumentados tanto de UI quanto para o RoomDatabase
- Foram adicionados testes unitários
- Foi adicionado o ktlint para padronização de style de código

## 📝 Padrão de Commits
Nas novas mudanças, foi utilizada a convenção de commits Karma para manter a legibilidade do projeto.

## 🔍 Análise Estática de Código
Foi utilizado o Ktlint para padronizar o código Kotlin. Para verificar, execute:

```
./gradlew ktlintCheck
```

E para formatar, execute:

```
./gradlew ktlintFormat
```

Todas as regras podem ser encontradas no arquivo `.editorconfig`.

## 🧪 Testes Unitários
Para melhorar a qualidade e segurança do código, agora este projeto possui testes unitários:
```
./gradlew testDebugUnitTest
```

## ⚙️ Testes de UI/Instrumentação
Este projeto também possui testes instrumentados e de UI utilizando Espresso e MockWebServer.

Para executar:
```
./gradlew connectedDebugAndroidTest
```

## 🏗️ Como Construir/Executar o Projeto?
Primeiro, compile o projeto com o seguinte comando:

```
./gradlew assembleDebug
```

Em seguida, instale-o em um dispositivo conectado:

```
./gradlew installDebug
```
