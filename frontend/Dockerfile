FROM node:latest

WORKDIR /frontend

COPY ./package*.json /frontend

RUN npm install

COPY / /frontend

CMD ["npm", "run", "start"]

EXPOSE 5500