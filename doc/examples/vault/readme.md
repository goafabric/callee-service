# dependency
implementation("org.springframework.cloud:spring-cloud-starter-vault-config:4.0.0")

# vault docker
docker run --name vault --rm --cap-add=IPC_LOCK -p8200:8200 -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' vault:1.13.1

# example curl to create secret
curl --header "X-Vault-Token: myroot" --request POST --data '{ "data": {"password": "my-long-password"} }' \
http://127.0.0.1:8200/v1/secret/data/mycredentials
