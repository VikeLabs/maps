FROM node:16.13.0-alpine AS build
WORKDIR /home/app
COPY ./app /home/app
COPY api/src/main/resources/swagger.json /home/api/src/main/resources/swagger.json
RUN npm install
RUN npm run generate
RUN npm run build
RUN npm run test -- --watchAll=false
EXPOSE 3000:3000/tcp
CMD ["npm", "run", "start"]