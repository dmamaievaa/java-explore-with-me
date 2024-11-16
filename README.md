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

## Установка и запуск

### Требования
- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL** (для хранения данных)
