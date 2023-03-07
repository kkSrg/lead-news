<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
<b>普通文本 String 展示：</b><br><br>
Hello ${name} <br>
<hr>
<b>对象Student中的数据展示：</b><br/>
姓名：${stu.name}<br/>
年龄：${stu.age}
<hr>

<b>集合Student中的数据展示：</b><br/>
<#list stus as stu>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <tr>
        <td>${stu_index+1}</td>
        <td>${stu.name}</td>
        <td>${stu.age}</td>
        <td>${stu.money}</td>
    </tr>
    </table>
</#list>

<b>Map集合Student中的数据展示：</b><br/>
<#list stuMap ?keys as key>
<table>
  <tr>
      <td>序号</td>
      <td>姓名</td>
      <td>年龄</td>
      <td>钱包</td>
  </tr>
    <#if stuMap[key].age = 19 >
        <tr style="color: green">
        <td>${key_index}</td>
        <td>${stuMap[key].name}</td>
        <td>${stuMap[key].age}</td>
        <td>${stuMap[key].money}</td>
    <#else>
        <td>${key_index}</td>
        <td>${stuMap[key].name}</td>
        <td>${stuMap[key].age}</td>
        <td>${stuMap[key].money}</td>
        </tr>
    </#if>
</#list>
</table>
</body>
</html>