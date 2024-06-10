# Avaliação 3 - Lab. de Banco de Dados

# Integrantes

- Luiz Antonio da Silva Cruz

- Davi de Queiroz Romão

# Escopo

No sistema acadêmico AGIS, se permite fazer o encerramento de semestre.

A partir da relação de matrícula, criar uma tela que permita inserir as notas parciais das
disciplinas. Para verificar o funcionamento da funcionalidade, utilizar as seguintes disciplinas
(Os códigos podem ser ajustados conforme projeto já em desenvolvimento):

![image](https://github.com/lant-silva/av3-SistemaAGIS/assets/125385378/2288a525-4467-485b-8b59-aa20df79f385)

Não estão contemplados, neste momento, avaliações substitutivas ou recuperações.

Na tela de consulta de notas, deve-se apresentar as notas parciais e as médias calculadas
de acordo com os pesos.

Deve-se apresentar, nesta tela, também, um status:

- Aprovado -> Média >= 6,0
  
- Exame -> Média >= 3,0 e Média <= 6,0

- Reprovado -> Média < 3,0

Com base nos lançamentos das faltas, deve-se exibir, em uma tela, uma tabela com a lista
dos alunos matriculados nas disciplinas e, por semana, a quantidade de faltas. A
penúltima coluna deve ser o total de faltas do aluno. A última coluna deve ser o status do
aluno (Reprovado para frequência inferior a 75% do total de aulas do semestre e Aprovado
para frequência superior a 75% do total de aulas do semestre). A tela deve ser montada a
partir da saída de uma UDF com cursor.

Deve-se gerar uma tela relatório que permita gerar um PDF de saída semelhante à tela de
notas e um PDF de saída semelhante à tela de consulta de frequências.

# Diagramas

### Diagrama de Entidade e Relacionamento

![image](https://github.com/lant-silva/av3-SistemaAGIS/assets/125385378/0906b8d8-94e3-4cb7-a56d-a3ec20ae3a82)

### Diagrama de Caso de Uso

![image](https://github.com/lant-silva/av3-SistemaAGIS/assets/125385378/7e374494-21a5-4872-b70c-6698f8e74a7a)

