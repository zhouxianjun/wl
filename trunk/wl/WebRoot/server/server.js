var winston=require('winston');
var mysql = require('./dao/Mysql.js').instance();
var db = mysql.getConnection('wl');
var uuid = require('node-uuid');
var moment = require('moment');
var tlogout = {};

var logger = new (winston.Logger)({
    transports: [
        new (winston.transports.Console)(),
        new (winston.transports.File)({
            filename: 'wl_chat_server.log',
            timestamp: 'true',
            maxsize: 10485760,
            maxFiles: 10
        })
    ]/*,
    exceptionHandlers: [
        new winston.transports.File({ filename: 'nodejs_server_exception.log' })
    ]  */
});
var args = process.argv.splice(2);
var socketIoPort = 4567;
args.forEach(function (val, index, array) {
    var keyVal = val.split('=');
    if(keyVal.length = 2){
        var key = keyVal[0];
        var value = keyVal[1];
        if(key == 'socket_io_port' && !isNaN(value)){
            socketIoPort = parseInt(value);
        }
    }
});
logger.info('socket.io at ' + socketIoPort);
/*var express = require('express');
var app = express();
var http = require('http');
var server = http.createServer(app);*/
var io = require('socket.io').listen(socketIoPort);
/*
server.listen(socketIoPort);
app.configure(function(){
    console.log('in configure');
    app.use(express.methodOverride());
    app.use(express.bodyParser());
    //app.use(express.logger({stream: fs.createWriteStream('./Im.http.log', {"flags": "a" })}));
});
app.all('/*.(gzjs|html|css|js|jpg|png|gif){1}', function(req, res, next){
    var realpath = __dirname + url.parse(req.url).pathname;
    var ext = path.extname(req.url);
    if(ext == '.gzjs' || req.url.indexOf('.gzip.css') > 0){
        res.setHeader("Content-Encoding", "gzip");
    }
    fs.exists(realpath,function(exists){
        if(exists)
            res.end(fs.readFileSync(path.normalize(realpath)));
        else
            res.end('Cannot find request url: '+req.url);
    });
});*/

io.set('log level', 1);
io.set('close timeout', 10);
io.set('heartbeat interval', 5);
io.set('heartbeat timeout', 10);
io.sockets.on('connection', function (socket) {
    console.log("连接:" + socket.id);
    socket.on('addFriendOk', function(data){
    	var s = getSocket(data.id);
    	if(s){
    		data.friend.login.online = getSocket(data.friend.id) ? true : false;
    		s.emit('addFriendOk', data);
    	}
    });
    
    socket.on('delFriend', function(data){
    	var s = getSocket(data.to);
    	if(s){
    		s.emit('delFriend', data);
    	}
    });
    
    socket.on('rumble', function(data){
    	var s = getSocket(data.to);
    	if(s){
    		s.emit('rumble', data);
    	}
    });
    
    socket.on('pushFile', function(data){
    	var s = getSocket(data.to);
    	if(s){
    		s.emit('pushFile', data);
    	}
    });
    
    socket.on('pushFileOk', function(data){
    	var s = getSocket(data.to);
    	if(s){
    		s.emit('pushFileOk', data);
    	}
    });
    
    socket.on('pushFileCancel', function(data){
    	var s = getSocket(data.to);
    	if(s){
    		s.emit('pushFileCancel', data);
    	}
    });
    
    socket.on('updateInfo', function(data){
    	if(socket.wl.user)
    		socket.wl.user = data;
    	emitAll('updateInfo', data, socket);
    });
    
    socket.on('rtc', function(data){
    	if(data.chat.type == 2){
    		for ( var i = 0; i < data.chat.usersDto.length; i++) {
				if(data.chat.usersDto[i].id != socket.wl.user.id){
					var s = getSocket(data.chat.usersDto[i].id);
		    		if(s){
		        		s.emit('rtc', data);
		        	}
				}
			}
    	}else {
    		var s = getSocket(data.id);
    		if(s){
        		s.emit('rtc', data);
        	}
    	}
    });
    
    socket.on('rtcOk', function(data){
    	var s = getSocket(data.from.id);
    	if(s){
    		s.emit('rtcOk', data);
    	}
    });
    
    socket.on('rtcFail', function(data){
    	var s = getSocket(data.from.id);
    	if(s){
    		s.emit('rtcFail', data);
    	}
    });
    
    socket.on('rtcClose', function(data){
    	if(data.chat.type == 2){
    		for ( var i = 0; i < data.chat.usersDto.length; i++) {
				if(data.chat.usersDto[i].id != socket.wl.user.id){
					var s = getSocket(data.chat.usersDto[i].id);
			    	if(s){
			    		s.emit('rtcClose', data);
			    	}
				}
			}
    	}else {
    		var s = getSocket(data.id);
        	if(s){
        		s.emit('rtcClose', data);
        	}
    	}
    });
    
    socket.on('addFriendFail', function(data){
    	var s = getSocket(data.id);
    	if(s){
    		s.emit('addFriendFail', data);
    	}
    });
    
    socket.on('push', function (data) {
    	if(!socket.wl)return;
    	data.from = data.from || socket.wl.user;
    	data.date = moment().format();
    	data.date = new Date();
    	try {
    		if(data.chat.type == 2){
    			for ( var i = 0; i < data.chat.usersDto.length; i++) {
    				if(data.chat.usersDto[i].id != socket.wl.user.id || data.type == 0){
    					data.id = data.chat.usersDto[i].id;
        				insertMsg(data);
    				}
				}
    		}else{
    			insertMsg(data);
    		}
		} catch (e) {
			logger.log('error',e);
		}
    });
    socket.on('addFriend', function (data) {
        try{
            db.query('select id from wl_message where type = 3 and state = 0 and fromuser_id = ? and touser_id = ?',[socket.wl.user.id, data.id],function(err, results, fields){
                if(err){
                    logger.log('error',err);
                    socket.emit('addFriend', {
                    	success: false
                    });
                }else{
                	if(results && results[0] && results[0].id){
                		db.query('update wl_message set date = ?, content = ?, state = 0 where id = ?',[new Date(), data.content, results[0].id],function(err, results, fields){
        	                if(err){
                                logger.log('error',err);
        	                    socket.emit('addFriend', {
        	                    	success: false
        	                    });
        	                }else{
        	                	socket.emit('addFriend', {
        	                    	success: true
        	                    });
        	                	var s = getSocket(data.id);
        	                	if(s){
        	                		s.emit('addFriendV', {
        	                			fromuser_id: socket.wl.user.id,
        	                			touser_id: data.id,
        	                			content: data.content
            	                    });
        	                	}
        	                }
        	            });
                	}else{
                		db.query('insert into wl_message values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)',[uuid(), data.content, new Date(), null, 0, null, 3, null, socket.wl.user.id, data.id],function(err, results, fields){
        	                if(err){
                                logger.log('error',err);
        	                    socket.emit('addFriend', {
        	                    	success: false
        	                    });
        	                }else{
        	                	var s = getSocket(data.id);
        	                	if(s){
        	                		s.emit('addFriendV', {
        	                			fromuser_id: socket.wl.user.id,
        	                			touser_id: data.id,
        	                			content: data.content
            	                    });
        	                	}
        	                }
        	            });
                	}
                }
            });
        }catch (e){
            logger.error(e);
            socket.emit('addFriend', {
            	success: false
            });
        }
    });
    socket.on('login', function (data) {
        try{
        	db.query('select login_id from wl_user where id = ?',[data.id],function(err, results, fields){
    	        if(err || !results[0]){
                    logger.log('error',err);
    	            socket.emit('login', {
                    	success: false
                    });
    	        }else{
    	        	db.query('update wl_area set city = ?, citySN = ?, country = ?, countrySN = ?, ip = ?, isp = ?, province = ?, provinceSN = ? where login_id = ?',[
    	        	    data.ipinfo.city,
    	        	    data.ipinfo.city_id,
    	        	    data.ipinfo.country,
    	        	    data.ipinfo.country_id,
    	        	    data.ipinfo.ip,
    	        	    data.ipinfo.isp,
    	        	    data.ipinfo.region,
    	        	    data.ipinfo.region_id,
    	        	    results[0].login_id
        	        ]);
    	        	db.query('update wl_login set online = 1, lastIp = ?, lastLogin = ? where id = ?',[data.ipinfo.ip, new Date(), results[0].login_id],function(err, results, fields){
    	                if(err){
                            logger.log('error',err);
    	                    socket.emit('login', {
    	                    	success: false
    	                    });
    	                }else{
    	                	close(data);
    	                	socket.wl = {
	                    		user: data
	                    	};
    	                	socket.emit('login', {
    	                    	success: true
    	                    });
    	                	socket.wl.user.login.online = true;
    	                	emitAll('online', socket.wl.user, socket);
    	                }
    	            });
    	        }
    	    });
        }catch (e){
            logger.error(e);
        }
    });
    socket.on('disconnect', function(){
    	if(socket.wl && socket.wl.user && socket.wl.user.id && !tlogout[socket.wl.user.id]){
    		socket.wl.user.login.online = false;
    		emitAll('logout', socket.wl.user, socket);
    		logout(socket.wl.user.id);
    	}
    	if(socket.wl)
    		delete tlogout[socket.wl.user.id];
        delete io.sockets.sockets[socket.id];
    });
});
function insertMsg(data){
	db.query('insert into wl_message values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)',[uuid(), data.content, data.date, data.file || null, 0, data.style || null, (typeof data.type == 'undefined' ? data.chat.type : data.type), data.chat.id, data.from.id, data.id],function(err, results, fields){
        if(err){
            logger.log('error',err);
            socket.emit('pushError', err);
        }
    });
	var s = getSocket(data.id);
	if(s){
		s.emit('push', data);
	}
}
function close(data){
	for ( var key in io.sockets.sockets) {
		var s = io.sockets.sockets[key];
		if(s){
			if(s && s.wl){
				if(s.wl.user.id == data.id){
					tlogout[data.id] = s.wl.user;
					s.emit('close', data);
					s.disconnect();
					delete io.sockets.sockets[key];
				}
			}
		}
	}
}
function emitAll(name, data, me){
	for ( var key in io.sockets.sockets) {
		var s = io.sockets.sockets[key];
		if(s){
			if(me && me.wl && s && s.wl){
				if(me.wl.user.id != s.wl.user.id)
					s.emit(name, data);
			}else{
				s.emit(name, data);
			}
		}
	}
}
function logout(id){
	try {
		db.query('select login_id from wl_user where id = ?',[id],function(err, results, fields){
	        if(err){
	            logger.log('error',err);
	            setTimeout(function(){
	            	logout(id);
	            },3000);
	        }else{
	        	db.query('update wl_login set online = 0 where id = ?',[results[0].login_id],function(err, results, fields){
	                if(err){
	                    logger.log('error',err);
	                    setTimeout(function(){
	                    	logout(id);
	                    },3000);
	                }
	            });
	        	db.query('update wl_login l set l.totalOnlineHour = (l.totalOnlineHour + hour(timediff(now(), l.lastLogin))) where l.id = ?',[results[0].login_id]);
	        }
	    });
	} catch (e) {
		logger.log('error',e);
	}
}
function getSocket(id){
	for(var key in io.sockets.sockets){
        var socket = io.sockets.sockets[key];
        if(socket.wl && socket.wl.user && socket.wl.user.id == id){
        	return socket;
        }
    }
	return false;
}
function getConnections(){
    var sz = new Array();
    for(var key in io.sockets.sockets){
        sz.push(key);
    }
    console.log(sz);
    return sz;
}
//过虑掉字符串首尾格式，替换字符串中的多个空格为一个空格
function trim(s){
    return s.replace(/(^\s*)|(\s*$)/g, '').replace(/\s+/g,' ');
}