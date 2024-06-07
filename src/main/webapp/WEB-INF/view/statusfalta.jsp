<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>AGIS - Faltas Parciais</title>
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styles.css"/>'>
<header>
	<h1 align="center">Faltas Parciais</h1>
	<div>
		<jsp:include page="menusecretaria.jsp" />
	</div>
</header>
</head>
<body>
	<form action="statusfalta" method="post">
		<div align="center" class="container">
			<table>
				<tr>
					<td><label>Escolha uma Disciplina</label></td>
					<td colspan="3"><select class="input_data" id="disciplina"
						name="disciplina">
							<option value="0">- - -</option>
							<c:forEach var="d" items="${disciplinas}">
								<c:if
									test="${(empty disciplina) || (disciplina.codigo ne d.codigo)}">
									<option value="${d.codigo}">
										<c:out value="${d.nome}" />
									</option>
								</c:if>
								<c:if test="${d.codigo eq disciplina.codigo}">
									<option value="${d.codigo}" selected="selected">
										<c:out value="${d.nome}" />
									</option>
								</c:if>
							</c:forEach>
					</select></td>
					<td><input type="submit" id="botao" name="botao" value="Listar"></td>
				</tr>
			</table>
		</div>
	</form>
	<br />
	<div align="center">
		<c:if test="${not empty saida}">
			<h2>
				<b><c:out value="${saida}" /></b>
			</h2>
		</c:if>
	</div>
	<br />
	<div align="center">
		<c:if test="${not empty erro}">
			<h2>
				<b><c:out value="${erro}" /></b>
			</h2>
		</c:if>
	</div>
	</br>
	<div align="center">
		<c:if test="${not empty alunos }">
			<div class="container">
				<table class="table-round">
					<thead>
						<tr>
							<td>R.A</td>
							<td>Nome</td>
							<td>Numero de Faltas</td>
							<td>Situa��o</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><c:out value="${a.matricula.aluno.ra}"/></td>
						 	<td><c:out value="${a.matricula.aluno.nome}"/></td>
							<td><c:out value="${a.qtdFaltas}"/></td>
						 	<td><c:out value="${a.situacao}"/></td>
						</tr>
					</tbody>
					<tfoot>
						<tr><td><input type="submit" id="botao" name="botao" value="Gerar Relat�rio"></td></tr>
					</tfoot>
				</table>
			</div>
		</c:if>
	</div>
</body>
</html>