FROM node:16.13.0-alpine
WORKDIR /home/app
COPY ./app /home/app
COPY ["api/src/test/resources/ca/vikelabs/maps/OpenApiTest.check approved.approved", "/home/api/src/test/resources/ca/vikelabs/maps/OpenApiTest.check approved.approved"]
RUN npm install
RUN npm run generate
RUN npm run test -- --watchAll=false
RUN npm run build
EXPOSE 5000:5000
HEALTHCHECK CMD curl --fail http://localhost:5000 || exit 1
CMD ["npm", "run", "start"]
