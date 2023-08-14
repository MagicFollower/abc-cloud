<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>优惠劵</title>
    <style>
        .bg {
            position: relative;
            float: left;
            width: 252px;
            height: 140px;
        }
    </style>
</head>
<body style="font-family: Microsoft YaHei;">
<#list aaaDtlList as item>
    <div class="bg" >
        <div style="position: absolute;left: 22px;bottom: 15px;">
            <div style="font-size: 5px">商户编码:${item.aaaCode!""}</div>
            <div style="font-size: 5px">商户名称:${item.aaaName!""}</div>
            <div style="font-size: 5px">备注:${item.memo!""}</div>
        </div>
    </div>
</#list>
</body>
</html>