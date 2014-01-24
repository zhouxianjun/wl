var fs = require('fs');
var url = require('url');  
var http = require('http');  
function download(file_url, file_name, cb, progress, path){
	var fileName = '';
	var savepath = '';
	if(!path || path == ''){
		var DOWNLOAD_DIR = 'files\\';  
		savepath = global.process.execPath;
		savepath = savepath.substring(0, savepath.lastIndexOf('\\') + 1);
		savepath = savepath + DOWNLOAD_DIR;
		fileName = file_name;
	}else{
		savepath = path.substring(0, path.lastIndexOf('\\') + 1);
		fileName = path.substring(path.lastIndexOf('\\') + 1);
	}
	fs.exists(savepath, function (exists) {
		if(!exists){
			fs.mkdir(savepath, 0777, function(){
				download_file_httpget(file_url);
			});
		}else{
			download_file_httpget(file_url);
		}
	});
	  
	// Function to download file using HTTP.get  
	var download_file_httpget = function(file_url) {  
		var isok = false;
		var file = fs.createWriteStream(savepath + fileName);  
		  
		var know = http.get(file_url, function(res) {
			if(res.statusCode == 200){
	    		isok = true;
	    		var tol = parseInt(res.headers['content-length']);
	    		var length = 0;
	    		file.on('close', function() {  
		            if(typeof cb === 'function'){
		            	cb(file);
		            }
		            console.log(fileName + ' downloaded to ' + savepath);  
		        });
	    		res.on('data', function(data) { 
	    			if(typeof progress === 'function'){
	    				length += data.length;
		            	var loaded = Math.ceil((length / tol) * 100)
		            	progress(loaded);
						if(loaded > 1){
							res.req.abort();
						}
		            }
		        });
				res.pipe(file);
	    	}else{
	    		isok = false;
	    		insertSystemMsg({
    				time: moment().format('YYYY-MM-DD HH:mm:ss'),
    				msg: '�ļ� '+file_name+'����ʧ��!',
    			});
	    	}
	    }).on('error', function(e) {
	    	  console.log("Got error: " + e.message);
	    });  
	}
}
download('http://localhost:62463/getFile?path=G:\\KanKan\\[电影天堂www.dy2018.com]独行侠BD中英双字.rmvb','dy',function(file){
	console.log(file.path);
},function(loaded){
	console.log(loaded + '%');
},'G:\\dy\\xx.rmvb');