<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>500</title>
    <link rel="stylesheet" href="css/error.css">
</head>

<body>
<div class="error-main">

    <div class="error-img-box">
        <img class="absolute-center error-img" src="images/500.png" alt="">
    </div>
    <div class="clearfix error-main-container">
        <div class="absolute-center info-content">
            <div class="error-title ">服务器错误</div>
            <div class="error-words">
<#--                <p>出现了问题，服务器无法处理您的处理请求，我们正在修复它，请过会再来。</p>-->
                <p>${message}</p>
                <#--<div class="error-search-box mt-10">
                    <label>
                        <input placeholder="搜索功能" class="error-search-input" type="text"/>
                    </label>
                    <div class="error-search"></div>
                </div>-->
            </div>
            <#--<div class="error-home" @click="toHome">
                回到首页
            </div>-->
        </div>
    </div>

    <div class="error-text-record text-center">Copyright © Epoint support
        <span class="error-mark">0512-58188000</span>
    </div>
</div>
</body>

</html>
