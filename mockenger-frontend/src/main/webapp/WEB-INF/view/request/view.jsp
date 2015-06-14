<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html xmlns:form="http://www.w3.org/1999/xhtml" xmlns:c="http://www.springframework.org/schema/util">
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

        });
    </script>
</head>
<body style="padding:10px;">


<div class="col-lg-6">
    <div class="well bs-component">
        <form:form class="form-horizontal" modelAttribute="requestEntity" action="${pageContext.request.contextPath}/requests/${requestId}/save">
            <form:hidden path="id"/>
            <fieldset>
                <legend>Legend</legend>
                <div class="form-group">
                    <label for="inputName" class="col-lg-2 control-label">Name</label>

                    <div class="col-lg-10">
                        <form:input path="name" cssClass="form-control" id="inputName"/>
                        <!--<span class="input-group-btn">
                        <div class="btn-group">
                              <a href="#" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                Dropdown
                                <span class="caret"></span>
                              </a>
                      <ul class="dropdown-menu">
                        <li><a href="#">Dropdown link</a></li>
                        <li><a href="#">Dropdown link</a></li>
                        <li><a href="#">Dropdown link</a></li>
                      </ul>
                      </div>
                          </span>
                          </div>-->
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputPath" class="col-lg-2 control-label">Request path</label>

                    <div class="col-lg-10">
                        <form:input path="path.value" cssClass="form-control" id="inputPath"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="selectMethod" class="col-lg-2 control-label">Request method</label>

                    <div class="col-lg-10">
                        <form:select path="method" items="${requestMethods}" class="form-control" id="selectMethod" />
                    </div>
                </div>
                <hr/>

                <c:forEach items="${requestEntity.headers.values}" var="headerValue">
                    <c:set var="hd" value="${headerValue.key}"/>
                    <div class="form-group">
                        <label for="inputDate" class="col-lg-2 control-label">${hd}</label>

                        <div class="col-lg-10">
                            <form:input path="headers.values[${hd}]" cssClass="form-control"/>
                        </div>
                    </div>
                </c:forEach>
                <hr/>

                <c:forEach items="${requestEntity.parameters.values}" var="paramValue">
                    <c:set var="pv" value="${paramValue.key}"/>
                    <div class="form-group">
                        <label for="inputDate" class="col-lg-2 control-label">${pv}</label>

                        <div class="col-lg-10">
                            <form:input path="parameters.values[${pv}]" cssClass="form-control"/>
                        </div>
                    </div>
                </c:forEach>
                <hr/>

                <div class="form-group">
                    <label for="textareaRequest" class="col-lg-2 control-label">Request body</label>

                    <div class="col-lg-10">
                        <form:textarea path="body.value" cssClass="form-control" rows="20" id="textareaRequest"></form:textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label for="textareaResponse" class="col-lg-2 control-label">Request body</label>

                    <div class="col-lg-10">
                        <form:textarea path="response.responseBody" cssClass="form-control" rows="20" id="textareaResponse"></form:textarea>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-10 col-lg-offset-2">
                        <button type="reset" class="btn btn-default">Cancel</button>
                        <input type="submit" class="btn btn-primary" />
                    </div>
                </div>
            </fieldset>
        </form:form>
    </div>
</div>


</body>
</html>