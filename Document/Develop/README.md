### Androidע������

1, ���粻���������߳��н�������Ҫ�����̡߳�

�Ƽ����������ʹ��AsyncHttpClient����ɡ�
�ο����ӣ�https://github.com/codepath/android_guides/wiki/Using-Android-Async-Http-Client

һ��ʼ�Ľ��������ʹ�ö��̺߳�NetManager��JsonManager���Լ�д�������࣬����ʹ�ñ���д���첽�������������һЩ���������


### Nodejsʹ��ע������

1, nodejs���������첽ִ�еģ���Ȼ����ȥ��˳��ģ���ִ��ʱ�첽������Ҫע��������⡣

���磬���ݿ��ѯ������

���������дһ���ص��������ڲ�ѯ�������ʱ����ִ����Ҫ�Ĳ�����

���磬����Ҫʵ�ֵ�¼����ô��Ҫ��javaȥ�����������post����nodejs���յ�����֮��ִ�в�ѯ��䣬���ǲ�����ִ�в�ѯ���ĺ����post��response����Ϊ�����ᵼ�����ݿ�Query��responseͬʱ���С�����ȷ��ִ��·���ǣ���ѯ����response��

���룺

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

2, ʹ��VSCode�����ؿ���

���Ч�ʣ�https://blog.csdn.net/qq_31331027/article/details/80661819
