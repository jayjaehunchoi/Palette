//Express 기본 모듈 불러오기
var express = require('express');
var http = require('http');

var app = express();

// 기본 포트를 app 객체에 속성으로 설정
app.set('port', 5500);

app.use('/public', express.static(__dirname + '/public'));

app.use('/view', express.static(__dirname + '/view'));

app.get('/',function(req,res){
    let url = '/index.html';
    res.sendFile(__dirname + url);
});

app.get('/index.html',function(req,res){
  let url = '/index.html';
  res.sendFile(__dirname + url);
});

// Express 서버 시작
http.createServer(app).listen(app.get('port'), function(){
    console.log('익스프레스 서버를 시작했습니다. : ', + app.get('port'));
});