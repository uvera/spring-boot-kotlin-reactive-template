.PHONY: all
all:
	$(info Usage:)
	$(info - make run-daemon)
	$(info - make stop-daemon)
	$(info - make run | run-inline)
	true

.PHONY: run-daemon
run-daemon:
		docker compose up -d

.PHONY: stop-daemon
stop-daemon:
		docker compose down

.PHONY: run
run:
		docker compose up

.PHONY: run-inline
run-inline: run
