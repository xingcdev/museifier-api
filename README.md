# Museum App

## Compétences acquis

J'ai pu acquérir les compétences suivantes avec ce projet :

- La création d'une API Rest de A à Z
- Les patterns utilisés en Backend, à savoir le système des 3 couches (Persistence, Service, View), Data Transfer Object
  et Repository.
- Test d'intégration des controllers avec Spring MVC Test framework
- Authentification avec le protocol d'autorisation OAuth 2.0. L'implémentation est fait avec le serveur d'authorization
  Keycloak.

## How to Authenticate

1. Go to the Login page and get the value of 'code' in the success login url:
   http://localhost:9000/realms/museum-dev/protocol/openid-connect/auth?response_type=code&client_id=museum-app&redirect_uri=http://localhost:8080/

2. Get the access token & refresh token using the code:

```bash
curl --location 'http://localhost:9000/realms/museum-dev/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=museum-app' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code=XXXXXX' \
--data-urlencode 'redirect_uri=http://localhost:8080/' \
--data-urlencode 'scope=openid'
```

3. Get the access token using the refresh token so that you don't have to login with your credentials again:

```bash
curl --location 'http://localhost:9000/realms/museum-dev/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=museum-app' \
--data-urlencode 'grant_type=refresh_token' \
--data-urlencode 'refresh_token=XXXXXX' \
--data-urlencode 'scope=openid'
```

## Todos

- [x] /visits should return the visits of the user not all visits.
- [x] Replace findOne of museum to existsById
- [ ] Check if we have a valid dto in POST, PUT, PATCH
- [ ] Change DirtyContext to SQL Script in @BeforeEach because it is too expensive to reset the application context each
  time?