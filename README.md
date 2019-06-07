# Image Service

Image Service is Spring-Boot based REST Webserver with the ability to download VM images created by users from the respective Openstack Head Nodes where the image is hosted.


## Design
With ORCA upgraded to latest openstack. Users can create images from Horizon Dashboard.
However, currently, glance does not expose a GUI interface to download the images from Horizon Dashboard.

Horizon URL, username, password and project name are provided to the User in the manifest for each compute node. We do not provide head node access to users so even with this information, they can download the images.

Image service allows users to download the images from head node. Users would be required to provide following information:

UserName
ProjectName
Password

REST service would be spawned on the head-node and use Openstack Glance REST APIs to download the image and transfer it to the user.

## API Server  
Swagger enables the generation of clients and servers in a variety of common programming languages via the swagger codegen project.

Clients are generated to be fully formed and functional from the generated files including documentation Servers are generated as stubbed code, and require the logical operations to be added by the user The server within this repository is based on Spring Boot and Java API for RESTful Web Services.

## API Documentation
[API Documentation](https://app.swaggerhub.com/apis-docs/kthare10/exoimageservice/1.0.0)

## Generate a new server stub
In a browser, go to [Swagger definition](https://app.swaggerhub.com/apis/kthare10/exoimageservice/1.0.0)

From the generate code icon (downward facing arrow), select Download API > JSON Resolved

A file named swagger-client-generated.zip should be downloaded. This file will contain swagger.json. Extract the json file from the swagger-client-generated.zip and run the following command to generate the Srpingboot Swagger server.

Use myOptions.json config file to specify name of the packages. Refer [Swagger-Codegen](https://github.com/swagger-api/swagger-codegen/wiki/Server-stub-generator-HOWTO#java-springboot) for more details.

```
$ cat myOptions.json
{
  "basePackage": "org.renci.exogeni.image.service",
  "modelPackage": "org.renci.exogeni.image.service.model",
  "apiPackage": "org.renci.exogeni.image.service.api",
  "configPackage":"org.renci.exogeni.image.service.config"
}
$ swagger-codegen generate -i swagger.json -l spring -c myOptions.json -o exoimageservice
```
## <a name="docker"></a>How to use or launch Image Service?
- Refer to [Docker](./docker/Readme.md) to launch Image Service
