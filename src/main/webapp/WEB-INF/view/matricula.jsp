<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styles.css"/>'>
<title>AGIS - Matricula</title>
<header>
	<div>
		<jsp:include page="menualuno.jsp" />
	</div>
</header>
</head>
<body>
	<h1 class="gerenciamento-matricula">Gerenciamento de Matricula</h1>
	
	<h4 id="periodo-matricula" align="center">*Per�odo de matr�cula: 15 a 21 de Janeiro / 15 a 21 de Julho*</h4>

	<form action="matricula" method="post">
		<div align="center" class="container">
			<tr>
				<td colspan="3">
   					 <input class="input_data" type="text" id="ra" name="ra" placeholder="R.A" value="${aluno.ra}" maxlength="9" oninput="this.value = this.value.replace(/[^0-9]/g, '')">
   					 <br> 
   					 <c:if test="${intervalo}">
       				 <input type="submit" class="consultar-botao" name="botao" value="Iniciar Matricula">
   					 </c:if>
    				<input type="submit" class="consultar-botao" name="botao" value="Consultar Matricula">
				</td>
			</tr>

		</div>
		</br>
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
		</br>
		<div align="center">
			<c:if test="${not empty disciplinas and listar eq 'false'}">
				<table id="listaDisciplinas" class="table_round" align="center">
					<thead>
						<tr>
							<th></th>
							<th>Nome</th>
							<th>Qtd. Aulas</th>
							<th>Hor�rio de In�cio</th>
							<th>Hor�rio de T�rmino</th>
							<th>Dia</th>
							<th>Situa��o</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<tr>
								<td>
									<div>
										<c:if test="${d.situacao eq 'N�o cursado' or d.situacao eq 'Reprovado'}">
											<input type="checkbox" name="disciplinasSelecionadas"
											value="${d.disciplina.codigo}">
										</c:if>
									</div>
								</td>
								<td><c:out value="${d.disciplina.nome}" /></td>
								<td><c:out value="${d.disciplina.qtdAulas}" /></td>
								<td><c:out value="${d.disciplina.horarioInicio}" /></td>
								<td><c:out value="${d.disciplina.horarioFim}" /></td>
								<td><c:out value="${d.disciplina.dia}" /></td>
								<td><c:out value="${d.situacao}" /></td>
							</tr>
						</c:forEach>
						</br>
						<td><input type="submit" id="botao" name="botao"
							value="Confirmar Matricula">
						<td />
					</tbody>
				</table>
			</c:if>
			
			<c:if test="${not empty disciplinas and listar eq 'true'}">
			<div class="container">
				<table id="listaDisciplinas" class="table_round" align="center">
					<thead>
						<tr> 
							<th>Nome</th>
							<th>Qtd. Aulas</th>
							<th>Hor�rio de In�cio</th>
							<th>Hor�rio de T�rmino</th>
							<th>Dia</th>
							<th>Situa��o</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<tr>
								<c:if test="${d.situacao eq 'Em curso' or d.situacao eq 'Aprovado' }">
									<td><c:out value="${d.disciplina.nome}" /></td>
									<td><c:out value="${d.disciplina.qtdAulas}" /></td>
									<td><c:out value="${d.disciplina.horarioInicio}" /></td>
									<td><c:out value="${d.disciplina.horarioFim}" /></td>
									<td><c:out value="${d.disciplina.dia}" /></td>
									<td><c:out value="${d.situacao}" /></td>
								</c:if>
							</tr>
						</c:forEach>
						</br>
					</tbody>
				</table>
				</div>
			</c:if>
		</div>
	</form>
</body>
</html>