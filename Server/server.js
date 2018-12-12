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

	var loginJson = {'login' : 'fail'};

	if(result[0]['password_md5'] == req.body['md5']) 
	{
		loginJson = {'login' : 'success'};
	}
	
	res.status(200).send(loginJson);
}

function signupCallback(result, req, res) {

	var signupJson = {'signup' : 'failed'};

	if(result) {
		signupJson = {'signup' : 'success'}; 
	}

	res.status(200).send(signupJson);
}

function firstContentCallback(result, req, res) {

	var recommandJson = [];

	console.log(result[0]);

	if(result) {

		for(var i = 0; i < result.length; i++) {

			var recommandItem = {
				"image_path" : result[i]['image_path'],
				"image_description" : result[i]['image_description']
			}

			recommandJson.push(recommandItem);
		}
	}

	var sendJson = {"recommand" : recommandJson};

	res.status(200).send(sendJson);
}

function normalCallback(result, req, res) {

	var normalJson = [];

	console.log(result[0]);

	if(result) {

		for(var i = 0; i < result.length; i++) {

			var normalItem = {
				"image_path" : result[i]['image_path'], 
				"nickname" : result[i]['nickname'], 
				"eat_place" : result[i]['eat_place'], 
				"date_time" : result[i]['date_time'], 
				"upup" : result[i]['upup'], 
				"downdown" : result[i]['downdown']
			}

			normalJson.push(normalItem);
		}

	}

	var sendJson = {"normal" : normalJson};

	res.status(200).send(sendJson);
}

function dbQuery(connection, sql, callback, req, res)
{
	connection.query(sql, function(err, result, fields) {
		callback(result, req, res);
	});
}

app.use(jsonParser.json());

// login
app.post('/account', function(req, res) {

	console.log(req.body);
	var sql = "select * from account where student_number = " + "\'" + req.body['account'] + "\'";
	dbQuery(conn, sql, loginCallback, req, res);
});

// signup
app.post('/createAccount', function(req, res) {

	console.log(req.body);
	var sql = "insert into account values (" + "\'" + req.body['student_number'] + "\'" + "," 
											 + "\'" + req.body['password'] + "\'" + ","
											 + "\'" + req.body['nickname'] + "\'" + ")";
	console.log(sql);
	dbQuery(conn, sql, signupCallback, req, res);
});

// serve static file
app.use('/image', express.static('image'));

// get recommand content
app.post('/firstRecommand', function(req, res) {
	
	console.log(req.body);
	var sql = "select * from recommand";
	dbQuery(conn, sql, firstContentCallback, req, res);
});


app.post('/normal', function(req, res) {

	console.log(req.body);
	var sql = "select * from normal";
	dbQuery(conn, sql, normalCallback, req, res);
});

app.post('/firstNormal', function(req, res) {

	console.log(req.body);
	var sql = "select * from normal";
	dbQuery(conn, sql, normalCallback, req, res);
});

app.listen(80);