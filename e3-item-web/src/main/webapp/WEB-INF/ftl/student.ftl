<html>
	<head>
		<title>student</title>
	</head>
	<body>
		学生信息:<br/>
		学号:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
		姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
		年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
		住址:${student.addr}&nbsp;&nbsp;&nbsp;&nbsp;<br/>
		
		学生列表:<br/>
		<table border="1">
			<tr>
				<th>编号</th>
				<th>学号</th>
				<th>姓名</th>
				<th>年龄</th>
				<th>住址</th>
			</tr>
			
			<#list stuList as student>
			<#if student_index % 2 == 0>
				<tr bgcolor="red">
			<#else>
				<tr bgcolor="blue">
			</#if>				
					<td>${student_index}</td>
					<td>${student.id}</td>
					<td>${student.name}</td>
					<td>${student.age}</td>
					<td>${student.addr}</td>
				</tr>
			</#list>
		</table><br/>
		
		日期:${date?date}<br/>
		日期:${date?time}<br/>
		日期:${date?datetime}<br/>
		日期:${date?string("yyyy/MM/dd HH:mm:ss")}<br/>
		
		null值处理测试:${val!"val值为null"}<br/>
		
		判断null值:
		<#if val??>
			val有值
		<#else>
			val值为null
		</#if></br>
		
		include测试:
		<#include "hello.ftl">
	</body>
</html>