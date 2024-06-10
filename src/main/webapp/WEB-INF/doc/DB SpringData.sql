CREATE DATABASE springagis
GO
USE springagis
GO
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
		RAISERROR('CPF Inválido - não tem 11 numeros', 16, 1)
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
		RAISERROR('CPF Inválido - valores invalidos', 16, 1)
		RETURN
	END
END
GO
CREATE TRIGGER t_validaridade ON aluno
AFTER INSERT, UPDATE
AS
BEGIN
	DECLARE @dtnasc DATE
	SELECT @dtnasc = data_nasc FROM INSERTED
	IF((DATEDIFF(YEAR,@dtnasc,GETDATE()) < 16) OR (DATEDIFF(YEAR,@dtnasc,GETDATE()) > 120))
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Data de nascimento inválida', 16, 1)
		RETURN
	END
END
GO
CREATE PROCEDURE sp_gerarmatricula(@ra CHAR(9), @codigomatricula INT OUTPUT)
AS
BEGIN
	DECLARE @cont INT
	DECLARE @novocodigo INT = 0

	SELECT @cont = COUNT(*)
	FROM matricula
	WHERE aluno_ra = @ra

	IF(@cont >= 1) -- Caso o aluno já seja matriculado
	BEGIN
		SELECT TOP 1 @codigomatricula = codigo 
		FROM matricula WHERE aluno_ra = @ra 
		ORDER BY codigo DESC
		-- Insiro o aluno em uma nova matricula

		SELECT TOP 1 @novocodigo = codigo + 1
		FROM matricula
		ORDER BY codigo DESC

		INSERT INTO matricula VALUES
		(@novocodigo, GETDATE(), @ra) -- o ultimo valor é só um placeholder


		-- Como a lógica para atualização da matricula será realizada por outra procedure,
		-- eu apenas reinsiro a ultima matricula feita pelo aluno
		INSERT INTO matricula_disciplina (matricula_codigo, disciplina_codigo, situacao, nota_final, qtd_faltas)
		SELECT @novocodigo, disciplina_codigo, situacao, nota_final, qtd_faltas FROM dbo.fn_ultimamatricula(@codigomatricula)

		-- Retorno o novo codigo
		SET @codigomatricula = @novocodigo
	END
	ELSE -- A primeira matricula do aluno
	BEGIN
		IF NOT EXISTS(SELECT * FROM matricula) --Se nenhuma outra matrícula existir (garante que tenha um codigo para ser inserido)
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
		(@codigomatricula, GETDATE(), @ra)

		INSERT INTO matricula_disciplina (matricula_codigo, disciplina_codigo, situacao, nota_final, qtd_faltas)
		SELECT * FROM dbo.fn_matriculainicial(@codigomatricula)
	END
END
GO
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

	RAISERROR('Matricula cancelada: Existe conflito de horários', 16, 1)
	RETURN
END
GO
CREATE FUNCTION fn_ultimamatricula(@codigomatricula INT)
RETURNS @tabela TABLE(
matricula_codigo INT,
disciplina_codigo INT,
situacao VARCHAR(50),
qtd_faltas INT,
nota_final FLOAT
)
AS
BEGIN
	INSERT INTO @tabela (matricula_codigo, disciplina_codigo, situacao, qtd_faltas, nota_final)
	SELECT @codigomatricula AS matricula_codigo, md.disciplina_codigo, md.situacao, md.qtd_faltas, md.nota_final
	FROM matricula_disciplina md, matricula m, aluno a, curso c
	WHERE md.matricula_codigo = @codigomatricula	
		AND m.codigo = @codigomatricula
		AND a.curso_codigo = c.codigo
		AND m.aluno_ra = a.ra
	RETURN
END
GO
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
GO
CREATE PROCEDURE sp_salvarnota(@avaliacaocodigo INT, @disciplinacodigo INT, @matriculacodigo INT, @nota FLOAT, @saida VARCHAR(200) OUTPUT)
AS
BEGIN
	UPDATE nota_avaliacao
	SET nota = @nota
	WHERE avaliacao_codigo = @avaliacaocodigo
	AND disciplina_codigo = @disciplinacodigo
	AND matricula_codigo = @matriculacodigo
END
GO
CREATE FUNCTION fn_listaralunos(@codigodisciplina INT, @avaliacaodisciplina INT)
RETURNS @tabela TABLE(
aluno_ra CHAR(9),
aluno_nome VARCHAR(100),
avaliacao_codigo INT,
matricula_codigo INT,
disciplina_codigo INT,
nota FLOAT
)
AS
BEGIN
	INSERT INTO @tabela (aluno_ra, aluno_nome, avaliacao_codigo, matricula_codigo, disciplina_codigo, nota)
	SELECT aluno_ra = a.ra, aluno_nome = a.nome, avaliacao_codigo = av.avaliacao_codigo, matricula_codigo = m.codigo, disciplina_codigo = d.codigo, na.nota
	FROM aluno a, matricula m, matricula_disciplina md, avaliacao av, disciplina d, nota_avaliacao na
	WHERE a.ra = m.aluno_ra
		AND m.codigo = md.matricula_codigo
		AND d.codigo = md.disciplina_codigo
		AND d.codigo = av.disciplina_codigo
		AND d.codigo = na.disciplina_codigo
		AND d.codigo = @codigodisciplina
		AND av.avaliacao_codigo = @avaliacaodisciplina
		AND md.situacao != 'Não cursado'
		AND av.avaliacao_codigo = na.avaliacao_codigo
		AND na.matricula_codigo = m.codigo
	RETURN
END
GO
CREATE PROCEDURE sp_inseriraula(@codigomatricula INT, @codigodisciplina INT, @presenca INT, @dataAula DATE, @saida VARCHAR(200) OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT TOP 1 * FROM aula WHERE matricula_codigo = @codigomatricula AND disciplina_codigo = @codigodisciplina AND data_aula = @dataAula)
	BEGIN
		RAISERROR('Chamada para o dia selecionado já foi realizada', 16, 1)
		RETURN
	END
	ELSE
	BEGIN
		INSERT INTO aula (matricula_codigo, disciplina_codigo, data_aula, presenca)VALUES
		(@codigomatricula, @codigodisciplina, @dataAula, @presenca)
		 
		--definir o total de faltas pro aluno
		UPDATE matricula_disciplina
		SET qtd_faltas = qtd_faltas + (4 - @presenca)
		WHERE matricula_codigo = @codigomatricula
			AND disciplina_codigo = @codigodisciplina

		SET @saida = 'Chamada finalizada'
	END
END
GOF
CREATE FUNCTION fn_notasparciais(@ra CHAR(9))
RETURNS @tabela TABLE(
disciplina_codigo INT,
avaliacao_codigo INT,
matricula_codigo INT,
disciplina_nome VARCHAR(100),
nome_avaliacao VARCHAR(10),
nota CHAR(3),
nota_final CHAR(3),
situacao VARCHAR(50)
)
AS
BEGIN
	INSERT INTO @tabela (disciplina_codigo, avaliacao_codigo, matricula_codigo, disciplina_nome, nome_avaliacao, nota, nota_final, situacao)
	SELECT disciplina_codigo = d.codigo, avaliacao_codigo = av.avaliacao_codigo, matricula_codigo = m.codigo,
		   disciplina_nome = d.nome, nome_avaliacao = av.nome, nota = na.nota, nota_final = md.nota_final, situacao = md.situacao
	FROM disciplina d, avaliacao av, nota_avaliacao na, aluno a, matricula m, matricula_disciplina md
		WHERE a.ra = @ra
			AND a.ra = m.aluno_ra
			AND m.codigo = md.matricula_codigo
			AND d.codigo = md.disciplina_codigo
			AND av.disciplina_codigo = d.codigo
			AND av.avaliacao_codigo = na.avaliacao_codigo
			AND na.disciplina_codigo = d.codigo
			AND na.matricula_codigo = md.matricula_codigo
	RETURN
END
GO
CREATE FUNCTION fn_situacaodisciplina()
RETURNS @tabela TABLE(
disciplina_codigo INT,
matricula_codigo INT,
aluno_ra CHAR(9),
aluno_nome VARCHAR(100),
disciplina_nome VARCHAR(100),
qtd_faltas INT,
nota_final CHAR(3),
situacao VARCHAR(50),
situacao_nota VARCHAR(50)
)
AS
BEGIN
	DECLARE @disciplinacodigo INT,
			@matriculacodigo INT,
			@alunora CHAR(9),
			@alunonome VARCHAR(100),
			@disciplinanome VARCHAR(100),
			@qtdfaltas INT,
			@situacao VARCHAR(50),
			@notafinal CHAR(3),
			@situacaonota VARCHAR(50)

	DECLARE cur CURSOR FOR
		SELECT DISTINCT d.codigo, m.codigo, a.ra, d.nome, a.nome, md.qtd_faltas, md.nota_final
		FROM disciplina d, matricula_disciplina md, matricula m, aluno a
		WHERE d.codigo = md.disciplina_codigo
		AND m.codigo = md.matricula_codigo
		AND a.ra = m.aluno_ra
		AND md.situacao != 'Não cursado'
	OPEN cur
	FETCH NEXT FROM cur INTO
		@disciplinacodigo, @matriculacodigo, @alunora, @disciplinanome, @alunonome, @qtdfaltas, @notafinal
	WHILE @@FETCH_STATUS = 0
	BEGIN
		DECLARE @qtdaulas INT
		SELECT @qtdaulas = qtd_aulas FROM disciplina WHERE codigo = @disciplinacodigo
		IF(@qtdfaltas > (@qtdaulas * 5))
		BEGIN
			SET @situacao = 'Reprovado por falta'
		END
		ELSE
		BEGIN
			SET @situacao = 'Aprovado por falta'
		END
		
		IF(CAST(@notafinal AS FLOAT) >= 6)
		BEGIN
			SET @situacaonota = 'Aprovado por Nota'
		END
		ELSE
		IF(CAST(@notafinal AS FLOAT) < 6 AND CAST(@notafinal AS FLOAT) >= 4)
		BEGIN
			SET @situacaonota = 'Em exame'
		END
		ELSE
		BEGIN
			SET @situacaonota = 'Reprovado por Nota'
		END

			INSERT INTO @tabela (disciplina_codigo, matricula_codigo, aluno_ra, disciplina_nome, aluno_nome, qtd_faltas, situacao, nota_final, situacao_nota) VALUES
			(@disciplinacodigo, @matriculacodigo, @alunora, @disciplinanome, @alunonome, @qtdfaltas, @situacao, @notafinal, @situacaonota)

		FETCH NEXT FROM cur INTO
		@disciplinacodigo, @matriculacodigo, @alunora, @disciplinanome, @alunonome, @qtdfaltas, @notafinal
	END
	CLOSE cur
	DEALLOCATE cur
	RETURN
END
GO
CREATE FUNCTION fn_listarultimamatricula(@ra char(9))
RETURNS @tabela TABLE(
matricula_codigo INT,
disciplina_codigo INT,
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
nota_final CHAR(3)
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

	INSERT INTO @tabela (matricula_codigo, disciplina_codigo, nome, codigo_professor, nome_professor, qtd_aulas, horario_inicio, horario_fim, dia, curso_codigo, data_matricula, situacao, qtd_faltas, nota_final)	
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
GO
CREATE PROCEDURE sp_alunodispensa(@alunora CHAR(9), @codigodisciplina INT, @motivo VARCHAR(200), @saida VARCHAR(200) OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM dispensa WHERE aluno_ra = @alunora AND disciplina_codigo = @codigodisciplina AND estado = 'Em andamento')
	BEGIN
		RAISERROR('Disciplina já possui dispensa pendente', 16, 1)
		RETURN
	END
	ELSE
	IF EXISTS(SELECT * FROM dispensa WHERE aluno_ra = @alunora AND disciplina_codigo = @codigodisciplina AND estado = 'Pedido de dispensa recusado')
	BEGIN
		RAISERROR('Pedido de dispensa já foi recusado', 16, 1)
		RETURN
	END
	ELSE
	BEGIN
		INSERT INTO dispensa VALUES
		(@alunora, @codigodisciplina, @motivo, 'Em andamento')
		SET @saida = 'Dispensa solicitada'
	END
END
GO
CREATE FUNCTION fn_matriculainicial(@codigomatricula INT)
RETURNS @tabela TABLE(
matricula_codigo INT,
disciplina_codigo INT,
situacao VARCHAR(50),
qtd_faltas INT,
nota_final CHAR(3)
)
AS
BEGIN
	INSERT INTO @tabela (matricula_codigo, disciplina_codigo, situacao, qtd_faltas, nota_final)
	SELECT @codigomatricula, d.codigo, 'Não cursado' AS situacao, 0 AS qtd_faltas, 0 AS nota_final
	FROM matricula m, curso c, disciplina d, aluno a
	WHERE d.curso_codigo = c.codigo
		AND a.curso_codigo = c.codigo
		AND m.codigo = @codigomatricula
		AND m.aluno_ra = a.ra
	RETURN
END
GO
CREATE FUNCTION fn_alunochamada()
RETURNS @tabela TABLE(
aluno_nome VARCHAR(100),
aluno_ra CHAR(9),
disciplina_codigo INT,
matricula_codigo INT
)
AS
BEGIN
	INSERT INTO @tabela (aluno_nome, aluno_ra, disciplina_codigo, matricula_codigo)
	SELECT DISTINCT a.nome AS nome, a.ra AS ra, d.codigo AS disciplina_codigo, m.aluno_ra AS aluno_ra
	FROM aluno a, matricula m, matricula_disciplina md, disciplina d
	WHERE m.aluno_ra = a.ra
	AND md.matricula_codigo = m.codigo
	AND md.disciplina_codigo = d.codigo
	AND md.situacao = 'Em curso'
	RETURN
END
GO
CREATE PROCEDURE sp_verificaraula(@codigodisciplina INT, @dataaula DATE, @saida BIT OUTPUT)
AS
BEGIN
	IF EXISTS(SELECT * FROM aula WHERE disciplina_codigo = @codigodisciplina AND @dataaula = data_aula)
	BEGIN
		SET @saida = 1
	END
	ELSE
	BEGIN
		SET @saida = 0
	END
END
GO
CREATE TRIGGER t_geraravaliacoes ON avaliacao
AFTER INSERT, DELETE
AS
BEGIN
	DECLARE @matriculacodigo INT,
			@disciplinacodigo INT,
			@avaliacaodisciplina INT,
			@avaliacaocodigo INT
	DECLARE cur CURSOR FOR
	SELECT md.matricula_codigo, md.disciplina_codigo
	FROM matricula_disciplina md
	WHERE md.situacao = 'Em curso'

	IF NOT EXISTS(SELECT * FROM DELETED)
	BEGIN
		SELECT @avaliacaodisciplina = disciplina_codigo, @avaliacaocodigo = avaliacao_codigo FROM inserted

		OPEN cur
		FETCH NEXT FROM cur INTO @matriculacodigo, @disciplinacodigo
		WHILE @@FETCH_STATUS = 0
		BEGIN
			IF(@disciplinacodigo = @avaliacaodisciplina)
			BEGIN
				INSERT INTO nota_avaliacao VALUES
				(0, @avaliacaocodigo, @disciplinacodigo, @matriculacodigo)
			END
		FETCH NEXT FROM cur INTO @matriculacodigo, @disciplinacodigo
		END
	END
	ELSE
	BEGIN
		SELECT @avaliacaodisciplina = disciplina_codigo, @avaliacaocodigo = avaliacao_codigo FROM deleted
		OPEN cur
		FETCH NEXT FROM cur INTO @matriculacodigo, @disciplinacodigo
		WHILE @@FETCH_STATUS = 0
		BEGIN
		IF(@disciplinacodigo = @avaliacaodisciplina)
			BEGIN
				DELETE nota_avaliacao
				WHERE avaliacao_codigo = @avaliacaocodigo
				AND	disciplina_codigo = @disciplinacodigo
				AND matricula_codigo = @matriculacodigo
			END
			FETCH NEXT FROM cur INTO @matriculacodigo, @disciplinacodigo
		END
	END
	CLOSE CUR
	DEALLOCATE CUR
END
GO
CREATE TRIGGER t_atualizarmedia ON nota_avaliacao
AFTER INSERT, UPDATE
AS
BEGIN
			DECLARE @nota CHAR(3),
				@matriculacodigo INT,
				@disciplinacodigo INT,
				@avaliacaocodigo INT,
				@peso FLOAT,
				@media CHAR(3)
		SELECT @nota = nota, @matriculacodigo = matricula_codigo, @disciplinacodigo = disciplina_codigo, @avaliacaocodigo = avaliacao_codigo FROM inserted

		SELECT @media = CAST(ROUND(SUM(na.nota * a.peso), 2) AS CHAR)
		FROM nota_avaliacao na, avaliacao a
		WHERE na.avaliacao_codigo = a.avaliacao_codigo
			AND na.matricula_codigo = @matriculacodigo
			AND na.disciplina_codigo = @disciplinacodigo

		UPDATE matricula_disciplina
		SET nota_final = @media
		WHERE disciplina_codigo = @disciplinacodigo
			AND matricula_codigo = @matriculacodigo
END