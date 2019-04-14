<!DOCTYPE html>
<html>
<head>
        
    <meta
             charset="utf‐8">
        <title>Hello World!</title>
</head>
<body>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
        <td>出生日期</td>

    </tr>
  <#list stus as stu>
    <tr>
        <td>${stu_index+1}</td>
        <td <#if stu.name=='小明'>style="background:red;"</#if>>${stu.name}</td>
        <td>${stu.age}</td>
            <td <#if stu.money gt 300>style="background: brown" </#if>${stu.money}</td>
        <td>${stu.birthday?date}</td>

    </tr>

  </#list>
    <br>
    姓名:${stuMap['stu1'].name}
    <br>
    年龄:${stuMap.stu1.age}
    <br>
    遍历map数据
    <#list stuMap?keys as key>
        <tr>
            <td>${stuMap[key].name}</td>
            <td>${stuMap[key].age}</td>
            <td>${stuMap[key].money}</td>
        </tr>
    </#list>

</table>
<br/>
<#--assign定义变量,使用?eval将json字符串转换为对象-->
<#assign text="{'bank':'工商银行','account':'10101920201920212'}"/>
    <#assign data=text?eval />
开户行：${data.bank}  账号：${data.account}
</body>
</html>