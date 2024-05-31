CREATE DATABASE springagis
USE springagis

CREATE TRIGGER t_validarcpf ON aluno
AFTER INSERT
AS
BEGIN
	DECLARE @cpf CHAR(11)
	SELECT @cpf = cpf FROM INSERTED

	DECLARE @soma1 INT,
	@soma2 INT,
	@cont INT,
	@digito1 INT,
	@digito2 INT

	SET @cont = 1
	SET @soma1 = 0
	SET @soma2 = 0

	IF LEN(@cpf) <> 11
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('CPF Inv�lido - n�o tem 11 numeros', 16, 1)
		RETURN
	END

	WHILE(@cont <= 9)	
	BEGIN 
		SET @soma1 = @soma1 + (CAST(SUBSTRING(@cpf, @cont, 1) AS INT) * (11 - @cont))
		SET @cont = @cont + 1
	END
	SET @cont = 1
	WHILE(@cont <= 10)
	BEGIN
		SET @soma2 = @soma2 + (CAST(SUBSTRING(@cpf, @cont, 1) AS INT) * (12 - @cont))
		SET @cont = @cont + 1
	END
	IF((@soma1 % 11) <  2)
	BEGIN
		SET @digito1 = 0
	END
	ELSE
	BEGIN
		SET @digito1 = 11 - (@soma1 % 11)
	END

	IF((@soma2 % 11) < 2)
	BEGIN
		SET @digito2 = 0
	END
	ELSE
	BEGIN
		SET @digito2 = 11 - (@soma2 % 11)
	END
	IF @digito1 = CAST(SUBSTRING(@cpf, 10, 1) AS INT) AND @digito2 = CAST(SUBSTRING(@cpf, 11, 1) AS INT)
	BEGIN
		RETURN
	END
	ELSE
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('CPF Inv�lido - valores invalidos', 16, 1)
		RETURN
	END
END

CREATE TRIGGER t_validaridade ON aluno
AFTER INSERT, UPDATE
AS
BEGIN
	DECLARE @dtnasc DATE
	SELECT @dtnasc = data_nasc FROM INSERTED
	IF((DATEDIFF(YEAR,@dtnasc,GETDATE()) < 16) OR (DATEDIFF(YEAR,@dtnasc,GETDATE()) > 120))
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Data de nascimento inv�lida', 16, 1)
		RETURN
	END
END

CREATE PROCEDURE sp_geraranolimite(@ano CHAR(4), @sem CHAR(1), @anolimite CHAR(6) OUTPUT)
AS
BEGIN
SET @ano = CAST((CAST(@ano AS INT) + 5) AS CHAR)

IF(@sem = '1')
BEGIN
	SET @sem = '2'
END
ELSE
BEGIN
	SET @sem = '1'
END
SET @anolimite = FORMATMESSAGE('%s/%s', @ano, @sem)
END



CREATE PROCEDURE sp_gerarra(@ano CHAR(4), @sem CHAR(1), @ra CHAR(9) OUTPUT)
AS

DECLARE @existe BIT = 1
WHILE(@existe = 1)
BEGIN
	DECLARE @n1 CHAR(1) = CAST(RAND()*10+1 AS CHAR)
	DECLARE @n2 CHAR(1) = CAST(RAND()*10+1 AS CHAR)
	DECLARE @n3 CHAR(1) = CAST(RAND()*10+1 AS CHAR)
	DECLARE @n4 CHAR(1) = CAST(RAND()*10+1 AS CHAR)
	
	SET @ra = @ano + @sem + @n1 + @n2 + @n3 + @n4
	
	-- Verifica se o RA gerado j� pertence a um aluno, caso contr�rio, outro RA vai ser gerado
	IF EXISTS(SELECT ra FROM aluno WHERE ra = @ra)
	BEGIN 
		SET @existe = 1
	END
	ELSE 
	BEGIN 
		SET @existe = 0
	END
END

CREATE PROCEDURE sp_gerarmatricula(@ra CHAR(9), @codigomatricula INT OUTPUT)
AS
BEGIN
	DECLARE @cont INT
	DECLARE @novocodigo INT = 0
	DECLARE @ano CHAR(4)
	DECLARE @sem CHAR(1)

	EXEC sp_geraringresso @ano OUTPUT, @sem OUTPUT

	SELECT @cont = COUNT(*)
	FROM matricula
	WHERE aluno_ra = @ra

	IF(@cont >= 1) -- Caso o aluno j� seja matriculado
	BEGIN
		SELECT TOP 1 @codigomatricula = codigo 
		FROM matricula WHERE aluno_ra = @ra 
		ORDER BY codigo DESC
		-- Insiro o aluno em uma nova matricula

		SELECT TOP 1 @novocodigo = codigo + 1
		FROM matricula
		ORDER BY codigo DESC

		INSERT INTO matricula VALUES
		(@novocodigo, @ra, GETDATE()) -- o ultimo valor � s� um placeholder


		-- Como a l�gica para atualiza��o da matricula ser� realizada por outra procedure,
		-- eu apenas reinsiro a ultima matricula feita pelo aluno
		INSERT INTO matricula_disciplina
		SELECT @novocodigo, disciplina_codigo, situacao, qtd_faltas, nota_final FROM dbo.fn_ultimamatricula(@codigomatricula)

		-- Retorno o novo codigo
		SET @codigomatricula = @novocodigo
	END
	ELSE -- A primeira matricula do aluno
	BEGIN
		IF NOT EXISTS(SELECT * FROM matricula) --Se nenhuma outra matr�cula existir (garante que tenha um codigo para ser inserido)
		BEGIN
			SET @codigomatricula = 1000001
		END
		ELSE
		BEGIN
			SELECT TOP 1 @codigomatricula = codigo + 1
			FROM matricula
			ORDER BY codigo DESC
		END

		INSERT INTO matricula VALUES
		(@codigomatricula, @ra, GETDATE())

		INSERT INTO matricula_disciplina (matricula_codigo, disciplina_codigo, situacao, qtd_faltas, nota_final)
		SELECT * FROM dbo.fn_matriculainicial(@codigomatricula)
	END
END

CREATE PROCEDURE sp_inserirmatricula(@ra CHAR(9), @codigomatricula INT, @codigodisciplina INT, @saida VARCHAR(200) OUTPUT)
AS
DECLARE @conflito BIT,
		@qtdaula INT,
		@horarioinicio TIME,
		@horariofim TIME,
		@diasemana VARCHAR(50),
		@codigoconteudo INT

SELECT @qtdaula = d.qtd_aulas, @horarioinicio = d.horario_inicio, @horariofim = d.horario_fim, @diasemana = d.dia, @codigoconteudo = c.codigo
FROM disciplina d, matricula_disciplina md, matricula m, conteudo c
WHERE d.codigo = @codigodisciplina
	ANd md.disciplina_codigo = d.codigo
	AND md.matricula_codigo = m.codigo
	AND m.codigo = @codigomatricula
	AND m.aluno_ra = @ra
	AND c.disciplina_codigo = @codigodisciplina

EXEC sp_verificarconflitohorario @codigomatricula, @qtdaula, @diasemana, @horarioinicio, @horariofim, @conflito OUTPUT

PRINT @conflito
IF(@conflito = 0)
BEGIN
	UPDATE matricula_disciplina 
	SET situacao = 'Em curso'
	WHERE matricula_codigo = @codigomatricula 
		AND disciplina_codigo = @codigodisciplina
	SET @saida = 'Matricula finalizada.'
END
ELSE
BEGIN
	DELETE matricula_disciplina
	WHERE matricula_codigo = @codigomatricula

	DELETE matricula
	WHERE codigo = @codigomatricula

	RAISERROR('Matricula cancelada: Existe conflito de hor�rios', 16, 1)
	RETURN
END

CREATE PROCEDURE sp_verificarconflitohorario(@codigomatricula INT, @qtdaulas INT, @diasemana VARCHAR(50), @horarioinicio TIME, @horariofim TIME, @conflito BIT OUTPUT)
AS
DECLARE @conflitoexiste INT

SELECT @conflitoexiste = COUNT(*)
FROM matricula_disciplina md, disciplina d
WHERE md.matricula_codigo = @codigomatricula
	AND md.disciplina_codigo = d.codigo
	AND d.dia = @diasemana
	AND	(md.situacao = 'Em curso')
	AND ((@horarioinicio BETWEEN d.horario_inicio AND d.horario_fim) 
	OR (@horariofim BETWEEN d.horario_inicio AND d.horario_fim) 
	OR (d.horario_inicio BETWEEN @horarioinicio AND @horariofim) 
	OR (d.horario_fim BETWEEN @horarioinicio AND @horariofim))

																	
IF (@conflitoexiste >= 1)
BEGIN
	SET @conflito = 1
END
ELSE
BEGIN
	SET @conflito = 0
END

CREATE FUNCTION fn_listarultimamatricula(@ra char(9))
RETURNS @tabela TABLE(
matricula_codigo INT,
codigo INT,
nome VARCHAR(100),
codigo_professor INT,
nome_professor VARCHAR(100),
qtd_aulas INT,
horario_inicio TIME(0),
horario_fim TIME(0),
dia VARCHAR(20),
curso_codigo INT,
data_matricula CHAR(6),
situacao VARCHAR(50),
qtd_faltas INT,
nota_final CHAR(2)
)
AS
BEGIN
	DECLARE @codigomatricula INT
	DECLARE @ano CHAR(4)
	DECLARE @sem CHAR(1)
	DECLARE @data CHAR(6)

	SELECT @ano = YEAR(data_matricula) FROM matricula WHERE aluno_ra = @ra
	SELECT @sem = MONTH(data_matricula) FROM matricula WHERE aluno_ra = @ra
	IF(@sem <= 6)
	BEGIN
		SET @sem = 1
	END
	ELSE
	BEGIN
		SET @sem = 2
	END
	SET @data = FORMATMESSAGE('%s/%s', @ano, @sem)

	SELECT TOP 1 @codigomatricula = codigo 
	FROM matricula WHERE aluno_ra = @ra
	ORDER BY codigo DESC

	INSERT INTO @tabela (matricula_codigo, codigo, nome, codigo_professor, nome_professor, qtd_aulas, horario_inicio, horario_fim, dia, curso_codigo, data_matricula, situacao, qtd_faltas, nota_final)	
	SELECT CAST(md.matricula_codigo AS VARCHAR), CAST(d.codigo AS VARCHAR),
		   d.nome, CAST(p.codigo AS VARCHAR), p.nome, CAST(d.qtd_aulas AS VARCHAR),
		   d.horario_inicio, d.horario_fim, d.dia AS dia, 
		   CAST(d.curso_codigo AS VARCHAR), @data, md.situacao,
		   CAST(md.qtd_faltas AS VARCHAR), CAST(md.nota_final AS VARCHAR)
	FROM matricula_disciplina md, disciplina d, aluno a, matricula m, curso c, professor p
	WHERE m.codigo = @codigomatricula
		AND m.aluno_ra = a.ra
		AND md.matricula_codigo = @codigomatricula
		AND md.disciplina_codigo = d.codigo
		AND a.curso_codigo = c.codigo
		AND d.curso_codigo = c.codigo
		AND p.codigo = d.professor_codigo
	ORDER BY situacao ASC
	RETURN
END

CREATE PROCEDURE sp_teste
AS
BEGIN
	INSERT INTO curso VALUES
	(101, 'An�lise e Desenvolvimento de Sistemas', 2800, 'ADS', 5),
	(102, 'Desenvolvimento de Software Multiplataforma', 1400, 'DSM', 5)

	INSERT INTO professor VALUES
	(1001, 'Marcelo Silva', 'Mestre'),
	(1002, 'Rafael Medeiros', 'Mestre'),
	(1003, 'Adriana Bastos', 'Doutora'),
	(1004, 'Henrique Galv�o', 'Mestre'),
	(1005, 'Ulisses Santos Barbosa', 'Doutor'),
	(1006, 'Pedro Guimar�es', 'Mestre'),
	(1007, 'Reinaldo Santos', 'Doutor'),
	(1008, 'Pedro Lima', 'Mestre'),
	(1009, 'Marcelo Soares', 'Doutor'),
	(1010, 'Costa Lima de Souza', 'Mestre'),
	(1011, 'Gabriela Gon�alves', 'Doutora'),
	(1012, 'Yasmin Ribeiro', 'Mestre')

	INSERT INTO disciplina VALUES
	(1001, 'Laborat�rio de Banco de Dados', 4, '14:50', '18:20', 'Segunda', 101, 1001),
	(1002, 'Banco de Dados', 4, '14:50', '18:20', 'Ter�a', 101, 1001),
	(1003, 'Algor�tmos e L�gica de Programa��o', 4, '14:50', '18:20', 'Segunda', 101, 1001),
	(1004, 'Matem�tica Discreta', 4, '13:00', '16:30','Quinta', 101, 1001),
	(1005, 'Linguagem de Programa��o', 4, '14:50', '18:20', 'Ter�a', 101, 1001),
	(1006, 'Estruturas de Dados', 2, '13:00', '14:40', 'Ter�a', 101, 1001),
	(1007, 'Programa��o Mobile', 4, '13:00', '16:30', 'Sexta', 101, 1001),
	(1008, 'Empreendedorismo', 2, '13:00', '14:40', 'Quarta', 101, 1002),
	(1009, '�tica e Responsabilidade', 2, '16:50', '18:20', 'Segunda', 101, 1002),
	(1010, 'Administra��o Geral', 4, '14:50', '18:20', 'Ter�a', 101, 1002),
	(1011, 'Sistemas de Informa��o', 4, '13:00', '16:30', 'Ter�a', 101, 1002),
	(1012, 'Gest�o e Governan�a de TI', 4, '14:50', '18:20', 'Sexta', 101, 1002),
	(1013, 'Redes de Computadores', 4, '14:50', '18:20', 'Quinta', 101, 1004),
	(1014, 'Contabilidade', 2, '13:00', '14:40', 'Quarta', 101, 1001),
	(1015, 'Economia e Finan�as', 4, '13:00', '16:30', 'Quarta', 101, 1004),
	(1016, 'Arquitetura e Organiza��o de Computadores', 4, '13:00', '16:30', 'Segunda', 101, 1001),
	(1017, 'Laborat�rio de Hardware', 4, '13:00', '16:30', 'Segunda', 101, 1001),
	(1018, 'Sistemas Operacionais', 4, '14:50', '18:20', 'Quinta', 101, 1001),
	(1019, 'Sistemas Operacionais 2', 4, '14:50', '18:20', 'Sexta', 101, 1001),
	(1020, 'Programa��o Web', 4, '13:00', '16:30', 'Ter�a', 101, 1001),
	(1021, 'Programa��o em Microinform�tica', 2, '13:00', '14:40', 'Sexta', 101, 1004),
	(1022, 'Programa��o Linear', 2, '13:00', '14:40', 'Segunda', 101, 1004),
	(1023, 'C�lculo', 4, '13:00', '16:30', 'Segunda', 101, 1003),
	(1024, 'Teste de Software', 2, '13:00', '14:40', 'Quinta', 101, 1002),
	(1025, 'Engenharia de Software 1', 4, '13:00', '16:30', 'Segunda', 101, 1001),
	(1026, 'Engenharia de Software 2', 4, '13:00', '16:30', 'Ter�a', 101, 1002),
	(1027, 'Engenharia de Software 3', 4, '14:50', '18:20', 'Segunda', 101, 1005),
	(1028, 'Laborat�rio de Engenharia de Software', 4, '14:50', '18:20', 'Quarta', 101, 1004),
	(1029, 'Ingl�s 1', 4, '14:50', '18:20', 'Sexta', 101, 1005),
	(1030, 'Ingl�s 2', 2, '14:50', '16:30', 'Ter�a', 101, 1005),
	(1031, 'Ingl�s 3', 2, '13:00', '14:40', 'Sexta', 101, 1005),
	(1032, 'Ingl�s 4', 2, '13:00', '14:40', 'Segunda', 101, 1005),
	(1033, 'Ingl�s 5', 2, '13:00', '14:40', 'Ter�a', 101, 1005),
	(1034, 'Ingl�s 6', 2, '13:00', '14:40', 'Quinta', 101, 1005),
	(1035, 'Sociedade e Tecnologia', 2, '14:50', '16:30', 'Ter�a', 101, 1002),
	(1036, 'Intera��o Humano Computador', 4, '14:50', '18:20', 'Ter�a', 101, 1002),
	(1037, 'Estat�stica Aplicada', 4, '14:50', '18:20', 'Quarta', 101, 1004),
	(1038, 'Laborat�rio de Redes de Computadores', 4, '14:50', '18:20', 'Sexta', 101, 1004),
	(1039, 'Intelig�ncia Artificial', 4, '13:00', '16:30', 'Quarta', 101, 1004),
	(1040, 'Programa��o para Mainframes', 4, '14:50', '18:20', 'Quarta', 101, 1004)

	INSERT INTO disciplina VALUES
	(1041, 'Desenvolvimento de Aplica��es Distribu�das', 4, '13:00', '16:30', 'Segunda', 102, 1006),
	(1042, 'Seguran�a de Aplica��es Web', 4, '13:00', '16:30', 'Segunda', 102, 1006),
	(1043, 'Banco de Dados NoSQL', 4, '13:00', '16:30', 'Ter�a', 102, 1006),
	(1044, 'Gerenciamento de Projetos de Software �gil', 4, '13:00', '16:30', 'Ter�a', 102, 1007),
	(1045, 'Desenvolvimento de Aplica��es M�veis', 4, '13:00', '16:30', 'Quarta', 102, 1012),
	(1046, 'Desenvolvimento de APIs', 2, '13:00', '14:40', 'Quarta', 102, 1011),
	(1047, 'Modelagem de Dados', 4, '13:00', '16:30', 'Quinta', 102, 1011),
	(1048, 'Arquitetura de Software Distribu�do', 4, '13:00', '16:30', 'Quinta', 102, 1011),
	(1049, 'Engenharia de Requisitos Avan�ada', 4, '13:00', '16:30', 'Sexta', 102, 1010),
	(1050, 'Metodologias �geis', 2, '13:00', '14:40', 'Sexta', 102, 1010),
	(1051, 'Desenvolvimento de Interfaces Gr�ficas', 4, '14:50', '18:20', 'Segunda', 102, 1010),
	(1052, 'Auditoria de Sistemas', 4, '14:50', '18:20', 'Segunda', 102, 1010),
	(1053, 'Administra��o de Bancos de Dados', 4, '14:50', '18:20', 'Ter�a', 102, 1009),
	(1054, 'Gest�o de Projetos de TI', 4, '14:50', '18:20', 'Ter�a', 102, 1009),
	(1055, 'Desenvolvimento de Jogos Digitais', 4, '14:50', '18:20', 'Quarta', 102, 1009),
	(1056, 'Seguran�a de Redes', 2, '14:50', '16:30', 'Quarta', 102, 1008),
	(1057, 'Minera��o de Dados', 4, '14:50', '18:20', 'Quinta', 102, 1008),
	(1058, 'Arquitetura de Software Orientada a Servi�os', 4, '14:50', '18:20', 'Quinta', 102, 1006),
	(1059, 'An�lise de Neg�cios em TI', 4, '14:50', '18:20', 'Sexta', 102, 1007),
	(1060, 'DevOps', 2, '14:50', '16:30', 'Sexta', 102, 1007),
	(1061, 'Desenvolvimento de Sistemas Embarcados', 2, '16:40', '18:20', 'Segunda', 102, 1007),
	(1062, 'Criptografia e Seguran�a de Dados', 2, '16:40', '18:20', 'Segunda', 102, 1007),
	(1063, 'Big Data Analytics', 2, '16:40', '18:20', 'Ter�a', 102, 1007),
	(1064, 'Gerenciamento �gil de Projetos', 2, '16:40', '18:20', 'Ter�a', 102, 1008),
	(1065, 'Desenvolvimento de Aplica��es Desktop', 2, '16:40', '18:20', 'Quarta', 102, 1008),
	(1066, 'Seguran�a em IoT', 2, '16:40', '18:20', 'Quarta', 102, 1008),
	(1067, 'Banco de Dados Geoespaciais', 2, '16:40', '18:20', 'Quinta', 102, 1008),
	(1068, 'Arquitetura de Microservi�os', 2, '16:40', '18:20', 'Quinta', 102, 1009),
	(1069, 'Engenharia de Requisitos Elicita��o e An�lise', 2, '16:40', '18:20', 'Sexta', 102, 1009),
	(1070, 'Scrum e M�todos �geis', 2, '16:40', '18:20', 'Sexta', 102, 1009),
	(1071, 'Desenvolvimento de Aplica��es H�bridas', 4, '13:00', '16:30', 'Segunda', 102, 1007),
	(1072, 'An�lise de Riscos em Seguran�a da Informa��o', 4, '13:00', '16:30', 'Segunda', 102, 1007),
	(1073, 'Banco de Dados Distribu�dos', 4, '13:00', '16:30', 'Ter�a', 102, 1008),
	(1074, 'Gest�o de Projetos de Desenvolvimento de Software', 4, '13:00', '16:30', 'Ter�a', 102, 1009),
	(1075, 'Desenvolvimento de Aplica��es para Dispositivos M�veis', 4, '13:00', '16:30', 'Quarta', 102, 1009),
	(1076, 'Seguran�a da Informa��o em Cloud Computing', 2, '13:00', '14:40', 'Quarta', 102, 1010),
	(1077, 'Data Science Aplicado', 4, '13:00', '16:30', 'Quinta', 102, 1011),
	(1078, 'Arquitetura de Microsservi�os Distribu�dos', 4, '13:00', '16:30', 'Quinta', 102, 1012),
	(1079, 'Engenharia de Requisitos para Sistemas Distribu�dos', 4, '13:00', '16:30', 'Sexta', 102, 1012),
	(1080, 'Kanban e Lean para Desenvolvimento de Software', 2, '13:00', '14:40', 'Sexta', 102, 1012)
END