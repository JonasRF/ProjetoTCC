# ProjetoTCC

DESENVOLVIMENTO DA APLICAÇÃO

### Introdução

** O diagrama da Figura 2.0 descreve a arquitetura do projeto Codeflix, uma plataforma de streaming de vídeo 
desenvolvida sob o paradigma de microsserviços. A estrutura do sistema, conforme ilustrada pelo diagrama C4, 
foi concebida para operar como uma entidade coesa e escalável, embora seja composta por múltiplos 
serviços independentes e especializados. Para apresentar um modelo de desenvolvimento eficiente em microsserviços 
será desenvolvido o backend do admin do catálogo de vídeos pois esse microsserviço tem toda estrutura necessária 
para se ter um serviço escalável, resiliente, tolerante a falhas e seguro.**
<br/>
<br/>

Figura 2.0 – Diagrama C4 detalhado do microsserviço de admin do Catalogo de vídeos baseado em contêiner.

<img width="646" height="502" alt="image" src="https://github.com/user-attachments/assets/7feca50f-71aa-46d5-bc9f-b5678fb50ac3" />

<p align="center"> Fonte: Elaborado pelo autor (2025)<p/>

## Código plantuml

O código fonte 1 abaixo representa o diagrama C4 baseado em conteiner
do serviço de administração do catálogo de vídeo, para auxiliar outros 
desenvolvedores a ter uma base de como é aplicado o código utilizando
a ferramenta de código aberto plantuml.

## Código fonte 1: Diagrama C4 em container

```
@startuml container
!include	https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml
' Define o sistema principal System_Boundary(c1, "Codeflix") {

' Define os sistemas externos (neste caso, "Frontend" e "Encoder")

System(encoder, "Encoder de vídeos", "Realiza o encoding dos vídeos para mpeg-dash")
Container(bucket_video,"Bucket	de	armazenamento	de	vídeos encodados","GCP")
Rel(encoder,bucket_video,"Faz	upload	do	vídeo	convertido via","HTTPS")

' Define o limite do Backend

System_Boundary(c2, "Backend: Admin do Catálogo de Vídeos") {
Container(app, "App", "Linguagem livre", "Sistema que gerencia os vídeos, incluindo as categorias e gêneros")
Container(db, "Banco de dados", "MySQL", "Armazena dados do catálogo")
}
Container(bucket, "Bucket de vídeos", "Google Cloud Storage", "Armazena os vídeos brutos")
}

' Define as interações entre os componentes

Rel(app, encoder, "Consome dados do vídeo convertido via", "RabbitMQ Fila: video.encoded")
Rel(encoder, app, "Publica dados do vídeo recém-criado via", "RabbitMQ Fila: video.created")
Rel_R(app, db, "Interage com db via", "SQL") Rel_L(app, bucket, "Faz upload de vídeo via", "HTTPS") @enduml
```
<p align="center> Fonte: Elaborado pelo autor (2025) <p/>

## Arquitetura do sistema

<center> 
A arquitetura do Codeflix é caracterizada pela divisão de funcionalidades em componentes autônomos, 
organizados em microsserviços que se comunicam por meio de APIs REST e serviços de mensageria. Esse 
modelo arquitetural permite que cada serviço seja especializado em um domínio específico, possibilitando 
a adoção de tecnologias distintas conforme a necessidade de cada módulo. Além disso, favorece escalabilidade, 
resiliência e manutenibilidade, aspectos destacados por Newman (2017) como centrais na adoção de microsserviços.
Segundo Fowler e Lewis (2014), a descentralização do desenvolvimento e da persistência de dados constitui uma das 
principais vantagens da abordagem em microsserviços, uma vez que reduz o acoplamento entre módulos e viabiliza a
evolução independente do sistema. Dessa forma, o Codeflix incorpora tais princípios, combinando comunicação síncrona 
via APIs e integração assíncrona por eventos, o que está em consonância com o modelo de event-driven architecture 
defendido por Richardson (2018).
<center/> 
<br>
<br>
## Componentes
<br>
O sistema é composto pelos seguintes módulos principais:
<br>
<br>
Serviços de Backend:
<br>
<br>
-	Backend Administrativo do Catálogo de Vídeos: gerencia o catálogo, incluindo processos de conversão e organização de vídeos.
	Gerenciamento de Dados e Mídia:
-	Database Administrativo do Catálogo de Vídeos (MySQL): mantém informações administrativas relacionadas ao catálogo.
	Buckets de Armazenamento:
-	Vídeos brutos (raw): guarda os arquivos originais enviados.
-	Vídeos processados (encoded): contém os arquivos convertidos em formatos prontos para streaming.
	Processamento de Mídia:
-	Encoder de Vídeos: converte os arquivos originais para o padrão MPEG-DASH, amplamente utilizado em soluções de streaming adaptativo (ISO/IEC 23009- 1:2019).
 

	Serviços de Infraestrutura Compartilhada:

-	Keycloak: implementa autenticação e autorização, em conformidade com o modelo de Identity and Access Management (IAM).
  	Interações e Papéis dos Atores
-	Administrador do Catálogo de Vídeos: utiliza o Frontend Administrativo que será desenvolvido futuramente para realizar 
    a manutenção do catálogo de vídeos e suas categorias, apoiado pelo Backend Administrativo.
	
Aplicação

Nesta subseção serão apresentados os principais trechos do microsserviço, bem como os testes, as integrações feitas com APIs e bancos de dados. 
Os requisitos funcionais (RFs) e não funcionais (RNFs) exercem papel essencial no processo de engenharia de software, especialmente em sistemas baseados 
em arquitetura de microsserviços, como o microsserviço de administração do catálogo de vídeos do projeto Codeflix. Esses requisitos estabelecem tanto as 
funções específicas que o sistema deve desempenhar quanto as características de qualidade e restrições que garantem sua eficiência, segurança e 
manutenibilidade (SOMMERVILLE, 2011)

Requisitos

Os Requisitos Funcionais conforme exposto na Tabela 01, definem as ações, serviços e comportamentos que o sistema deve realizar para atender aos objetivos de 
negócio e às necessidades dos usuários. No contexto do Codeflix, os RFs contemplam as operações de cadastro, atualização, exclusão e consulta de entidades como 
categorias, gêneros, elenco e vídeos. Também abrangem funcionalidades de upload e gerenciamento de mídias, controle de publicação de vídeos, além de mecanismos de 
autenticação, autorização e auditoria. Esses requisitos representam a espinha dorsal do microsserviço, garantindo que ele cumpra sua função de administrar o catálogo 
de vídeos de forma segura, escalável e coerente com as regras de negócio. Segundo Pressman e Maxim (2021), requisitos funcionais bem definidos são fundamentais para que 
o software entregue valor real aos stakeholders e reduza ambiguidades durante o processo de desenvolvimento.
 
Tabela 01 – Requisitos Funcionais (RFs)

<img width="739" height="755" alt="image" src="https://github.com/user-attachments/assets/d6e0d1c7-310e-432e-a289-de1c1dd2e949" />

                                        
## Modelagem
<center>
Uma vez que nesse projeto será aplicado o design arquitetural do clean arquitecture, não serão utilizados os tactical partterns: Domain Services e Factories conforme foi destacado de verde na Figura 2.1 Tatical Patterns. Será aplicado os componentes conforme é demostrado na Figura 2.2 que é a seção que descreve a estrutura de camadas da Clean Architecture, um padrão que visa isolar as regras de negócio de frameworks e detalhes de implementação. A ideia é que as dependências fluam para dentro, ou seja, as camadas mais externas dependem das mais internas, mas nunca o contrário.</center>

                                              
<br><img width="753" height="578" alt="image" src="https://github.com/user-attachments/assets/841dd242-f905-4afd-8237-360de163b10e" /><br>

                                                   
A Figura 2.2 ilustra o diagrama da arquitetura da aplicação, que combina os princípios de Domain-Driven Design (DDD) com os componentes da Clean Architecture. O diagrama mostra a base do DDD com o uso de Value Objects (como Identifier, Name, Money) e Aggregates (como Category, Genre, Cast Member, Video), que encapsulam a lógica de negócio principal. Esses Aggregates são então mapeados para os Componentes da Clean Architecture, que incluem as Entidades do DDD, os Use Cases (responsáveis pela lógica da aplicação), os Gateways (que fazem a interface com a infraestrutura/persistência) e os Presenters (que preparam os dados para a interface). O diagrama estabelece a clareza e separação de responsabilidades entre as diferentes camadas.<br>
                                        
<img width="796" height="460" alt="image" src="https://github.com/user-attachments/assets/b3c0377f-6674-40ac-9574-81aa901b146b" />
                                
Esses padrões e camadas trabalham juntos conforme a Figura 2.2, criam um sistema com um design robusto, que é fácil de manter, testar e evoluir. A combinação de DDD e Clean Architecture é muito comum, pois o DDD ajuda a modelar o domínio de forma clara, enquanto a Clean Architecture organiza o código para que o domínio seja o centro da aplicação, protegido de detalhes técnicos.


Descrição do domínio

O agregado Categoria conforme observado na Tablela 03, representa a entidade central do domínio do catálogo de vídeos, responsável por agrupar e organizar conteúdos audiovisuais de acordo com critérios temáticos ou estratégicos, como “Filmes”, “Séries”, “Lançamentos” ou “Infantil”. Seu principal objetivo é estruturar o catálogo de forma lógica, permitindo melhor indexação, filtragem e recomendação de vídeos dentro do sistema. Cada categoria é definida por um nome e uma descrição opcional, além de possuir um estado de ativação, que indica se está ativa ou inativa. O agregado garante a integridade dos dados por meio de regras de negócio, como a obrigatoriedade do nome, que deve conter no mínimo três caracteres, e a validação do estado ativo. A descrição, por sua vez, é um campo flexível, podendo ser nula ou em branco.
                   Tabela 03 - Descrição de domínio do agregado de categoria:

Categoria
Nome	Nome da categoria
Descrição	Texto descritivo da categoria
Ativo	Indica se a categoria está ativa ou inativa
Criado	Data da criação
Atualizado	Data da última atualização
Deletado	Data da exclusão
Regras de negócio
Nome não dever ser nulo ou em branco
Nome deve conter no mínimo 3 caracteres
Descrição pode ser nula ou em branco
                                       
                                          Fonte: Elaborado pelo autor (2025)
 

Além disso, o agregado mantém informações de auditoria, incluindo as datas de criação, atualização e exclusão, que permitem rastrear o ciclo de vida da entidade. No contexto do domínio, o agregado Categoria estabelece uma relação conceitual com os agregados Gênero e Vídeo, podendo ser referenciada por ambos para classificação e associação de conteúdo. Ainda assim, preserva sua autonomia e independência transacional, conforme os princípios de isolamento entre agregados propostos por Evans (2004) em Domain-Driven Design.

O agregado Gênero representa uma categoria temática que agrupa vídeos de acordo com características comuns de conteúdo, como Ação, Drama, Comédia ou Documentário conforme demonstrado na Tabela 04. Seu principal objetivo é organizar e classificar os vídeos no catálogo, permitindo uma melhor experiência de navegação e busca por parte dos usuários. Cada gênero possui um nome e um estado de ativação, que indica se ele está ativo ou inativo no sistema. O agregado é responsável por garantir a integridade das informações, assegurando que o nome não seja nulo ou vazio, e que os registros mantenham consistência quanto ao seu ciclo de vida, controlado por atributos de auditoria; como datas de criação, atualização e exclusão lógica. Os agregados de categoria e gênero possuem um relacionamento muitos para muitos conforme e demonstrado na imagem acima. Uma vez que esse tipo de relacionamento tem a necessidade de ter uma tabela associativa conforme Figura onde são configuradas as chaves compostas, que são as chaves primarias de categoria e gênero.
Tabela 04 - Descrição de domínio do agregado de gênero:

Gênero
Nome	Nome do gênero
Ativo	Indica se o gênero está ativo ou inativo
Criado	Data da criação
Atualizado	Data da última atualização
Deletado	Data da exclusão
Regras de negócio
Nome não dever ser nulo ou em branco

Fonte: Elaborado pelo autor (2025)
 
                   Figura 2.3 – Diagrama da tabela associativa entre os agregados de categoria e gênero



Categoria
N to N	

Gênero

		

                               Fonte: Elaborado pelo autor (2025)

O agregado Cast Member representado na Tabela 05, representa as pessoas que participam da produção dos vídeos do catálogo, podendo assumir papéis distintos, como ator ou diretor. Cada membro possui um nome e um tipo, definidos no momento do cadastro. O agregado é responsável por garantir a consistência e validade dessas informações, assegurando que o nome não seja nulo ou vazio, contenha entre 3 e 255 caracteres e que o tipo pertença a um conjunto pré-definido de valores válidos. Além disso, o agregado mantém informações de auditoria, como data de criação, atualização e exclusão, permitindo rastrear alterações ao longo do tempo. Esse agregado se relaciona indiretamente com o agregado Vídeo, sendo referenciado por meio de identificadores.

               Tabela 05 - Descrição de domínio do agregado de Cast Member:

Membro do elenco
Nome	Nome do membro do elenco
Tipo	Tipo do membro
Criado	Data da criação
Atualizado	Data da última atualização
Deletado	Data da exclusão
Regras de negócio
Nome não dever ser nulo ou em branco
Nome deve conter no mínimo 3 e no máximo 255
caracteres
 
                                                   Fonte: Elaborado pelo autor (2025)
 
O agregado Vídeo constitui o núcleo central do domínio do catálogo de vídeos, representando o conteúdo audiovisual disponibilizado na plataforma conforme representado na Tabela 06. Seu propósito é gerenciar todas as informações essenciais sobre um vídeo, abrangendo dados descritivos, classificações, mídias associadas e vínculos com outras entidades do domínio. Cada vídeo possui um título, uma descrição, o ano de lançamento e a duração, que caracterizam o conteúdo e facilitam sua identificação dentro do catálogo. O título é obrigatório e limitado a 255 caracteres, enquanto a descrição não pode ser vazia. O ano de lançamento e a classificação indicativa (rating) são informações mandatórias, garantindo a conformidade com políticas de exibição e recomendações etárias.
                                    Tabela 06 - Descrição de domínio do agregado de Vídeo:

Vídeo
Título	Título do vídeo
Descrição	Descrição do vídeo
Ano de
lançamento	Ano de lançamento do vídeo
Duração	Tempo de duração do vídeo
Publicado	Data de publicação
Auditoria	Data de criação e atualização
Rating	Classificação do vídeo
Thumb	Imagem
Meia thumb	Imagem
Banner	Imagem
Trailer	mídia
Mídia	url original, url encodada, controle de status
Relacionamento com Categorias(várias)
Relacionamento com Gêneros (várias)
Relacionamento com Membros do elenco (várias)
Regras de negócio
Título não dever ser nulo ou em branco
Título deve conter no máximo 255 caracteres
Descrição não pode ser vazia
Ano de lançamento é obrigatório
Rating é obrigatório
                                                  
                                                     Fonte: Elaborado pelo autor (2025)
 
O agregado também mantém metadados de mídia, como imagens de thumb, meia thumb e banner, além de elementos audiovisuais como trailer e arquivo principal de vídeo. Esses arquivos são armazenados por meio de URLs originais e codificadas, com controle de status de processamento, assegurando o acompanhamento do ciclo de vida da mídia digital. Além disso, o agregado Vídeo registra informações de auditoria, incluindo as datas de criação, atualização e publicação, permitindo rastreabilidade e controle de versões. No que se refere às relações entre agregados, o Vídeo estabelece associações com Categoria, Gênero e Membro do Elenco, representando as classificações temáticas e autorais do conteúdo. Cada vídeo pode estar vinculado a múltiplas categorias, gêneros e membros do elenco, reforçando a modelagem orientada a composição e o princípio de agregados independentes do Domain- Driven Design (EVANS, 2004).

7.9  Diagrama

O diagrama Figura 2.4, apresenta a arquitetura modular do microsserviço de admin do catálogo de vídeos do projeto CodeFlix, fundamentada em princípios de Design Orientado a Domínio (DDD). A arquitetura é centrada nas agregações, que representam as entidades de negócio essenciais do domínio: Category, Genre, Cast Member e Video. A camada de aplicação, implementada com Spring Boot e exposta via REST API, serve como a interface do sistema, recebendo as requisições externas. Essa camada delega a execução da lógica de negócio para a camada de Use Cases (Casos de Uso), que encapsula as operações específicas sobre as gregações, como Create, List, Delete, Get e Update. O design separa claramente as responsabilidades, isolando a lógica de negócio dos detalhes de infraestrutura e da interface de comunicação. Além disso, a arquitetura incorpora serviços externos essenciais: RabbitMQ é utilizado para mensageria assíncrona, viabilizando a comunicação desacoplada entre os componentes, enquanto Google Cloud Storage é empregado para o armazenamento de arquivos multimídia, como vídeos.
 


Em suma, a estrutura proposta resulta em um sistema escalável e de fácil manutenção, onde a lógica de negócio reside nos Casos de Uso e opera sobre os agregados do domínio, enquanto a comunicação externa e a infraestrutura são tratadas por tecnologias e serviços modernos e robustos.

                                        Figura 2.4 – Diagrama do serviço de admin do catálogo de vídeos


Fonte: Elaborado pelo autor (2025)
 
8	APRESENTAÇÃO DA APLICAÇÃO
8.1	Pirâmide de testes

A Figura 2.5 apresenta a pirâmide dos níveis de testes, proposta por Cohn (2010), que organiza a proporção e a importância relativa dos diferentes tipos de testes em um projeto de software. Essa abordagem foi aplicada no desenvolvimento do microsserviço de administração do catálogo de vídeos do projeto Codeflix, permitindo estruturar a estratégia de verificação e validação do código. No nível mais baixo da pirâmide estão os testes unitários, que verificam de forma isolada o funcionamento de pequenas partes do código, como métodos ou funções. Por não dependerem de frameworks externos ou integrações, esses testes são extremamente rápidos e representam a maior parte dos testes implementados na aplicação. O nível intermediário é composto pelos testes de integração, que validam a comunicação e o funcionamento conjunto entre diferentes unidades do sistema.

                                                Figura 2.5 – A pirâmide dos níveis de testes

                                                Fonte: Imagem adaptada de COHN, 2010


No contexto do microsserviço em estudo, eles foram utilizados especialmente para garantir que os componentes da camada de infraestrutura interagissem corretamente com o framework adotado. Por fim, no topo da pirâmide encontram-se os testes de ponta a ponta (E2E), que simulam a experiência real do usuário,
 
percorrendo todo o fluxo de interação com o sistema, desde o envio de requisições até a visualização de resultados. Embora mais complexos e demorados, eles garantem que as funcionalidades críticas do microsserviço operem como esperado no ambiente de produção (COHN, 2010).

