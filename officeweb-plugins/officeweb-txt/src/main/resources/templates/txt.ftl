<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <script src="codemirror5/lib/codemirror.js"></script>
    <link rel="stylesheet" href="codemirror5/lib/codemirror.css">
    <script src="codemirror5/mode/${source}"></script>
    <link rel="stylesheet" href="codemirror5/addon/display/fullscreen.css">
    <script src="codemirror5/addon/display/fullscreen.js"></script>
    <script src="js/jquery-3.0.0.min.js" type="text/javascript"></script>
    <script src="js/jquery.base64.js" type="text/javascript"></script>
    <#--    <link rel="stylesheet" href="theme/idea.css">-->
    <title>${name}</title>
</head>

<body>
</body>
<script type="text/javascript">
    CodeMirror(document.body, {
        value: $.base64.atob("${textData}", true, 'utf8'),
        //mode: "javascript",
        mode: "${mode}",
        //theme: "idea",
        fullScreen: true,
        lineNumbers: true,
        //readOnly: boolean|string
        //This disables editing of the editor content by the user. If the special value "nocursor" is given (instead of simply true), focusing of the editor is also disallowed.
        readOnly: "nocursor"
    });
</script>

</html>