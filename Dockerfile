FROM mcr.microsoft.com/dotnet/core/aspnet:2.1.18@sha256:7000c71ad64ef922005ac3ea9ebd0daf2ecd4c4329dadfe56ca3b80433c82a51 AS base
WORKDIR /app
EXPOSE 80

FROM mcr.microsoft.com/dotnet/core/sdk:2.1.806@sha256:bdee2c2664cd8db7c68d782cb91fa722eca2730c84f9ee8040029b9dcd8fcc3b AS build
WORKDIR /src
COPY . .
RUN [...]

FROM build AS publish
WORKDIR /src/Web/Web.Startup
RUN [...]

FROM base AS final
WORKDIR /app
COPY --from=publish /app .

# Local
COPY /run-local.sh /run.sh
RUN chmod a+x /run.sh

ENTRYPOINT ["/run.sh"]