function login() {
	$("#logining").attr("disabled", true);
	$("#logining").attr("src", basePath + "/images/btn_logining.gif");
	if ($('#userName').val() == '') {
		layer.tips('请输入帐户名', '#userName');
		$("#logining").attr("disabled", false);
		$("#logining").attr("src", basePath + "/images/btn_login.gif");
		return;
	}
	if ($('#upassword').val() == '') {
		layer.tips('请输入密码', '#upassword');
		$("#logining").attr("disabled", false);
		$("#logining").attr("src", basePath + "/images/btn_login.gif");
		return;
	}
	if ($('#randomCode').val() == '') {
		layer.tips('请输入验证码', '#randomCode');
		$("#logining").attr("disabled", false);
		$("#logining").attr("src", basePath + "/images/btn_login.gif");
		return;
	}
	$.ajax({
		type : "POST",
		url : basePath + "/doLogin",
		data : $('#form1').serialize(),//FormId
		error : function(request) {
			layer.alert('登录出现异常', {icon: 5});
			$('#checkCode').attr("src",basePath+'/loginCodeServlet?date='+new Date());
			$('#randomCode').val('');
		},
		success : function(data) {
			if (data.code == 'SUCCESS') {
				window.location.href = basePath + "/index";
			} else {
				$("#logining").attr("disabled", false);
				$("#logining").attr("src",basePath + "/images/btn_login.gif");
				layer.alert(data.message, {icon: 5});
				$('#checkCode').attr("src",basePath+'/loginCodeServlet?date='+new Date());
				$('#randomCode').val('');
			}
		}
	});
}