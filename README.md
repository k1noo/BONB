# Believe Or Not to Believe 
Разработчик:
---------------------------------------
Божко Кирилл Александрович, БПМИ154

Тема проектной работы:
---------------------------------------------------------------------------
Приложение для образовательной платформы "Мой Универ".

Данное приложение представляет собой игру для платформы Android. Суть игры заключается в распознавании лжи. В каждом раунде игроку демонстрируется факт, который является правдой или нет. Игрок указывает что это Правда или Ложь. Если он угадал - ему начисляются очки. Каждый раунд формируется рандомизированно. 

Данное приложение предполагает несколько кейсов использования:
---------------------------------------------------------------------------
- Повествование об интересных фактах на общие темы
- Подготовка к прохождению тестирования по конкретным аспектам конкретных предметов
- Самостоятельное обучение и подготовка к проверке знаний
- Соревновательная сторона викторины

Используемые технологии:
---------------------------------------------------------------------------
- Мой Универ API - для доступа к базе вопросов платформы
- Android SDK - для построения приложения и взаимодействия с ОС Android
- Java - основной язык разработки
- OkHTTP Java Lib - организация REST-запросов и взаимодействие с "Мой Универ API"
- Google Play Services API - авторизация пользователей.

Реализованно:
---------------------------------------------------------------------------
- 2 активити: главное (MainActivity) и дополнительное, вызываемое через меню - DatabaseScreen(для редактирования базы данных фактов)
- База данных: набор пар "Факт-Истинность"
- База данных пользователей вида "Имя-EMail-Highscores"
- Механизм игрового процесса: по нажатию на *Start* начинается викторина из фактов, выбираемых из базы данных. На каждый предложенный факт необходимо дать ответ *True* или *False*. В зависимости от верности ответа высвечивается всплывающее сообщение (*Toast*) (*Right/Wrong*) и ведется подсчёт очков. По окончании фактов в базе - выводится общее количество набранных очков и предлагается перезапустить викторину. Если набранное количество очков превосходит рекордный результат данного игрока - новый рекорд заносится в базу данных пользователей.
- Авторизация в приложении через Google аккаунт. По нажатию на *Войти* запускается диалоговое активити с возможностью выбора уже существующего на устройстве аккаунта или созданием нового. После входа в систему, данные пользователя (*Display Name* и *EMail*) заносятся в базу данных, если вход был произведен впервые, либо определяется текущий игрок, если данные пользоватля были добавлены ранее. 
- Механизмы автоматической и ручной модификации базы данных фактов. 

  В автоматическом режиме (по нажатию на *Automatically Refresh Facts* на DatabaseScreen экране), после очистки базы данных, происходит 3 запроса на получение списка вопросов по 3 предметам (данные константы объявлены в коде, с возможностью простой модификации, а обусловлены они ограничением на доступ к базе вопросов в тестовом режиме "песочницы" API). Элементы данной выдачи становятся первой частью "Факта", к которой случайным образом (математическими преобразованиями над результатом Math.random()) запрашивается правильный или неправильный "Хвост факта". Полученный факт с пометкой корректности заносится в базу данных фактов приложения и использются для викторины.
  
  В ручном режиме (по нажатию на *Manual Editing*) становятся доступны элементы управления базой данных (2 EditText-объекта и 3 кнопки). Можно вручную вписать факт, указать его истинность и добавить его в базу нажатием на *Add*. Так же, можно очистить базу данных или вывести ее содержимое, отображаемое в логах приложения с LOGTAG'ом "mLog", нажатием на *Clear* и *Read* соответственно. После чего можно скрыть элементы ручного управления нажатием на кнопку *Done*.


План реализации:
---------------------------------------------------------------------------
- Проектная работа завершена.

Возможное развитие:
--------------------------------------------------------------------------
- Расширение базы фактов путем активации полной версии  Мой Универ API.
- Улучшение пользовательского интерфейса.
- Ввод дополнительных видов фактов с изображениями и видеозаписями.

Инструкция по запуску:
---------------------------------------------------------------------------
- Способ А: В Android Studio *ctrl+R* и выбрать физическое устройство для запуска, подключенное по USB с разрешенными USB-отладкой и USB-установкой или виртуальное устройство (эмулятор).
- Способ Б: В Android Studio *Build->Build APK*. Собранный APK проинсталлировать на Android-устройстве и запустить нажатием на иконку BONB.
