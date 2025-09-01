resource "helm_release" "callee-service-application" {
  repository = var.helm_repository
  name       = "callee-service-application"
  chart      = "${var.helm_repository}/callee-service/application"
  namespace  = "example"
  create_namespace = false
  timeout = var.helm_timeout

  depends_on = [helm_release.redis]

  set {
    name  = "ingress.hosts"
    value = var.hostname
  }

  set {
    name = "oidc.enabled"
    value = local.oidc_enabled
  }

  set_sensitive {
    name = "oidc.session.secret"
    value = random_password.oidc_session_secret.result
  }

  set {
    name = "image.arch"
    value = ""
  }

  set {
    name = "image.tag"
    value = "3.5.4-dapr-SNAPSHOT"
  }
}

# deploy redis used for state and pubsub
resource "helm_release" "redis" {
  name       = "redis"
  repository = "https://charts.bitnami.com/bitnami"
  chart      = "redis"
  namespace  = "example"
  version    = "20.1.1"
  timeout    = var.helm_timeout

  set {
    name = "image.repository"
    value = "bitnamilegacy/redis"
  }
  set {
    name = "global.security.allowInsecureImages"
    value = true
  }

  set {
    name = "auth.enabled"
    value = false
  }

  
  set {
    name = "architecture"
    value = "standalone"
  }
  set {
    name  = "replica.replicaCount"
    value = "1"
  }
  set {
    name  = "sentinel.readinessProbe.initialDelaySeconds"
    value = "5"
  }
  set {
    name  = "replica.readinessProbe.initialDelaySeconds"
    value = "5"
  }
}

# patch sidecar annotations, normally part of helm
resource "null_resource" "patch_callee_service" {
  depends_on = [helm_release.callee-service-application]
  provisioner "local-exec" {
    command = <<EOT
      kubectl patch deployment callee-service-application \
        -n example \
        -p '{"spec":{"template":{"metadata":{"annotations":{"dapr.io/enabled":"true","dapr.io/app-id":"callee-service","dapr.io/app-port":"8080"}}}}}'
    EOT
  }
}


resource "kubernetes_manifest" "dapr_state_store" {
  manifest = {
    "apiVersion" = "dapr.io/v1alpha1"
    "kind"       = "Component"
    "metadata" = {
      "name"      = "mystore"
      "namespace" = "example"
    }
    "spec" = {
      "type"    = "state.redis"
      "version" = "v1"
      "metadata" = [
        {
          "name"  = "redisHost"
          "value" = "redis-master.example:6379"
        },
        {
          "name"  = "redisPassword"
          "value" = ""
        }
      ]
    }
    "scopes" = ["callee-service"]
  }
}

resource "kubernetes_manifest" "dapr_pubsub" {
  manifest = {
    "apiVersion" = "dapr.io/v1alpha1"
    "kind"       = "Component"
    "metadata" = {
      "name"      = "pubsub"
      "namespace" = "example"
    }
    "spec" = {
      "type"    = "pubsub.redis"
      "version" = "v1"
      "metadata" = [
        {
          "name"  = "redisHost"
          "value" = "redis-master.example:6379"
        },
        {
          "name"  = "redisPassword"
          "value" = ""
        }
      ]
    }
    "scopes" = ["callee-service"]
  }
}
