## Swagger

http://localhost:8080/swagger-ui/index.html

## User stories

A user should:

- the list of visits (searching & filtering & sorting). A museum can have multiple visits.
- the map of museums (searching & filtering & sorting). If a museum is visited, there will be a indicator.
- add a visit related to a museum. A museum can have multiple visits at differents dates. It cannot have multiple visit
  a day for a user.

## Setup

```bash
docker compose --project-name museifier-dev -f ./docker/docker-compose-dev.yml up -d
```

## BAN

```bash
wget https://adresse.data.gouv.fr/data/ban/adresses/latest/addok/addok-france-bundle.zip
mkdir base_adresse_nationale
unzip -d base_adresse_nationale addok-france-bundle.zip
```

https://github.com/BaseAdresseNationale/addok-docker#installer-une-instance-avec-les-donn%C3%A9es-de-la-base-adresse-nationale

## Deployment

1. Package the app using Maven

2. Build the image and push

```bash
docker buildx build --platform linux/amd64 -t hoshiix/museifier-web:latest --push .
docker buildx build --platform linux/amd64 -t hoshiix/museifier-api:latest --push .
```

3. Run Docker compose

```bash
docker-compose up --detach
```

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
--data-urlencode '   redirect_uri=http://localhost:8080/' \
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