
# Creating a Helm chart

1. Initialize a new Chart for the member component:

```
$ helm create member
```

2. Set the correct image in values.yaml:

```
eu.gcr.io/cloud-native-deployment/member
```

3. Add the following settings to the values.yaml,
and configure them in the Deployment as environment variables

| key | Default value | Description | Environment variable |
| --- | --------------|-------------|----------------------|
| `rabbitmq.host`| rabbitmq | RabbitMQ host | RABBITMQ_HOST |
| `rabbitmq.port`| 5672 | RabbitMQ port     | RABBITMQ_PORT |
| `rabbitmq.username`| guest | RabbitMQ username | RABBITMQ_USERNAME |
| `rabbitmq.password`| guest | RabbitMQ password | RABBITMQ_PASSWORD |
| `member.name` | Unknown | The name of the member| MEMBER_NAME |

4. Make the ingress path configurable.
Add a `ingress.path` property to values.yaml with default value `member`.
Use this for the Ingress (see deployment/k8s/member.yaml).

5. Install the Chart. Use your own name as release name,
override the member.name property with your own name,
use a custom ingress path,
and set the RabbitMQ credentials.
Example:

```
$ helm install member --name tom \ 
  --set member.name=Tom \
  --set ingress.path=tom \
  --set rabbitmq.username=something \
  --set rabbitmq.password=something
```

Optionally create a separate settings YAML that you can use:

```
# values.override.yaml
member.name: Tom
ingress.path: tom
rabbitmq:
    username: something
    password: something

$ helm install member --name tom -f values.override.yaml
```

6. Go to [https://console.cloud.google.com/kubernetes/discovery?project=cloud-native-deployment&service_list_tablesize=50](https://console.cloud.google.com/kubernetes/discovery?project=cloud-native-deployment&service_list_tablesize=50)
and find your ingress.

The application should be available at http://<ip of the load balancer/<your-ingress-path>/index.html

