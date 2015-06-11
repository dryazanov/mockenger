<!DOCTYPE html>
<html>
<head>
    <title></title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/jquery.bootgrid.css"/>
    <script src="http://code.jquery.com/jquery-2.1.4.js"></script>
    <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script src="js/jquery.bootgrid.min.js"></script>
    <script src="js/jquery.bootgrid.fa.min.js"></script>
    <!--<link rel="stylesheet" href="/resources/demos/style.css">-->
    <script>
        $(function() {
            $( "#tabs" ).tabs();

            $("#grid-data").bootgrid({
                ajax: true,
                ajaxSettings: {
                    method: "GET",
                    cache: false
                },
                url: "http://localhost:8080/mockenger/group/5571f383ab6b8c9a859507a7/request-list",
                formatters: {
                    "id": function(column, row) {
                        return '<a href="#' + row.id + '">' + row.id +'</a>';
                    },
                    "link": function(column, row) {
                        return "<a href=\"#\">" + column.id + ": " + row.id + "</a>";
                    }
                }
            });
        });
    </script>
</head>
<body>

<div id="tabs">
  <ul>
    <li><a href="#tabs-1">REST</a></li>
    <li><a href="#tabs-2">SOAP</a></li>
  </ul>
  <div id="tabs-1">
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
  </div>
  <div id="tabs-2">
    <p>Morbi tincidunt, dui sit amet facilisis feugiat, odio metus gravida ante, ut pharetra massa metus id nunc. Duis scelerisque molestie turpis. Sed fringilla, massa eget luctus malesuada, metus eros molestie lectus, ut tempus eros massa ut dolor. Aenean aliquet fringilla sem. Suspendisse sed ligula in ligula suscipit aliquam. Praesent in eros vestibulum mi adipiscing adipiscing. Morbi facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut posuere viverra nulla. Aliquam erat volutpat. Pellentesque convallis. Maecenas feugiat, tellus pellentesque pretium posuere, felis lorem euismod felis, eu ornare leo nisi vel felis. Mauris consectetur tortor et purus.</p>
  </div>
</div>




</body>
</html>