<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>SSO登录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <script src="/resource/js/jquery-2.2.3.min.js"></script>

</head>
<body>
<div class="logo">
    <h2>SSO 登录</h2>
</div>

<div class="login">
    <form id="login">
        <div>
            <label>
                用户名 : <input name="username">
            </label>
        </div>
        <div style="margin-top: 20px">
            <label>
                密码 : <input name="password" type="password">
            </label>
        </div>
        <input type="hidden" name="redirectUrl" value="${redirectUrl!}">

    </form>
    <button  style="margin-top: 20px" onclick="submitLogin()">登录</button>
</div>
</body>
<script>
    function submitLogin(){
        $.ajax({
            url:"/login",
            method:"POST",
            data:$("#login").serialize(),
            success:function(data){
                if(data.status==="success"){
                    location.href=data.returnUrl;
                }else{
                    alert(data.message);
                }
            }
        })
    }
</script>


<style>
    body{
        font-family: "PingFang SC","Microsoft YaHei";
        margin: 0;
        padding: 0;
        background-color: #f7f8f9;
    }

    .logo{
        margin: 10% auto 0 auto;width: 300px;text-align: center;color: #303133
    }

    .login{
        margin: 50px auto 0 auto;width: 300px;text-align: center
    }
</style>
</html>