<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.dark.css">
    <!--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css">-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.bootgrid.css"/>
    <script src="http://code.jquery.com/jquery-2.1.4.js"></script>
    <!--<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>-->
    <script src="${pageContext.request.contextPath}/js/jquery.bootgrid.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.bootgrid.fa.min.js"></script>
    <script>
        $(function() {
            $("#grid-data").bootgrid({
                ajax: true,
                ajaxSettings: {
                    method: "GET",
                    cache: false
                },
                url: '${pageContext.request.contextPath}/groups/<c:out value="${groupId}" />/requests',
                formatters: {
                    "id": function(column, row) {
                        return '<a href="${pageContext.request.contextPath}/requests/' + row.id + '/view">' + row.id +'</a>';
                    }
                }
            });
        });
    </script>
</head>
<body style="padding:10px;">


    <table id="grid-data" class="table table-condensed table-hover table-striped">
        <thead>
            <tr>
                <th data-column-id="id" data-formatter="id">ID</th>
                <th data-column-id="name">Name</th>
                <th data-column-id="creationDate">Created</th>
                <th data-column-id="method">Method</th>
                <th data-column-id="path">Path</th>
                <th data-column-id="parameters" data-sortable="false">Parameters</th>
            </tr>
        </thead>
    </table>





</body>
</html>