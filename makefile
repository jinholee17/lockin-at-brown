.PHONY: backend frontend dependencies

backend:
	cd server ; mvn package ; ./run

frontend:
	cd client ; npm start

dependencies:
	cd client; npm install; npm install firebase; npx playwright install

