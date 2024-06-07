<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Realizar Avaliação</title>
<style>
/* Estilos para a tabela */
.table-round {
    border-collapse: collapse;
    width: 80%;
    margin: 20px auto;
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
}

.table-round th, .table-round td {
    padding: 12px 15px;
    text-align: center;
}

.table-round th {
    background-color: #4CAF50;
    color: white;
}

.table-round tr:nth-child(even) {
    background-color: #f2f2f2;
}

.table-round tr:hover {
    background-color: #ddd;
}

/* Estilos para o botão "Salvar Notas" */
.save-button {
    background-color: #4CAF50; 
    border: none; 
    color: white; 
    padding: 10px 20px; 
    text-align: center; 
    text-decoration: none; 
    display: inline-block; 
    font-size: 16px; 
    margin: 20px auto; 
    cursor: pointer; 
    border-radius: 12px; 
    transition-duration: 0.4s; 
}

.save-button:hover {
    background-color: white; 
    color: black; 
    border: 2px solid #4CAF50; 
}
</style>
</head>
<body>

<h2 align="center">Realizar Avaliação</h2>

<div class="container">
    <form action="salvarNotas" method="post">
        <input type="hidden" name="codigodisciplina" value="${param.codigodisciplina}">
        <input type="hidden" name="avaliacao_codigo" value="${param.avaliacao_codigo}">

        <table class="table-round">
            <thead>
                <tr>
                    <th>Nome do Aluno</th>
                    <th>Nota</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="aluno" items="${alunos}">
                    <tr>
                        <td><c:out value="${aluno.nome}"/></td>
                        <td>
                            <input type="number" name="notas[${aluno.id}]" value="<c:out value='${aluno.nota}'/>" min="0" max="10" step="0.01">
                        </td>
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
