# Table of contents

 - [Description](#descr)
 - [Installation](#install)
   - [Image Client](#imgclient)
     - [Usage](#usage)
   - [Image Client Examples](#image_client_examples)
     - [Download a workflow](#img_download)
 
# <a name="descr"></a>Description
Python based clients
 - Image client to trigger Image Service REST commands.

# <a name="install"></a>Installation
`https://github.com/RENCI-NRIG/exoimageservice.git`

`cd exoimageservice/python/`

You are now ready execute python client.

Pre-requisites: requires python 3 or above version and python requests package installed

## <a name="imgclient"></a>Image Client
Image client to trigger Image Service REST commands.

### <a name="usage"></a>Usage
```
usage: python3 image_client.py -h
usage: image_client.py [-h] -p PROJECT [-e EXOIMAGEHOST] -u USER -w PASSWORD
                       -i IMAGEID -f FILE

Python client to download images.

optional arguments:
  -h, --help            show this help message and exit
  -p PROJECT, --project PROJECT
                        Openstack Project Name maps to
  -e EXOIMAGEHOST, --exoimageHost EXOIMAGEHOST
                        Exo Image Service Host e.g. http://localhost:8222/
  -u USER, --user USER  User Name
  -w PASSWORD, --password PASSWORD
                        password
  -i IMAGEID, --imageId IMAGEID
                        imageId
  -f FILE, --file FILE  file
```

### <a name="image_client_examples"></a>Image Client Examples
#### <a name="img_download"></a>Download an image
```
python3 image_client.py -p <project name> -u <user> -w <password> -i <image id> -f <file name along with path>
```
