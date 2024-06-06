<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<script>
function editarCliente(codigo) {
	window.location.href = 'cliente?cmd=alterar&codigo=' + codigo;
}
</script>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styles.css"/>'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<title>AGIS - Avaliação</title>

<style>
/* Estilos para o botão "Realizar avaliação" */
.custom-button {
    background-color: #4CAF50; 
    border: none; 
    color: white; 
    padding: 10px 20px; 
    text-align: center; 
    text-decoration: none; 
    display: inline-block; 
    font-size: 16px; 
    margin: 4px 2px; 
    cursor: pointer; 
    border-radius: 12px; 
    transition-duration: 0.4s; 
}

.custom-button:hover {
    background-color: white; 
    color: black; 
    border: 2px solid #4CAF50; 
}

.custom-button i {
    margin-right: 8px; 
}
</style>

<header>
	<h1 align="center">Gerenciamento de Avaliações</h1>
	<div>
		<jsp:include page="menuprofessor.jsp" />
	</div>
</header>
</head>
<body>
	<div align="center" class="container">
		<form action="avaliacao" method="post">
			<table>
				<tr>
					<td colspan="3">
						<select class="input_data" id="disciplina" name="disciplina">
							<option value="0">Escolha uma Disciplina</option>
							<c:forEach var="d" items="${disciplinas}">
								<c:if test="${(empty disciplina) || (disciplina.codigo ne d.codigo)}">
									<option value="${d.codigo}">
										<c:out value="${d.nome}"/>
									</option>
								</c:if>
								<c:if test="${d.codigo eq disciplina.codigo}">
									<option value="${d.codigo}" selected="selected">
										<c:out value="${d.nome}"/>
									</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td><input type="submit" id="botao" name="botao" value="Listar Av. por Disciplina"></td>
				</tr>
				<tr>
					<td colspan="3">
						<input class="input_data" type="number" id="codigo" name="codigo" placeholder="Codigo Avaliacao" value='<c:out value="${avaliacao.avaliacao_codigo}"></c:out>'>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<input class="input_data" type="text" maxlength="100" id="nome" name="nome" placeholder="Nome" value='<c:out value="${avaliacao.nome}"></c:out>'>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<input class="input_data" type="text" id="peso" name="peso" placeholder="Peso" value='<c:out value="${avaliacao.peso}"></c:out>'>
					</td>
				</tr>
				<tr>
					<td><input type="submit" id="botao" name="botao"value="Cadastrar"></td>
					<td><input type="submit" id="botao" name="botao"value="Alterar"></td>
					<td><input type="submit" id="botao" name="botao"value="Excluir"></td>
					<td><input type="submit" id="botao" name="botao"value="Listar"></td>
				</tr>
			</table>
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
<div align="center">
    <c:if test="${not empty avaliacoes}">
        <div class="container">
            <table class="table-round">
                <thead>
                    <tr>
                        <td>Selecionar</td>
                        <td align="center">Disciplina</td>
                        <td align="center">Nome</td>
                        <td align="center">Peso</td>
                        <td align="center">Ação</td>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${avaliacoes}">
                        <tr>
                            <td>
                                <form action="avaliacao" method="post">
                                    <input type="hidden" name="disciplina" value="${a.disciplina.codigo}">
                                    <input type="hidden" name="codigo" value="${a.avaliacao_codigo}">
                                    <input type="hidden" name="botao" value="Buscar">
                                    <center>
                                        <button type="submit" class="custom-button">
                                            <i class="fas fa-pencil-alt" style="font-size: 24px;"></i>
                                        </button>
                                    </center>
                                </form>
                            </td>
                            <td><c:out value="${a.disciplina.nome}"/></td>
                            <td><c:out value="${a.nome}"/></td>
                            <td><c:out value="${a.peso}"/></td>
                            <td>
                                <form action="realizaravaliacao.jsp" method="get">
    										<input type="hidden" name="disciplina" value="${a.disciplina.codigo}">
   											 <input type="hidden" name="codigo" value="${a.codigo}">
   										 <center>
      										  <button type="submit" class="custom-button">
         										   Realizar avaliação
     										   </button>
   										 </center>
								</form>

                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

</body>
</html>
