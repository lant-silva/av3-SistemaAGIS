<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styles.css"/>'>
<title>AGIS - Notas Parciais</title>
<header>
	<div>
		<jsp:include page="menualuno.jsp" />
	</div>
</header>
</head>
<body>
	<h1 align="center">Consulta de Notas</h1>
	<div align="center" class="container">
		<form action="statusnota" method="post">
			<table>
				<tr>
					<td><label>R.A:</label></td>
					<td colspan="3"><input class="input_data" type="text" id="ra"
						name="ra" placeholder="R.A" value="${aluno.ra}" maxlength="9"
						oninput="this.value = this.value.replace(/[^0-9]/g, '')"></td>
						<td> <input type="submit" class="consultar-botao" name="botao" value="Consultar"></td>
				</tr>
			</table>
		</form>
	</div>
	<br />
	<div>
    <c:if test="${not empty saida }">
        <h2><b><c:out value="${saida }" /></b></h2>
    </c:if>
	</div>
	<br />
	<div align="center">
    	<c:if test="${not empty erro }">
       		<h2><b><c:out value="${erro }" /></b></h2>
    	</c:if>
	</div>
	<div align="center">
		<c:if test="${not empty notas }">
			<div class="container">
				<table class="table-round">
					<thead>
						<tr>
							<td>Disciplina</td>
							<td>Avaliação</td>
							<td>Nota</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="a" items="${notas }">
							<tr>
								<td><c:out value="${a.matricula.disciplina.nome }" /></td>
								<td><c:out value="${a.avaliacao.nome }" /></td>
								<td><c:out value="${a.nota }" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
					<div align="center">
						<tr><td><input type="submit" id="botao" name="botao" value="Gerar Relatório"></td></tr>
					</div>
			</div>
		</c:if>
	</div>
</body>
</html> 