# Установка
### Что нужно сначала установить
Для правильно работы необходимо установить Tomcat Server версии 8 или 9 и Java JRE 1.8 и выше
## ВАЖНО! ПРИ УСТАНОВКЕ НЕОБХОДИМО УСТАНОВИТЬ ЛОГИН И ПАРОЛЬ В TOMCAT ИНАЧЕ НЕЛЬЗЯ БУДЕТ ПОЛУЧИТЬ ДОСТУП К TOMCAT MANAGER

**1. Если есть Inellij IDEA Ultimate**
* Склонировать проект в IDEA
* добавить конфигурацию Tomcat Server 

![Configuration](https://istgit.pro/Group6224/Jacobi572/raw/commit/65b8075c868fa921e5e5bb16f4965996c13c5057/readme/Screenshot_1.png)

![Configuration](https://istgit.pro/Group6224/Jacobi572/raw/commit/03cb64c35e4ede1c2dfe8e011ac5c9b85aec70f5/readme/Screenshot_2.png)

![Configuration](https://istgit.pro/Group6224/Jacobi572/raw/commit/c97a89a6436487a6e8149266f205f5b7f74c80f0/readme/Screenshot_3.png)

* запустить сборку

**2. Запуск только с помощью Tomcat Server**
* Открыть Tomcat Server Manager
* Ввести логин и пароль которые были введены при установке Tomcat Server
* В поле Delpoy выбрать war файл из репозитория
![Deploy](https://istgit.pro/Group6224/Jacobi572/raw/commit/504715edfb54b8c7cc0515c9286ca913feb35c8c/readme/Screenshot_4.png)
 или скопировать в корневой кталог Tomcat в папку webapps war файл,обновить страницу Tomcat Manager и нажать кнопку start напротив /jacobi
 ![Start](https://istgit.pro/Group6224/Jacobi572/raw/commit/07d730e82ce3ca1b9cbaafae74d37f93d750e086/readme/Screenshot_5.png)
по нажатию на /jacobi откроется приложение в новой вкладке