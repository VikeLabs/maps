FROM node:16.13.0-alpine AS build
WORKDIR /home/app
COPY ./app /home/app
RUN npm install
RUN npm run build
RUN npm run test -- --watchAll=false
EXPOSE 3000:3000/tcp
CMD ["npm", "run", "start"]