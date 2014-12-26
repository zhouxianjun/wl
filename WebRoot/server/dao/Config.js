/**
 * Created with JetBrains WebStorm.
 * User: Gary
 * Date: 12-11-30
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
exports.config = [{
    pool: {
        name: 'wl',
        maxconn: 3
    },
    db: {
        host: 'localhost',
        port: 3306,
        user: 'root',
        password: '',
        database: 'wl'
    }
}];
