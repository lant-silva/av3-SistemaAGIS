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
	<div>
        <jsp:include page="menuprofessor.jsp" />
    </div>
</header>
</head>
<body>
	<h1 class="gerenciamento-matricula">Avaliação</h1>
	<div class="container">
		<form action="realizaravaliacao" method="post">
			<input type="hidden" name="disciplina_codigo"
				value="${param.disciplina_codigo}"> <input type="hidden"
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
					<input type="hidden" name="matriculaCodigo" value="${a.matricula.matricula.codigo }"/>
						<tr>
							<td><c:out value="${a.matricula.matricula.aluno.nome}" /></td>
							<td><input type="number" name="nota"
								value="<c:out value='${a.nota}'/>" min="0" max="10"
								step="0.01"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div align="center">
				<input type="submit" id="botao" name="botao" value="Salvar"></input>
			</div>
		</form>
	</div>
	<br />
    <div align="center">
        <c:if test="${not empty saida}">
            <h2><b><c:out value="${saida}"/></b></h2>
        </c:if>
    </div>
    <br />
    <div align="center">
        <c:if test="${not empty erro}">
            <h2><b><c:out value="${erro}"/></b></h2>
        </c:if>
    </div>
</body>
</html>
