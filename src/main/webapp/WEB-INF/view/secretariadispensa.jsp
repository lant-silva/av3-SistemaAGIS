<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styles.css"/>'>
<title>AGIS - Confirmação de Dispensas</title>
<header>
    <div>
        <jsp:include page="menusecretaria.jsp" />
    </div>
</header>
</head>
<body>
	<h1 class="gerenciamento-matricula">Gerenciamento de Dispensas</h1>
    <div align="center" class="container">
        <form action="secretariadispensa" method="post">
            <c:choose>
            	<c:when test="${not empty dispensas }">
            	<table class="table_round">
                    <thead>
                        <tr>
                            <th>RA</th>
                            <th>Aluno</th>
                            <th>Curso</th>
                            <th>Disciplina</th>
                            <th>Motivo</th>
                            <th>Aprovação</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${dispensas }">
                            <tr id="dispensa_${d.aluno.ra}_${d.disciplina.codigo}">
                                <td><c:out value="${d.aluno.ra}" /></td>
                                <td><c:out value="${d.aluno.nome}" /></td>
                                <td><c:out value="${d.disciplina.curso.nome}" /></td>
                                <td><c:out value="${d.disciplina.nome}" /></td>
                                <td><c:out value="${d.motivo}" /></td>

                                <td>
                                    <select class="input_data" id="aprovacao_${d.aluno.ra}_${d.disciplina.codigo}" name="aprovacao_${d.aluno.ra}_${d.disciplina.codigo}">
                                        <option value="Recusar">Recusar</option>
                                        <option value="Aprovar">Aprovar</option>
                                    </select>
                                </td>
                                <td>
                                    <button type="button" onclick="resolverDispensa('${d.aluno.ra}', '${d.disciplina.codigo}', document.getElementById('aprovacao_${d.aluno.ra}_${d.disciplina.codigo}').value)">Concluir</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            	</c:when>
            	<c:otherwise>
            		<div align="center">
              		      <h2><b>Não há dispensas a serem tratadas</b></h2>
            		</div>
            	</c:otherwise>
            </c:choose>
            <br />
            <div align="center">
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
        </form>
    </div>
</body>
<script>
// Função para concluir a dispensa
function resolverDispensa(alunora, disciplina) {      
    // Exibe um prompt de confirmação
    if (confirm("Tem certeza que deseja concluir?")) {
        // Obtém o valor da aprovação selecionada
        var aprovacao = document.getElementById('aprovacao_' + alunora + '_' + disciplina).value;
     
        // Faz uma requisição POST para o servidor com os dados da dispensa
        fetch('secretariadispensa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                ra: alunora,
                disciplina: disciplina,
                aprovacao: aprovacao
            })
        })
        .then(response => response.text()) // Converte a resposta para texto
        .then(data => {
            console.log(data); // Exibe a resposta no console
            // Exibe uma mensagem de sucesso
            alert('Dispensa concluída com sucesso!');
        })
        .catch(error => {
            console.error('Erro:', error); // Exibe erro no console
            // Exibe uma mensagem de erro
            alert('Erro ao concluir dispensa');
        });
    }
}
</script>
</html>