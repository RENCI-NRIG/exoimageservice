# Table of contents

 - [Running the code](#run1)
   - [Running everything in docker](#run2)
     - [Clone git repo](#clone)
     - [User specific configuration](#config)
     - [Run Docker](#docker)
     - [Examples](#examples)
 
# <a name="run1"></a>Running the code
This repository is designed to be run in Docker out of the box using docker-compose. Optionally the user can make minor configuration changes to run portions of the project on their local machine for easier programmatic interaction with Mobius directly.

## <a name="run2"></a>Running everything in docker
### <a name="clone"></a>Clone git repo
```
git clone https://github.com/RENCI-NRIG/exoimageservice.git
cd ./exoimageservice/docker
```
### <a name="config"></a>User specific configuration
Once images are ready, update configuration in docker as indicated below:
Update docker/config/application.properties to specify head node specific values for following properties
```
os.authUrl=http://10.100.0.10:5000/v3
os.region=RegionOne
```
### <a name="run3"></a>Run Docker
Run docker-compose up -d from Mobius/docker directory

```
$ docker-compose up -d
Creating image_service ... done
```
After a few moments the docker containers will have stood up and configured themselves. User can now trigger requests to image service. Refer to [Interface](../python/Readme.md) to see the API

#### <a name="example"></a>Few example commands
- Download an image
```
python3 image_client.py -p <project name> -u <user> -w <password> -i 48ddb2fa-84ca-4411-8047-5610f4ad20db -f ./image_name.qcow2
```
