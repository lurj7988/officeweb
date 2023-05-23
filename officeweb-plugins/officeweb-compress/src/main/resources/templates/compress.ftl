<!DOCTYPE html>
<html style="height: 100%" lang="en">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <title>${name}</title>
    <link rel="stylesheet" href="layui/css/layui.css"/>
    <script src="js/jquery-3.0.0.min.js" type="text/javascript"></script>
    <script src="js/jquery.base64.js" type="text/javascript"></script>
</head>

<body style="height: 100%">
<div class="layui-fluid" style="height: 100%">
    <div class="layui-row layui-col-space15" style="height: 100%">
        <!-- 左树 -->
        <div class="layui-col-sm12 layui-col-md4 layui-col-lg2" style="height: 100%">
            <div class="layui-card" style="height: 100%">
                <div class="layui-card-body mini-bar" id="ltTree"></div>
            </div>
        </div>
        <!-- 右表 -->
        <div class="layui-col-sm12 layui-col-md8 layui-col-lg10" style="height: 100%">
            <div class="layui-card" style="height: 100%">
                <iframe id="iframe" style="width: 100%; height: 100%; border-width: 0"></iframe>
            </div>
        </div>
    </div>
</div>
<!-- body 末尾处引入 layui -->
<script src="layui/layui.js"></script>
<script>
    layui.use(["tree", "util"], function () {
        const $ = layui.jquery;
        const tree = layui.tree;
        const md5 = '${md5}';
        const supports = '${supports}'
        // 树形渲染
        tree.render({
            elem: "#ltTree",
            data: ${filetree},
            click: function (obj) {
                // 只有叶子节点才可以跳转，也有可能是空文件夹
                if (obj.data.children.length === 0) {
                    if (obj.data.title) {
                        const name = obj.data.title;
                        const suffix = name.substring(name.lastIndexOf(".") + 1);
                        if (supports.indexOf(suffix) > 0) {
                            window.open('/compress?md5=' + md5 + '&id=' + obj.data.id);
                        } else {
                            $("#iframe").attr('src', '/compress?md5=' + md5 + '&id=' + obj.data.id);
                        }
                    }
                }
            }
        });
    });
</script>
</body>

</html>