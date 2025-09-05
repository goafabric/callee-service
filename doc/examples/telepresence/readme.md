# once
https://telepresence.io/docs/install/client

telepresence helm install

# start

telepresence connect -n example

telepresence intercept callee-service-application --port 50900:8080

telepresence leave callee-service-application

telepresence quit