function userLogin() {
    var userName=$("#userName").val();
    var userPwd=$("#userPwd").val();
    if(isEmpty(userName)){
        alert("用户名不能为空");
        return;
    }
    if (isEmpty(userPwd)){
        alert("用户密码不能为空");
        return;
    }
    //封装参数
    var params={};
    params.userName=userName;
    params.userPwd=userPwd;
    $.ajax({
        type:"post",
        url:ctx+"/user/userLogin",
        data:params,
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                //alert(data.msg);
                $.cookie("userName",data.result.userName);
                $.cookie("trueName",data.result.trueName);
                $.cookie("id",data.result.id);
                //实现页面的跳转
                window.location.href="main";
            }else{
                alert(data.msg);
            }
        }
    })
}