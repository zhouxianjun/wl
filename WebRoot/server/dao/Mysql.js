/**
 * Created with JetBrains WebStorm.
 * User: Gary
 * Date: 12-11-30
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
var Mysql = require('mysql');
var config = require('./Config.js').config;
var pool = function(config){
    this.free = [];
    this.used = 0;
    for(var key in config.pool)
        this[key] = config.pool[key];
    this.newConnection = function(){
        var con = Mysql.createConnection(config.db);
        this.free.push(con);
        return con;
    };
    this.getConnection = function(){
        var con = null;
        if(this.used < this.maxconn){
            if(this.free.length > 0){
                con = this.free[0];
                this.free.splice(0,1);
                if(!con)
                    con = this.getConnection();
            }else{
                con = this.newConnection();
            }
            this.used++;
            console.log('当前使用连接: ' + this.used + ' 空闲连接: ' + this.free.length);
        }
        return con;
    };
    this.freeConnection = function(con){
        this.free.push(con);
        this.used--;
    };
    this.freeAll = function(){
        this.used = 0;
        for(var i = 0; i < this.free.length; i++){
            this.free[i].end();
        }
        this.free = [];
    };
};
var client = {
    pools: [],
    /**
     * 得到一个数据库连接
     * @param name 数据库pool名字name
     * @return {*}
     */
    getConnection: function(name){
        var pool = this.pools[name];
        return pool ? pool.getConnection() : null;
    },
    /**
     * 释放一个数据库连接
     * @param name 数据库pool名字name
     * @param con 连接
     */
    freeConnection: function(name,con){
        var pool = this.pools[name];
        if(pool)
            pool.freeConnection(con);
    },
    /**
     * 释放一个数据库所有连接
     * @param name 数据库pool名字name
     */
    freePool: function(name){
        var pool = this.pools[name];
        if(pool)
            pool.freeAll();
    },
    /**
     * 释放所有数据库的所有连接
     */
    freeAll: function(){
        for(var key in this.pools)
            this.pools[key].freeAll();
    }
};
exports.instance = function(){
    if(client.pools.length < 1){
        for(var i = 0; i < config.length; i++){
            client.pools[config[i].pool.name] = new pool(config[i]);
        }
    }
    return client;
};
