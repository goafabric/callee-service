# save and load state

curl -X POST http://localhost:3500/v1.0/state/statestore \
-H "Content-Type: application/json" \
-d '[{"key":"mykey","value":"hello dapr"}]' \
&& curl http://localhost:3500/v1.0/state/statestore/mykey 

# pub sub

curl -X POST http://localhost:3500/v1.0/publish/pubsub/mytopic \
-H "Content-Type: application/json" \
-d '{"message":"hi from dapr"}'

# docker compose
- start your app inside intellij
- just use docker compose up
- compose needs to be restarted everytime for pubsub to work 

# kubernetes
- dapr init -k
- dapr dashboard -k
- apply terraform file, that takes care of everything including a redis

- dapr uninstall -k

# evaluation
- idea of dapr in theory good because it delegates functionality like pubsub, state management to the infra structure
- this decouples in theory even more and allows for e.g. swapping out the message broker and unifies implementation with different languages
- odd mixup of infrastructure (sidecar) and application library 
- 
problems
- breaks native image support
- local development of pubsub gets harder, because instead of just having a simple kafka, we need a dapr container with loopback config
- mtls only works when using Daprs prop. service invocation, not with springs / quarkus default implementations
- it introduces an extra sidecar per app with about 30mb

# what problem does it really solve, what are we using ?
Infrastructure
- mTLS, Security, Observability: Istio Ambient Mesh, works WITHOUT a sidecar for all HTTP Traffic independent of implementation, defacto Standard
- Service registry: Already part of Kubernetes with by DNS
- Messaging: Kafka, which has become the defacto Standard
- State: Redis which has become the defacto Standard (Dragonfly has in Drop Replacement)
- Secrets: (Azure) Vault, injected via FS or Environment, without ANY Application Coupling
- Configuration: application.properties, overriden by Kubernetes Configmap
- Cryptography: Encryption of managed Databases, should NOT be party of applications
              
Implementation
- Messaging Integration: Spring Boot, Quarkus, GOFR
- Service Invocation: Spring Boot, Quarkus, GOFR
- State: Spring, Quarkus, GOFR (via Redis Implementation)
- Jobs: Spring Batch | JobRunr
- Workflows: Usually no value for simple Workflows, Spring State or Kotlin DSL as Alternative
- AI: Langchain, which is available for basically all Languages
- Distributed Lock: Spring Repository Lock Implementation
