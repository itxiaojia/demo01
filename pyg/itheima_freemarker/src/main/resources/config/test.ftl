<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门小DEMO </title>
</head>
<body>
<#--我只是一个注释，我不会有任何输出  -->
${name},你好。${message}
<br/>
<#--自定义变量-->
<#assign linkman="周大福">
${linkman}
<br/>
<#--自定义对象-->
<#assign user={'username':'张三丰','age':'121'}>
${user.username}年龄${user.age}岁了
<br/>
<#--条件判断-->
<#if success=true>
    这是个true
<#else>
    只是个false
</#if>
<br/>
<#--集合数据-->
<#list goodsList as goods>
    ${goods_index+1}:${goods.name}:${goods.price}元<br/>
</#list>
水果总数:${goodsList?size}
<br/>
<#--Freemarker引入别的模板:只要引入的模版中有表达式需要的参数,那么一定要给,否则报错-->
<#include "top.ftl">
<br/>
<#--json字符串转对象-->
<#assign info="{'username':'小花','age':'20'}">
<#assign userinfo=info?eval/>
${userinfo.username}:${userinfo.age}
<br/>
<#--日期格式化-->
当前日期:${date?date}<br/>
当前日期:${date?time}<br/>
当前日期:${date?datetime}<br/>
当前日期:${date?string("yyyy-MM-dd HH:mm:ss SSS")}
<br/>
<#--获取id :将数值类型转换为字符串-->
${goodsId?c}
<br/>
<#--数值非空判断 :!判断变量是否有值,如果有值,显示;如果没值,显示!号的值-->
${userName!"没有值"}
<br/>
<#--??判断username是否有值,如果有返回true-->
<#if userName??>
    username=${userName}
<#else>
    username=没有值
</#if>
</body>
</html>
