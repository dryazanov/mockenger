<!DOCTYPE html>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.dark.css">
    <!--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css">-->
    <link rel="stylesheet" href="css/jquery.bootgrid.css"/>
    <script src="http://code.jquery.com/jquery-2.1.4.js"></script>
    <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script src="js/jquery.bootgrid.min.js"></script>
    <script src="js/jquery.bootgrid.fa.min.js"></script>
    <script>
        $(function() {
            var data = {
                load: function(id, type) {
                    $("#" + id).bootgrid({
                        ajax: true,
                        ajaxSettings: {
                            method: "GET",
                            cache: false
                        },
                        url: "${pageContext.request.contextPath}/groups?type=" + type,
                        formatters: {
                            "id": function(column, row) {
                                return '<a href="${pageContext.request.contextPath}/groups/' + row.id + '/view">' + row.id +'</a>';
                            },
                            "recordingStarted": function(column, row) {
                                return row.recordingStarted == true ? "&#9679;" : "";
                            }
                        }
                    });
                }
            }

            $("#tabs").on("tabscreate", function(event, ui) {
                $(ui.tab[0]).addClass("active");
                var panel = $(ui.panel[0]);
                var type = panel.attr("value");
                var tableId = $("table", panel).attr("id");
                data.load(tableId, type);
            });

            $("#tabs").on("tabsactivate", function(event, ui) {
                $(ui.oldTab[0]).removeClass("active");
                $(ui.newTab[0]).addClass("active");
                var panel = $(ui.newPanel[0]);
                var type = panel.attr("value");
                var tableId = $("table", panel).attr("id");
                data.load(tableId, type);
            });

            $("#tabs").tabs();


        });
    </script>
</head>
<body style="padding:10px;">

<div>
    <div id="tabs">
      <ul class="nav nav-tabs">
        <li><a href="#tabs-1">REST</a></li>
        <li><a href="#tabs-2">SOAP</a></li>
      </ul>
      <div id="tabs-1" value="REST">
        <table id="grid-data-tab1" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-formatter="id">ID</th>
                    <th data-column-id="name">Name</th>
                    <th data-column-id="recordingStarted">On air</th>
                </tr>
            </thead>
        </table>
      </div>
      <div id="tabs-2" value="SOAP">
        <table id="grid-data-tab2" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-formatter="id">ID</th>
                    <th data-column-id="name">Name</th>
                    <th data-column-id="recordingStarted">On air</th>
                </tr>
            </thead>
        </table>
      </div>
    </div>
</div>




</body>
</html>