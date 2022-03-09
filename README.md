# Coding Challenge

Die Aufgabe der Challenge ist es, den `Spring-Controller` im Backend, als auch den `Http-Service` im Frontend zu ergänzen.
Dadurch soll eine Kommunikation zwischen dem Front- und Backend ermöglicht werden.

Nachfolgend sind alle notwendingen REST-Schnitstellen definiert.


#### REST

- `GET` `localhost:8080/api/countries` - liefert eine Liste mit allen Einträgen zurück
- `GET` `localhost:8080/api/countries/{name}` - liefert eine Liste mit den gefundenen Namen zurück
- `POST` `localhost:8080/api/countries` - legt ein neues Land in der Datenbank an
- `PUT` `localhost:8080/api/countries/{id}` - ermöglicht das Bearbeiten vom Namen und Länderkürzel, mittels der `id`
- `DELETE` `localhost:8080/api/countries/{id}` - Löscht ein Land, mittels der `id`


#### JSON-Struktur

```
  {
    "id":999,
    "name":"CountryName",
    "countrycode":"CountryCode"
  }
```


