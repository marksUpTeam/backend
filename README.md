# Инструкция

## База данных

Проект настроен на работу с СУБД PostgreSQL. Скачайте и установите с [официального сайта](https://www.postgresql.org/)

Сущности сами должны создаться в БД в ходе применения миграций (первом запуске).

## Настройки

В проекте присутствуют два файла настроек:

- application.properties - основные настройки приложения
- application-tls.properties - настройки шифрования для работы HTTPS (в gitignore, так и нужно)

Естественно, все файлы настроек лежат по пути src/main/resources

### application.properties

- spring.application.name - название приложения, не менять
- spring.datasource.url - URL до СУБД и соответствующей базы данных, поменяйте под свой порт (на котором сидит PostgreSQL) и свое название базы данных внутри PostgreSQL
- spring.datasource.username - имя пользователя PostgreSQL из под которого будут производиться транзакции Hibernate
- spring.datasource.password - пароль пользователя PostgreSQL
- spring.jpa.hibernate.ddl-auto - не менять
- spring.jpa.show-sql - выводить SQL запросы в консоль или нет
- jwtKey - публичный ключ ВК для валидации выданных VK ID JWT
- spring.profiles.active - не менять

Пример заполнения:
```
spring.application.name=marksUpBackend
spring.datasource.url=jdbc:postgresql://localhost:1488/marksup
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwtKey=-----BEGIN PUBLIC KEY-----\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvsvJlhFX9Ju/pvCz1frB\nDgJs592VjdwQuRAmnlJAItyHkoiDIOEocPzgcUBTbDf1plDcTyO2RCkUt0pz0WK6\n6HNhpJyIfARjaWHeUlv4TpuHXAJJsBKklkU2gf1cjID+40sWWYjtq5dAkXnSJUVA\nUR+sq0lJ7GmTdJtAr8hzESqGEcSP15PTs7VUdHZ1nkC2XgkuR8KmKAUb388ji1Q4\nn02rJNOPQgd9r0ac4N2v/yTAFPXumO78N25bpcuWf5vcL9e8THk/U2zt7wf+aAWL\n748e0pREqNluTBJNZfmhC79Xx6GHtwqHyyduiqfPmejmiujNM/rqnA4e30Tg86Yn\ncNZ6vLJyF72Eva1wXchukH/aLispbY+EqNPxxn4zzCWaLKHG87gaCxpVv9Tm0jSD\n2es22NjrUbtb+2pAGnXbyDp2eGUqw0RrTQFZqt/VcmmSCE45FlcZMT28otrwG1ZB\nkZAb5Js3wLEch3ZfYL8sjhyNRPBmJBrAvzrd8qa3rdUjkC9sKyjGAaHu2MNmFl1Y\nJFQ3J54tGpkGgJjD7Kz3w0K6OiPDlVCNQN5sqXm24fCw85Pbi8SJiaLTp/CImrs1\nZ3nHW5q8hljA7OGmqfOP0nZS/5zW9GHPyepsI1rW6CympYLJ15WeNzePxYS5KEX9\nEncmkSD9b45ge95hJeJZteUCAwEAAQ==\n-----END PUBLIC KEY-----

spring.profiles.active=tls

```

### application-tls.properties

Файл настроек TLS для работы защищенного соединения через HTTPS. Находится в gitignore так как содержит пароль к сертификату.

Строчки, которые нужно изменить из примера:

- server.ssl.key-store-password - пароль к сертификату (спросите)
- server.ssl.key-alias - идентификатор сертификата (спросите)

```
server.ssl.key-store-type=PKCS12

server.ssl.key-store=classpath:keystore/naburnm8.p12

server.ssl.key-store-password=ПАРОЛЬ

server.ssl.key-alias=АЛИАС

server.ssl.enabled=true

```

### P.S

Если что, naburnm8 без аватарки, Me и аккаунт сделавший этот коммит - один и тот же человек


