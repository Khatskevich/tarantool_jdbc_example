local yaml = require('yaml')
box.cfg{
    wal_mode = 'none',
    listen = 3301,
}
box.schema.user.grant('guest', 'read,write,execute', 'universe')

box.sql.execute("create table users(id integer primary key, name string, sname string)")
box.sql.execute("insert into users values(1, 'valera', 'kon')")
box.sql.execute("insert into users values(2, 'Русскими', 'Буквами')")
--print(yaml.encode(res))
require('console').start()
