REPO=eu.gcr.io/cloud-native-deployment
SPEAKER=speaker
MEMBER=member
HOUSE=house

guard-%:
	@ if [ "${${*}}" = "" ]; then \
		echo "Environment variable $* not set"; \
		exit 1; \
	fi

.PHONY: all
all: speaker member house

.PHONY:
push: speaker-push member-push house-push

.PHONY: member
member:
	docker build -t ${REPO}/${MEMBER} ./member

.PHONY: house
house:
	docker build -t ${REPO}/${HOUSE} ./house

.PHONY: house-push
house-push:
	docker push ${REPO}/${HOUSE}

.PHONY: member-push
member-push:
	docker push ${REPO}/${MEMBER}

.PHONY: speaker
speaker:
	docker build -t ${REPO}/${SPEAKER} ./speaker

.PHONY: speaker-push
speaker-push:
	docker push ${REPO}/${SPEAKER}

.PHONY: kube
kube: speaker-kube member-kube house-kube

.PHONY: speaker-kube
speaker-kube:
	kubectl apply -f ./deployment/k8s/speaker

.PHONY: member-kube
member-kube:
	kubectl apply -f ./deployment/k8s/member

.PHONY: house-kube
house-kube:
	kubectl apply -f ./deployment/k8s/house

.PHONY: speaker-kube-del
speaker-kube-del:
	kubectl delete -f ./deployment/k8s/speaker

.PHONY: house-kube-del
house-kube-del:
	kubectl delete -f ./deployment/k8s/house

.PHONY: member-kube-del
member-kube-del:
	kubectl delete -f ./deployment/k8s/member

.PHONE: kube-del
kube-del: speaker-kube-del house-kube-del member-kube-del