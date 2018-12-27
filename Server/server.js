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
		loginJson = {'login' : 'success', 'nickname' : result[0]['nickname']};
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

function postNormalCallback(result, req, res) {

	console.log(result);

	var postJson = {'post' : 'failed'};

	if(result) {
		postJson = {'post' : 'success'}; 
	}

	res.status(200).send(postJson);
}

function firstContentCallback(result, req, res) {

	var recommandJson = [];

	if(result) {

		for(var i = 0; i < result.length; i++) {

			console.log(result[i]);
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

	// console.log(result[0]);

	if(result) {
		for(var i = 0; i < result.length; i++) {
			var normalItem = {
				// "image_path" : result[i]['image_path'], 
				"nickname" : result[i]['nickname'], 
				"eat_place" : result[i]['eat_place'], 
				"date_time" : result[i]['date_time'], 
				"upup" : result[i]['upup'], 
				"downdown" : result[i]['downdown']
			}
			var json = JSON.parse(result[i]['image_path']);
			normalItem['image_path'] = "image/" + json[0];
			// console.log(normalItem);
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
	// console.log(sql);
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

app.post('/postNormal', function(req, res) {
	console.log(req.body);
	
	var picNames = req.body['picNames'];
	for(var i = 0; i < picNames.length; i++) {
		picNames[i] = "\"" +  picNames[i] + "\"";
	}

	var list = '[' + picNames.join(", ") + ']';
	console.log(list);

	var sql = "insert into normal values (" + "\'" + req.body['pictureCount'] + "\'" + "," 
											 + "\'" + list + "\'" + "," 
											 + "\'" + req.body['username'] + "\'" + ","
											 + "\'" + req.body['location'] + "\'" + ","
											 + "\'" + req.body['time'] + "\'" + ","
											 + "\'" + req.body['content'] + "\'" + ","
											 + "\'" + 0 + "\'" + ","
											 + "\'" + 0 + "\'" + ")";
	console.log(sql);
	dbQuery(conn, sql, postNormalCallback, req, res);
});

app.get('/test',function(req,res){
	res.status(200).send("test");
});

app.get('/setUserInfo',function(req,res){
	param=require('url').parse(req.url,true).query
	if(!(param.student_id&&param.address&&param.profilePhotoAdd&&param.nickName&&param.gender)){
		return res.status(400).send('Lack necessary param.');
	}
	sql = "update account set nickname='"+param.nickName+"',address='"+param.address+"',gender="+param.gender+",profilePhotoAdd='"+param.profilePhotoAdd+"' where student_number='"+param.student_id+"';";
	console.log(sql);
	dbQuery(conn,sql,function(request,req,res){
		console.log(request);
		res.status(200).send("done");
		return;
	},req,res);
	res.status(200);
})

app.get('/getUserInfo',function(req,res){
	console.log(req.body);
	param=require('url').parse(req.url,true).query
	if(!param.student_id){
		return res.status(400).send('Param student_id is necessary.');
	}
	console.log(require('url').parse(req.url,true).query.student_id);
	var student_number=require('url').parse(req.url,true).query.student_id;
	var sql = "select * from account where student_number = \"" + student_number + "\";"; 
	dbQuery(conn, sql, 
		function(result,req,res){
			var normalJson = [];

			console.log(result[0]);
		
			if(result) {
		
				for(var i = 0; i < result.length; i++) {
		
					var normalItem = {
						"nickName":result[i]['nickname'],
						"address":result[i]['address'],
						"gender":result[i]['gender'],
						"profilePhotoAdd":result[i]['profilePhotoAdd'],
					}
					normalJson.push(normalItem);
				}
		
			}
		
			var sendJson = {"data" : normalJson};
		
			res.status(200).send(sendJson);
	},
	req, res);
}
);

var fileupload = require("express-fileupload");
app.use(fileupload());
app.post("/upload", function(req, res)
{
    console.log("===");
    console.log("Begin receving file");
    var file;
	
	var student_number=req.body.student_number;

    if(!req.files)
    {
        res.send("File was not found");
		console.log("File not sent");
		return res.status(400).send('No files were uploaded.');
    }
    else{
		console.log("receving file:");
		console.log(req.files.file);
    }

    file = req.files.file; 
	sql = "update account set profilePhotoAdd='image/"+file.name+"' where student_number='"+student_number+"';";
	console.log(sql);

    file.mv("./image/"+file.name, function(err)  //Obvious Move function
        {
		if(err){
			console.log("Error when moving");
			console.log(err);
			return res.status(400).send('No files were uploaded.');
		}
		});

	dbQuery(conn,sql,function(res,req,res){;},req,res);

	res.send("File Uploaded");
	res.status(200).send();

});

app.listen(80);
