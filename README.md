# Helm Workshop

This repository contains all course material for the Helm Workshop

Tom Verelst
<br />Cloud Native Deployment

## Prerequisites

* Helm 2 (https://helm.sh)
* kubectl
* Google Cloud SDK

## IntelliJ IDEA Plugins

Install the following plugins in IntelliJ IDEA

* Kubernetes
* Go Template

## Setting up gcloud/kubectl

Initialize a new Google Cloud SDK config:

```
$ gcloud init
```

* Login with the correct account
* Choose the project: `cloud-native-deployment`
* Choose a default Compute Region and Zone: `[18] europe-west1-d`

Your Google Cloud SDK should be configured now.

To configure `kubectl`, fetch the credentials:

```
$ gcloud container clusters get-credentials helm
Fetching cluster endpoint and auth data.
kubeconfig entry generated for helm.
```