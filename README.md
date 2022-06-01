# Тестовое задание для GeoSteering

## Задание

Нужно реализовать два сервиса:
 
1. Загружать данные из csv-файла в БД PostgreSQL:
 - первая строка каждого файла содержит произвольные заголовки столбцов
 - каждую следующую строку следует сохранить в БД в виде в json-объекта {заголовок1=значение1, заголовок2=значение2,...}
 
2. По наименованию заголовка извлечь из БД все имеющиеся значения для данного заголовка и вернуть для отображения в таблице трёх столбцов: имя файла, номер строки в файле, значение.
 
Ограничения: реализация на spring boot, json-объекты следует хранить в колонке типа jsonb, для работы с БД использовать фреймворк mybatis (см. https://mybatis.org/spring/)

## Как запустить
Скачать репозиторий, в корне проекта выполнить `docker-compose up -d`. Затем следует запустить спринговое приложение.

## Пояснения

Во время старта приложения, выполниться импорт в базу данных из `.csv` файлов, расположенных в папке `/src/main/resources/csv/`.

Cэмплы `.csv` были взяты [отсюда](https://www.stats.govt.nz/large-datasets/csv-files-for-download/). Мою использовался набор сэмплов под названием `International trade: December 2021 quarter`. Самый объёмный файл насчитывает 1.7 миллионов записей.

Процесс и время импорта данных можно увидеть в логе прриложения:
```
[loadCsvFiles] START
Now loading: country_classification.csv, size 4257 bytes
 - Loaded and persisted in 81ms (0sec)
Now loading: goods_classification.csv, size 239619 bytes
 - Loaded and persisted in 92ms (0sec)
Now loading: output_csv_full.csv, size 87925789 bytes
 - Loaded and persisted in 41156ms (41sec)
Now loading: revised.csv, size 5665209 bytes
 - Loaded and persisted in 2519ms (2sec)
Now loading: services_classification.csv, size 2828 bytes
 - Loaded and persisted in 10ms (0sec)
[loadCsvFiles] END. Time taken: 43863ms (43sec)
```

### Как выполнять запросы к БД

Для получения результата выборки по ключу в `JSON` объекте следует проследовать на эдпойнт `http://localhost:8080/entry`. Для указания ключа, по которому следует сделать выборку нужно ипользовать параметр запроса `?q=`, например `?q=code`. Стоит отметить, что возвращаемый результат является "пагинированным". Для указания раз номера страницы и размера страныцы, необходимо использовать параметры запроса `?page=` и `?size=`. По-умолчанию размер страницы составляет `20` записей.

Для выборки по ключу без повторяющихся значений нужно указать параметр запроса `?distinct=true`
