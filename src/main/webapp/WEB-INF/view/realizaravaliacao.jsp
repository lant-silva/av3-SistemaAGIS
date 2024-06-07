<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href='<c:url value = "./resources/css/styles.css"/>'>
<title>Realizar Avaliação</title>
<header>
	<h1 align="center">Faltas Parciais</h1>
</header>
</head>
<body>
	<div class="container">
		<form action="realizaravaliacao" method="post">
			<input type="hidden" name="codigodisciplina"
				value="${param.codigodisciplina}"> <input type="hidden"
				name="avaliacao_codigo" value="${param.avaliacao_codigo}">

			<table class="table-round">
				<thead>
					<tr>
						<th>Nome do Aluno</th>
						<th>Nota</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="a" items="${alunos}">
						<tr>
							<td><c:out value="${a.matricula.matricula.aluno.nome}" /></td>
							<td><input type="number" name="notas[${aluno.id}]"
								value="<c:out value='${aluno.nota}'/>" min="0" max="10"
								step="0.01"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div align="center">
				<button type="submit" class="save-button">Salvar Notas</button>
			</div>
		</form>
	</div>
</body>
</html>
