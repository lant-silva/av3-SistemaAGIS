<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styles.css"/>'>
<title>AGIS - Aluno</title>
<header>
	<div>
		<jsp:include page="menusecretaria.jsp" />
	</div>
</header>
</head>
<body>
	<h1 class="gerenciamento-matricula">Gerenciamento de Alunos</h1>
	<div>
		<div align="center" class="container">
			<form action="aluno" method="post">
				<table>
					<tr>
						<td><label for="cpf">CPF:</label></td>
						<td colspan="3"><input class="input_data" type="number"
							min="0" max="99999999999" id="cpf" name="cpf" placeholder="CPF" 
							value='<c:out value="${aluno.cpf}"></c:out>'>
						<td />
					<tr />
					<tr>
						<td><label for="ra">R.A:</label></td>
						<td colspan="3"><input class="input_data" type="number"
							id="ra" name="ra" placeholder="RA" disabled
							value='<c:out value="${aluno.ra}"></c:out>'>
						<td />
					<tr />
					<tr>
						<td><label for="nome">Nome:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="100" id="nome" name="nome" placeholder="Nome"
							value='<c:out value="${aluno.nome}"></c:out>'>
					</tr>
					<tr>
						<td><label for="nomeSocial">Nome Social:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="100" id="nomeSocial" name="nomeSocial"
							placeholder="Nome Social"
							value='<c:out value="${aluno.nomeSocial}"></c:out>'></td>
					</tr>
					<tr>
						<td><label for="dataNasc">Data de Nascimento:</label></td>
						<td colspan="4"><input class="input_data" type="date"
							id="dataNasc" name="dataNasc"
							value='<c:out value="${aluno.dataNasc}"></c:out>'></td>
					</tr>
					<tr>
						<td><label for="telefoneCelular">Telefone Celular:</label></td>
						<td colspan="3"><input class="input_data" type="text"
							id="telefoneCelular" name="telefoneCelular" placeholder="Telefone Celular"
							value='<c:out value="${aluno.telefoneCelular}"></c:out>'>
						<td />
					</tr>
					<tr>
						<td><label for="telefoneResidencial">Telefone Residencial:</label></td>
						<td colspan="3"><input class="input_data" type="text"
							id="telefoneResidencial" name="telefoneResidencial" placeholder="Telefone Residencial"
							value='<c:out value="${aluno.telefoneResidencial}"></c:out>'>
						<td />
					<tr>
						<td><label for="emailPessoal">Email Pessoal:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="200" id="emailPessoal" name="emailPessoal"
							placeholder="Email Pessoal"
							value='<c:out value="${aluno.emailPessoal}"></c:out>'></td>
					</tr>
					<tr>
						<td><label for="emailCorporativo">Email Corporativo:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="200" id="emailCorporativo" name="emailCorporativo"
							placeholder="Email Corporativo"
							value='<c:out value="${aluno.emailCorporativo}"></c:out>'></td>
					</tr>
					<tr>
						<td><label for="dataSegundoGrau">Data Segundo Grau:</label></td>
						<td colspan="4"><input class="input_data" type="date"
							id="dataSegundoGrau" name="dataSegundoGrau" 
							value='<c:out value="${aluno.dataSegundoGrau}"></c:out>'>
						</td>
					</tr>
					<tr>
						<td><label for="instituicaoSegundoGrau">Inst. Segundo Grau:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="100" id="instituicaoSegundoGrau"
							name="instituicaoSegundoGrau"
							placeholder="Institui��o Segundo Grau"
							value='<c:out value="${aluno.instituicaoSegundoGrau}"></c:out>'>
						</td>
					</tr>
					<tr>
						<td><label for="pontuacaoVestibular">Pontua��o Vestibular:</label></td>
						<td colspan="4"><input class="input_data" type="number"
							min="0" max="1000" id="pontuacaoVestibular"
							name="pontuacaoVestibular" placeholder="Pontua��o no Vestibular" 
							value='<c:out value="${aluno.pontuacaoVestibular}"></c:out>'>
						</td>
					</tr>
					<tr>
						<td><label for="posicaoVestibular">Posi��o Vestibular:</label></td>
						<td colspan="4"><input class="input_data" type="number"
							min="0" max="999999999" id="posicaoVestibular"
							name="posicaoVestibular" placeholder="Posi��o no Vestibular" 
							value='<c:out value="${aluno.posicaoVestibular}"></c:out>'>
						</td>
					</tr>
					<tr>
						<td><label for="semestreGraudacao">Semestre Gradua��o:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							min="1" maxlength="6" id="semestreGraduacao"
							name="semestreGraduacao" placeholder="Semestre de Gradua��o"
							value='<c:out value="${aluno.semestreGraduacao}"></c:out>'>
						</td>
					</tr>
					<tr>
						<td><label for="anoLimite">Ano Limite:</label></td>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="6" id="anoLimite" name="anoLimite"
							placeholder="Ano/Semestre Limite" disabled
							value='<c:out value="${aluno.anoLimite}"></c:out>'></td>
					</tr>
					<tr>
						<td><label for="curso">Curso:</label></td>
						<td colspan="3"><select class="input_data" id="curso"
							name="curso">
								<option value="0">Escolha um curso</option>
								<c:forEach var="c" items="${cursos }">
									<c:if test="${(empty curso) || (c.codigo ne aluno.curso.codigo)}">
										<option value="${c.codigo }">
											<c:out value="${c.nome }"/>
										</option>
									</c:if>
									<c:if test="${c.codigo eq aluno.curso.codigo }">
										<option value="${c.codigo }" selected="selected">
											<c:out value="${c.nome }"/>
										</option>
									</c:if>
								</c:forEach>
						</select>
						<td />
					<tr>
						<td colspan="4"><input class="input_data" type="text"
							maxlength="6" id="turno" name="turno"
							placeholder="Turno" disabled
							value='<c:out value="${aluno.turno}"></c:out>'></td>
					</tr>	
					<tr />
					<tr>
						<td><input type="submit" id="botao" name="botao"
							value="Cadastrar"></td>
						<td><input type="submit" id="botao" name="botao"
							value="Alterar"></td>
						<td><input type="submit" id="botao" name="botao"
							value="Listar"></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<br />
	<div align="center">
		<c:if test="${not empty saida }">
			<H2>
				<b><c:out value="${saida }" /></b>
			</H2>
		</c:if>
	</div>
	<br />
	<div align="center">
		<c:if test="${not empty erro }">
			<H2>
				<b><c:out value="${erro }" /></b>
			</H2>
		</c:if>
	</div>

	<!-- Bot�o Listar Alunos -->
	<div align="center" class="container">
		<c:if test="${not empty alunos}">
			<table class="table-round">
				<thead>
					<tr>
						<th>Selecionar</th>
						<th>CPF</th>
						<th>RA</th>
						<th>Nome</th>
						<th>Nome Social</th>
						<th>Data Nasc.</th>
						<th>Telefone Celular</th>
						<th>Telefone Residencial</th>
						<th>Email Pessoal</th>
						<th>Email Corporativo</th>
						<th>Semestre de Gradua��o</th>
						<th>Semestre Limite</th>
						<th>Curso</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="a" items="${alunos}">
						<tr>
							<td>
							        <form action="aluno" method="post">
                                        <input type="hidden" name="ra" value="${a.ra}">
                                        <input type="hidden" name="botao" value="Buscar">
                                        <input type="submit" value=" ">       
                                    </form>
							</td>
							<td><c:out value="${a.cpf}" /></td>
							<td><c:out value="${a.ra}" /></td>
							<td><c:out value="${a.nome}" /></td>
							<td><c:out value="${a.nomeSocial}" /></td>
							<td><c:out value="${a.dataNasc}" /></td>
							<td><c:out value="${a.telefoneCelular}" /></td>
							<td><c:out value="${a.telefoneResidencial}" /></td>
							<td><c:out value="${a.emailPessoal}" /></td>
							<td><c:out value="${a.emailCorporativo}" /></td>
							<td><c:out value="${a.semestreGraduacao}" /></td>
							<td><c:out value="${a.anoLimite}" /></td>
							<td><c:out value="${a.curso.sigla}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>