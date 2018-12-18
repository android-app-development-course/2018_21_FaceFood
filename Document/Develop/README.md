### Android注意事项

0. Gradle Build因为不能下载AsyncHttpClient加载不成功:

不知道为什么使用Gradle的下载链接不能下载，我已经手动下载到2018_21_FaceFood/MainPage/app/libs/__android-async-http-1.4.9.jar.test

如果你也是因为这个理由失败了，可以把__android-async-http-1.4.9.jar.test更名位android-async-http-1.4.9.jar，然后手动导入这个包即可。

1. 网络不可以在主线程中进行请求，要开多线程。

推荐解决方法：使用AsyncHttpClient来完成。
参考连接：https://github.com/codepath/android_guides/wiki/Using-Android-Async-Http-Client

一开始的解决方法：使用多线程和NetManager、JsonManager（自己写的两个类，不过使用别人写的异步网络请求类更好一些）来解决。

强烈建议使用组长推荐的AsyncHttpClient，非常好用！


### Nodejs使用注意事项

1, nodejs许多操作是异步执行的，虽然看上去是顺序的，但执行时异步，所以要注意这个问题。

比如，数据库查询操作。

解决方法：写一个回调函数，在查询出结果的时候，再执行想要的操作。

比如，我想要实现登录。那么我要用java去向服务器发送post请求，nodejs接收到请求之后，执行查询语句，但是不能再执行查询语句的后面加post的response，因为这样会导致数据库Query和response同时进行。而正确的执行路线是：查询后再response。

代码：

```
function loginCallback(result, req, res) {

	var loginJson = {'login' : 'fail'};

	if(result[0]['password_md5'] == req.body['md5']) 
	{
		loginJson = {'login' : 'success'};
	}
	
	res.status(200).send(loginJson);
}

function dbQuery(connection, sql, callback, req, res)
{
	connection.query(sql, function(err, result, fields) {
		callback(result, req, res);
	});
}


// login
app.post('/account', function(req, res) {

	console.log(req.body);
	var sql = "select * from account where student_number = " + "\'" + req.body['account'] + "\'";
	dbQuery(conn, sql, loginCallback, req, res);
});

```

2, 使用VSCode来本地开发

提高效率：https://blog.csdn.net/qq_31331027/article/details/80661819
