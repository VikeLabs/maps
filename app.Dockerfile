FROM node:16.13.0-alpine AS build
WORKDIR /home/app
COPY ./app /home/app
COPY ["api/src/test/resources/OpenApiTest.check approved.approved", "/home/api/src/test/resources/OpenApiTest.check approved.approved"]
RUN npm install
RUN npm run generate
RUN npm run build
RUN npm run test -- --watchAll=false
EXPOSE 3000:3000/tcp
CMD ["npm", "run", "start"]