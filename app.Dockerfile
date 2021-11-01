FROM node:16.13.0-alpine
WORKDIR /home/app
COPY ./app /home/app
COPY ["api/src/test/resources/ca/vikelabs/maps/OpenApiTest.check approved.approved", "/home/api/src/test/resources/ca/vikelabs/maps/OpenApiTest.check approved.approved"]
RUN npm install
RUN npm run generate
RUN npm run test -- --watchAll=false
RUN npm run build
EXPOSE 3000:3000
RUN npm install -g serve
HEALTHCHECK CMD curl --fail http://localhost:3000 || exit 1
CMD npx serve -l 3000 build
