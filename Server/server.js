var express = require('express');
var jsonParser = require('body-parser');
var mysql = require('mysql');
var app = express();

var conn = mysql.createConnection({
	host: "localhost",
	user: "root",
	password: "Orz;)2333",
	database: "accountDB"
});

function loginCallback(result, req, res) {

	var loginJson;

	if(result.length == 0)
	{
		loginJson = {'login' : 'fail'};
	}
	else if(result[0]['password_md5'] == req.body['md5']) 
	{
		loginJson = {'login' : 'success'};
	}
	else 
	{
		loginJson = {'login' : 'fail'};
	}
	
	res.status(200).send(loginJson);
}

function dbQuery(connection, sql, callback, req, res)
{
	connection.query(sql, function(err, result, fields) {
		callback(result, req, res);
	});
}

app.use(jsonParser.json());

app.post('/account', function(req, res) {

	console.log(req.body);
	var sql = "select * from account where student_number = " + "\'" + req.body['account'] + "\'";
	dbQuery(conn, sql, loginCallback, req, res);
});

app.get('/test', function(req, res) {
	res.send("test");
});

app.listen(80);
