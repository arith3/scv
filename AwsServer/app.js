const express = require('express');
const app = express();
var EventEmitter = require('events');
var custom_object = new EventEmitter();
var crypto = require('crypto');
var mysql      = require('mysql');  
var connection = mysql.createConnection({  
  host     : 'localhost',  
  user     : 'root',  
  password : 'scv1234',  
  database : 'test'  
});
var listTemp=100;
var salt = Math.round((new Date().valueOf() *Math.random()))+"";  
connection.connect(function(err){  
if(!err) {  
    console.log("Database is connected ... \n\n");    
} else {  
    console.log("Error connecting database ... \n\n");    
}  
}); 
let users = [
  {
    id: 1,
    name: 'alice'
  },
  {
    id: 2,
    name: 'bek'
  },
  {
    id: 3,
    name: 'chris'
  }
]

app.get('/users', (req, res) => {
   console.log('who get in here/users');
   res.json(users)
});
app.post('/list', (req, res) => {
   var inputData;
   var temp = "0";
   var temp1 = "1";
   var temp2 = "2";
   var detectChange;
   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });
   req.on('end', () => {
     console.log("list_Num : "+inputData.user_id);

   console.log("temp :" +temp);
   detectChange=listTemp;
   if(inputData.user_id == temp)
   {
	console.log("log -- 0000");
	listTemp=0;
	res.write("0");
   }
   else if(inputData.user_id == temp1)
   {
	console.log("log -- 1111"); 
	listTemp=1;
	res.write("1");
   }
   else if(inputData.user_id == temp2)
   {
	console.log("log -- 2222");
	listTemp=2;
	res.write("2");
   }
   res.end();
  
   console.log("listTemp= " +listTemp);
 });
});
app.post('/server', (req, res) => {
   var inputData;
   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });
   req.on('end', () => {
     console.log("user_id : ");
     if(listTemp==0)
     {
	res.write("00");
	res.end();
     }
     else if(listTemp==1)
     {
	res.write("11");
	res.end();
     }
     else if(listTemp==2)
     {
	res.write("22");
	res.end();
     }
   });
   
   


});
app.post('/register', (req, res) => {
   console.log('who get in here register /users');
   var inputData;
   var temp=0;
   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });
   req.on('end', () => {
     console.log("user_id : "+inputData.user_id + " , pw : "+inputData.name);
   });
   
   connection.query('SELECT * from USER', function(err, rows, fields) { 
   
   if (!err){
       for (var i in rows){ 
        if(rows[i].userID == inputData.user_id)
        {
           console.log('used');
           temp=1;

        }
	else
	{
		
		console.log('not used');
	}
       }
	if(temp==0)
        {
		var hashpass = crypto.createHash("sha512").update(inputData.name+salt).digest("hex");
                connection.query('INSERT INTO USER(userID,userPassword,userName,userAge) VALUES (?, ?, ?, ?)', [inputData.user_id,hashpass, salt, inputData.age], function(err, rows, fields) {
		console.log('sex3');
               	if(!err)
                {
                        console.log('sex1');
			res.write("success");
			res.end();
		}
		else
		{
			console.log('sex4');
		}

	});
	}
	else
	{
		console.log('sex2');
		res.write("failed");
		res.end();
	}
      }
   });
	
	
	console.log('temp'+ temp);
	console.log('this2');
       temp=0; 


});   
app.post('/post', (req, res) => {
   console.log('who get in here post /users');
   var inputData;
   console.log('jt');
   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });

   req.on('end', () => {
     console.log("user_id : "+inputData.user_id + " , pw : "+inputData.name);
   });
   console.log('whewej');  
   connection.query('SELECT * from USER', function(err, rows, fields) {  
 var temp=0;
 var uPass;
 var uSalt;
 var userHashPass;
 var age;
   if (!err){  
       for (var i in rows){ 
        if(rows[i].userID == inputData.user_id)  
        {
	   uPass =  rows[i].userPassword;
	   uSalt =  rows[i].userName;
	   userHashPass = crypto.createHash("sha512").update(inputData.name+uSalt).digest("hex");
	   if(userHashPass == uPass)
	   {
	      console.log('pw ok');
	      if(rows[i].userAge>=20)
	      {
		 temp=1;
	      }
	      else
	      {
		temp=3;
	      }
	   }
	   else
	   {
	      console.log('pw not ok');
	      temp=2;
	   }
	}

       }
	if(temp==1)
	{
		res.write("adult");
		res.end();
	}
	else if(temp==2)
	{
		res.write("pwnot");
		res.end();
	}
	else if(temp==3)
	{
		res.write("teen");
		res.end();
	}
	else
	{
		res.write("not");
		res.end();
	}
	
  }



 });       
});

app.listen(8000, () => {
  console.log('Example app listening on port 3000!');
}); 
