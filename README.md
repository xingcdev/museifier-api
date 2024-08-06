# Museum App

## Compétences acquis

J'ai pu acquérir les compétences suivantes avec ce projet :

- La création d'une API Rest de A à Z
- Les patterns utilisés en Backend, à savoir le système des 3 couches (Persistence, Service, View), Data Transfer Object et Repository.
- Test d'intégration des controllers avec Spring MVC Test framework
- Authentification avec le protocol d'autorisation OAuth 2.0. L'implémentation est fait avec le serveur d'authorization Keycloak.

## Authentication

Login page:
http://localhost:9000/realms/museum-dev/protocol/openid-connect/auth?response_type=code&client_id=museum-app&redirect_uri=http://localhost:8080/

Get the access token & refresh token: 

```bash
curl --location 'http://localhost:9000/realms/museum-dev/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=museum-app' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code=XXX' \
--data-urlencode 'redirect_uri=http://localhost:8080/'
```

## Todos

- [ ] /visits should return the visits of the user not all visits.
- [ ] Replace findOne of museum to existsById
- [ ] Check if we have a valid dto in POST, PUT, PATCH