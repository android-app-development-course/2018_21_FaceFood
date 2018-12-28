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
	var postJson = {'post' : 'failed'};
	if(result) {
		postJson = {'post' : 'success'}; 
	}
	res.status(200).send(postJson);
}

function recommendCallback(result, req, res) {
	var recommendJson = [];
	if(result) {
		for(var i = 0; i < result.length; i++) {
			var recommandItem = {
				"image_path" : result[i]['image_path'],
				"image_description" : result[i]['image_description']
			}
			recommendJson.push(recommandItem);
		}
	}
	var sendJson = {"recommend" : recommendJson};
	res.status(200).send(sendJson);
}

function normalCallback(result, req, res) {
	var normalJson = [];
	var sendJson= {};
	if(result && result.length != 0) {
		for(var i = 0; i < result.length; i++) {
			var normalItem = {
				"id" : result[i]['id'],
				"nickname" : result[i]['nickname'], 
				"eat_place" : result[i]['eat_place'], 
				"date_time" : result[i]['date_time'], 
				"upup" : result[i]['upup'], 
				"downdown" : result[i]['downdown'],
				"content" : result[i]['content'],
				"images" : result[i]['image_path']
			}
			var json = JSON.parse(result[i]['image_path']);
			normalItem['image_path'] = "image/" + json[0];
			normalJson.push(normalItem);
		}
		sendJson['normal'] = normalJson;
		sendJson['hasNormal'] = "true";
	}
	else {
		sendJson['hasNormal'] = "false";
	}
	res.status(200).send(sendJson);
}

function getUpDownCallback(result, req, res) {
	console.log(result);
	var sendJson = {};
	if(result && result.length != 0) {
		sendJson['upOrDown'] = result[0]['upOrDown'];
		sendJson['hasRecord'] = "true";
	}
	else {
		sendJson['hasRecord'] = "false";
	}
	console.log(sendJson);
	res.status(200).send(sendJson);
}

function getCommentCallback(result, req, res) {
	console.log(result);
	var sendJson = {};
	if(result) {
		var commentList = [];
		for(var i = 0; i < result.length; i++) {
			var oneComment = {};
			oneComment['username'] = result[i]['nickname'];
			oneComment['commentContent'] = result[i]['commentContent'];
			commentList[i] = oneComment;
		}
		sendJson['get'] = 'success';
		sendJson['content'] = commentList;
	}
	else {
		sendJson['get'] = "failed";
	}
	console.log(sendJson);
	res.status(200).send(sendJson);
}

function postCommentCallback(result, req, res) {
	res.status(200).send();
}

function postUpDownCallback(result, req, res) {
	var sendJson = {};
	if(result) {
		sendJson['post'] = "success";
	}
	else {
		sendJson['post'] = "failed";
	}
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
	var sql = "select * from account where student_number = " + "\'" + req.body['account'] + "\'";
	dbQuery(conn, sql, loginCallback, req, res);
});

// signup
app.post('/createAccount', function(req, res) {

	var sql = "insert into account values (" + "\'" + req.body['student_number'] + "\'" + "," 
											 + "\'" + req.body['password'] + "\'" + ","
											 + "\'" + req.body['nickname'] + "\'" + ")";
	dbQuery(conn, sql, signupCallback, req, res);
});

// serve static file
app.use('/image', express.static('image'));

// get recommand content
app.post('/recommend', function(req, res) {
	var sql = "select * from recommend";
	dbQuery(conn, sql, recommendCallback, req, res);
});


app.post('/normal', function(req, res) {
	var offset = req.body['offset'];
	var sql = "select * from normal order by id desc limit " + offset + ", 5";
	dbQuery(conn, sql, normalCallback, req, res);
});

app.post('/myNormal', function(req, res) {
	console.log(req.body);
	var offset = req.body['offset'];
	var username = req.body['username'];
	var sql = "select * from normal where nickname = '" + username + "' order by id desc limit " + offset + ", 5";
	console.log(sql);
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

	var sql = "insert into normal values (null, " 
											 + "\'" + req.body['pictureCount'] + "\'" + "," 
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

app.post('/getUpDown', function(req, res) {
	console.log(req.body);
	var normalID = req.body['normalID'];
	var userID = req.body['userID'];
	var sql = "select * from updown where userID = " + userID + " and normalID = " + normalID;  
	console.log(sql);
	dbQuery(conn, sql, getUpDownCallback, req, res);
});

app.post('/getComment', function(req, res) {
	console.log(req.body);
	var normalID = req.body['normalID'];
	var sql = "select * from comment inner join account where comment.student_number = account.student_number and "
			  + "normalID = " + normalID;
	console.log(sql);
	dbQuery(conn, sql, getCommentCallback, req, res);
});

app.post('/postComment', function(req, res) {
	console.log(req.body);
	var normalID = req.body['normalID'];
	var student_number = req.body['student_number'];
	var commentContent = req.body['commentContent'];
	var sql = "insert into comment values (" + normalID + ", '" + student_number + "', '" + commentContent + "')";
	console.log(sql);
	dbQuery(conn, sql, postCommentCallback, req, res);
});

app.post('/postUpDown', function(req, res) {
	console.log(req.body);
	var isFirst = req.body['isFirst'];
	var entryExist = req.body['entryExist'];
	var normalID = req.body['normalID'];
	var userID = req.body['userID'];
	var upOrDown = req.body['upOrDown'];

	if(upOrDown == 2) {
		var sql = "delete from updown where normalID = " + normalID + " and userID = " + userID;
		conn.query(sql, function(err, result, fields){
			if(err) throw err;
		});
		sql = "update normal set upup = upup - 1 where id = " + normalID;
		conn.query(sql, function(err, result, fields){
			if(err) throw err;
		});
		res.status(200).send();
		return;
	}
	if(upOrDown == 3) {
		var sql = "delete from updown where normalID = " + normalID + " and userID = " + userID;
		conn.query(sql, function(err, result, fields){
			if(err) throw err;
		});
		sql = "update normal set downdown = downdown - 1 where id = " + normalID;
		conn.query(sql, function(err, result, fields){
			if(err) throw err;
		});
		res.status(200).send();
		return;		
	}

	var sql = "insert into updown values (" + normalID + ", " + userID + ", " +  upOrDown +") on duplicate key update upOrDown = " + upOrDown;
	dbQuery(conn, sql, postUpDownCallback, req, res);

	if(isFirst == "yes") return;
	var updateNormalSQL;
	if(entryExist == 1 && upOrDown == 1) {
		updateNormalSQL = "update normal set upup = upup + 1 , downdown = downdown - 1 where id = " + normalID;
	}
	else if(entryExist == 1 && upOrDown == 0){
		updateNormalSQL = "update normal set upup = upup - 1 , downdown = downdown + 1 where id = " + normalID;
	}
	else if(entryExist == 0 && upOrDown == 1) {
		updateNormalSQL = "update normal set upup = upup + 1 where id = " + normalID;
	}
	else {
		updateNormalSQL = "update normal set downdown = downdown + 1 where id = " + normalID;
	}
	conn.query(updateNormalSQL, function (err, result, fields) {
		if (err) throw err;
	});
});

app.get('/setUserInfo',function(req,res){
	param=require('url').parse(req.url,true).query
	if(!(param.student_id&&param.address&&param.profilePhotoAdd&&param.nickName&&param.gender)){
		return res.status(400).send('Lack necessary param.');
	}
	sql = "update account set nickname='"+param.nickName+"',address='"+param.address+"',gender="+param.gender+",profilePhotoAdd='"+param.profilePhotoAdd+"' where student_number='"+param.student_id+"';";
	dbQuery(conn,sql,function(request,req,res){
		res.status(200).send("done");
		return;
	},req,res);
	res.status(200);
})

app.get('/getUserInfo',function(req,res){
	param=require('url').parse(req.url,true).query
	if(!param.student_id){
		return res.status(400).send('Param student_id is necessary.');
	}
	// console.log(require('url').parse(req.url,true).query.student_id);
	var student_number=require('url').parse(req.url,true).query.student_id;
	var sql = "select * from account where student_number = \"" + student_number + "\";"; 
	dbQuery(conn, sql, 
		function(result,req,res){
			var normalJson = [];
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
