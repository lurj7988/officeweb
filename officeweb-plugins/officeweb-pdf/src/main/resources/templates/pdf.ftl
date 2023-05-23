<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>${name}</title>
</head>
<iframe width="100%" frameborder="0"></iframe>
<body>
</body>
<script type="text/javascript">
    const fm = document.getElementsByTagName("iframe")[0];
    fm.src = "pdfjs/web/viewer.html?file=" + encodeURIComponent('${pdfurl}');
    fm.height = window.document.documentElement.clientHeight - 20;
    /**
     * 页面变化调整高度
     */
    window.onresize = function () {
        fm.height = window.document.documentElement.clientHeight - 20;
    }
</script>