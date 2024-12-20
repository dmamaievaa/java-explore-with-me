# Многомодульный проект: java-explore-with-me

## Описание
Этот проект представляет собой многомодульное Java-приложение, состоящее из двух сервисов:

1. **Основной сервис (EWM Main Service)**:
   - Содержит функционал для работы основного продукта.
   - API разделён на три уровня:
     - **Публичный API** — доступен всем пользователям без авторизации.
     - **Закрытый API** — доступен только авторизованным пользователям.
     - **Административный API** — предназначен для администраторов сервиса.

2. **Сервис статистики (EWM Stats Service)**:
   - Сохраняет информацию о просмотрах.
   - Предоставляет выборки для анализа активности приложения, включая:
     - Количество обращений к спискам событий.
     - Количество запросов к деталям события.

---

### Основной сервис
API основного сервиса реализован в трёх частях:
1. **Публичный API**:
   - Предоставляет информацию для всех пользователей.
   - Примеры: доступ к списку событий, категории, публичные данные.

2. **Закрытый API**:
   - Обеспечивает функциональность для авторизованных пользователей.
   - Примеры: создание, редактирование и управление своими событиями.

3. **Административный API**:
   - Позволяет администраторам управлять системой.
   - Примеры: модерирование событий, управление категориями и пользователями.

Спецификация API: `ewm-main-service-spec.json`.

---

### Сервис статистики
Сервис статистики предназначен для анализа активности приложения. Он собирает данные о:
1. Количестве запросов к спискам событий.
2. Количестве запросов к деталям событий.

На основе этих данных формируется статистика, которая может использоваться для мониторинга и оптимизации работы приложения.

Спецификация API: `ewm-stats-service.json`.

---

## Дополнительная фича - Рейтинг

# LikesAdminController

Контроллер для работы с лайками и дизлайками событий. Данный контроллер доступен только администраторам и предоставляет функционал для поиска и удаления реакции (лайк/дизлайк).

## Эндпоинты

### 1. Поиск реакции

**URL:** `/admin/likes/search`

**Метод:** `GET`

**Описание:** Этот эндпоинт позволяет искать лайки/дизлайки по заданным параметрам.

#### Параметры запроса:
- `eventId` (необязательный, `Integer`): ID события, для которого нужно найти лайки.
- `userId` (необязательный, `Integer`): ID пользователя, для которого нужно найти лайки.
- `from` (необязательный, `Integer`, по умолчанию 0): Параметр для пагинации, указывает, с какого элемента нужно начать.
- `size` (необязательный, `Integer`, по умолчанию 10): Параметр для пагинации, указывает количество элементов на странице.

#### Пример запроса:
GET /admin/likes/search?eventId=1&userId=2&from=0&size=10


#### Ответ:
Возвращает список объектов типа `Like`, соответствующих заданным параметрам. Каждый объект содержит информацию о реакции для конкретного события и пользователя.


## Примечания
- Параметры пагинации (`from`, `size`) необходимы для реализации постраничного вывода результатов в методе поиска.
- Если не указать параметры поиска (например, `eventId` или `userId`), то будут возвращены все реакции в пределах указанных параметров пагинации.

# EventPrivateController

Контроллер для работы с событиями пользователя. Он предоставляет API для получения, создания, обновления событий, а также для управления лайками и дизлайками для событий.

## Добавленные эндпоинты

### 1. Добавление лайка

**URL:** `/users/{userId}/events/{eventId}/like`

**Метод:** `PUT`

**Описание:** Добавляет лайк к событию для указанного пользователя.

#### Параметры пути:
- `userId` (`Integer`): ID пользователя.
- `eventId` (`Integer`): ID события, к которому нужно поставить лайк.


#### Пример запроса:
PUT /users/1/events/2/like

#### Ответ:
Статус ответа: `204 No Content`.

---

### 2. Удаление лайка

**URL:** `/users/{userId}/events/{eventId}/like`

**Метод:** `DELETE`

**Описание:** Удаляет лайк с события для указанного пользователя.

#### Параметры пути:
- `userId` (`Integer`): ID пользователя.
- `eventId` (`Integer`): ID события, с которого нужно удалить лайк.


#### Пример запроса:
DELETE /users/1/events/2/like


#### Ответ:
Статус ответа: `204 No Content`.

---

### 3. Добавление дизлайка

**URL:** `/users/{userId}/events/{eventId}/dislike`

**Метод:** `PUT`

**Описание:** Добавляет дизлайк к событию для указанного пользователя.

#### Параметры пути:
- `userId` (`Integer`): ID пользователя.
- `eventId` (`Integer`): ID события, к которому нужно поставить дизлайк.


#### Пример запроса:
PUT /users/1/events/2/dislike


#### Ответ:
Статус ответа: `204 No Content`.

---

### 4. Удаление дизлайка

**URL:** `/users/{userId}/events/{eventId}/dislike`

**Метод:** `DELETE`

**Описание:** Удаляет дизлайк с события для указанного пользователя.

#### Параметры пути:
- `userId` (`Integer`): ID пользователя.
- `eventId` (`Integer`): ID события, с которого нужно удалить дизлайк.


#### Пример запроса:
DELETE /users/1/events/2/dislike


#### Ответ:
Статус ответа: `204 No Content`.

---

## Примечания
- Лайк и дизлайк могут быть добавлены или удалены только авторизованными пользователями.
- Все операции с лайками и дизлайками возможны только на уже опубликованных событиях, в противном случае ожидается ответ 409. 
- В случае успеха ожидается статус 204 (No Content).


# EventPublicController

**URL:** `/events`

Контроллер предоставляет публичный доступ к информации о событиях. Он позволяет пользователям получать данные о событиях, включая детали и рейтинг, а также осуществлять поиск по фильтрам. Контроллер содержит следующие методы:

## Добавленные эндпоинты

### Получение топовых событий

**URL:** `/events/events/top-rated`

**Метод:** `GET`

**Описание:** Возвращает список топовых событий с наибольшими рейтингами. Количество событий в списке можно ограничить с помощью параметра `limit`.
Метод возвращает список объектов `EventShortDto`, которые содержат информацию о событиях, включая количество лайков/дизлайков и их рейтинг.

#### Параметры запроса:
- `limit` (необязательный, `int`, по умолчанию 10): Максимальное количество событий, которое необходимо вернуть. Если параметр не указан, возвращается 10 событий.

#### Пример запроса:

GET /events/events/top-rated?limit=5


## Ответ:
Возвращает список объектов типа `EventShortDto`. 


# EventAdminController

**URL:** `/admin/events"`

Контроллер для управления событиями администраторами. Предоставляет возможности для редактирования событий, управления видимостью лайков, а также другие административные действия.Контроллер содержит следующие методы:

## Добавленные эндпоинты

### Скрытие/показ лайков для события

**URL:** `/admin/events/{eventId}/hide-likes`

**Метод:** `PATCH`

**Описание:** Этот эндпоинт позволяет администратору скрыть или показать количество лайков, дизлайков и рейтинг для указанного события.

#### Параметры пути:
-  'eventId' (Integer): ID события, для которого необходимо изменить видимость лайков.

#### Параметры запроса:
- 'hideLikes' (Boolean): Флаг, определяющий, нужно ли скрыть (true) или показать (false) лайки, дизлайки и рейтинг.

#### Пример запроса:

PATCH /admin/events/8/hide-likes?hideLikes=true

## Ответ:
Возвращает список объектов типа `EventFullDto`. 
Если hideLikes=true, то поля likes, dislikes и rating не включаются в ответ.
Если hideLikes=false, то эти поля становятся видимыми в ответе.



## Установка и запуск

### Требования
- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL** (для хранения данных)

https://github.com/dmamaievaa/java-explore-with-me/pull/5

