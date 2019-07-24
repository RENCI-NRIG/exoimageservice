
#
# Copyright (c) 2017 Renaissance Computing Institute, except where noted.
# All rights reserved.
#
# This software is released under GPLv2
#
# Renaissance Computing Institute,
# (A Joint Institute between the University of North Carolina at Chapel Hill,
# North Carolina State University, and Duke University)
# http://www.renci.org
#
# For questions, comments please contact software@renci.org
#
# Author: Komal Thareja(kthare10@renci.org)
import requests
import urllib3
import string
import logging
import sys
import os
import time
import json
import argparse
import subprocess
import shutil

class ImageException(Exception):
    pass

class ImageInterface:
    @classmethod
    def __init__(self, log=None):
        self.stdout_path = '/dev/null'
        self._log = log
        if self._log is None:
            self._log = logging.getLogger('')

    @classmethod
    def get_image_size(self, host, project, user, password, imageId):
        headers = {
            'Accept': 'text/plain',
            'Content-Type': "application/json",
        }
        params = {
                'project':project,
                'userName':user,
                'password':password,
                'imageId':imageId
            }
        response = requests.get((host + '/exoimageservice/1.0.0/imageSize'), headers=headers, params=params, stream=True, verify=False)
        print ("Received Response Status: " + str(response.status_code))
        return response

    @classmethod
    def get_image(self, host, project, user, password, imageId, contentRange=None):
        headers = {
            'Accept': 'application/octet-stream',
            'Content-Type': "application/json",
        }
        if contentRange is not None:
            headers = {
             'Accept': 'application/octet-stream',
             'Content-Type': "application/json",
             'Range':contentRange
            }

        params = {
                'project':project,
                'userName':user,
                'password':password,
                'imageId':imageId
            }
        response = requests.get((host + '/exoimageservice/1.0.0/image'), headers=headers, params=params, stream=True, verify=False)
        print ("Received Response Status: " + str(response.status_code))
        return response


def main():
     parser = argparse.ArgumentParser(description='Python client to download images.\n')

     parser.add_argument(
         '-p',
         '--project',
         dest='project',
         type = str,
         help='Openstack Project Name maps to ',
         required=True
     )
     parser.add_argument(
         '-e',
         '--exoimageHost',
         dest='exoimageHost',
         type = str,
         help='Exo Image Service Host e.g. http://localhost:8222/',
         required=False,
         default='http://localhost:8222/'
     )
     parser.add_argument(
        '-u',
        '--user',
        dest='user',
        type = str,
        help='User Name',
        required=True
     )
     parser.add_argument(
         '-w',
         '--password',
         dest='password',
         type = str,
         help='password',
         required=True
     )
     parser.add_argument(
         '-i',
         '--imageId',
         dest='imageId',
         type = str,
         help='imageId',
         required=True
     )
     parser.add_argument(
         '-f',
         '--file',
         dest='file',
         type = str,
         help='file',
         required=True
     )
     parser.add_argument(
         '-s',
         '--split',
         dest='split',
         type = bool,
         help='download the file by parts',
         required=False,
         default=True
     )
     parser.add_argument(
         '-r',
         '--range',
         dest='range',
         type = int,
         help='download the file by parts in increments of range',
         required=False,
         default=49999999
     )

     args = parser.parse_args()

     im=ImageInterface()

     if args.split == True:
         r =im.get_image_size(args.exoimageHost, args.project, args.user, args.password, args.imageId)
         if r.status_code != 200 :
            print ("Failed to determine image size")
            sys.exit(1)
         size=int(r.content)
         print ("Image size: " + str(size))
         start=0
         end = args.range
         length=0
         while length < size :
            contentRange='bytes=' + str(start) + "-" + str(end)
            r =im.get_image(args.exoimageHost, args.project, args.user, args.password, args.imageId, contentRange)
            print ("Downloaded: %s" % r.headers.get("Content-Range") + "; Image size: " + str(size))
            #print ("Headers: %s" % r.headers)
            if r.status_code == 200 or r.status_code == 206:
                length = length + int(r.headers['Content-Length'])
                with open(args.file, 'ab') as f:
                    r.raw.decode_content = True
                    shutil.copyfileobj(r.raw, f)
            else :
                print ("Failed to download image")
                sys.exit(1)
            start = end
            end = start + args.range
            start = start + 1
            if r.status_code == 200:
                break
     else :
         r =im.get_image(args.exoimageHost, args.project, args.user, args.password, args.imageId)
         if r.status_code != 200 :
            print ("Failed to determine image size")
            sys.exit(1)
         print ("Headers: %s" % r.headers)
         if r.status_code == 200:
            with open(args.file, 'wb') as f:
                r.raw.decode_content = True
                shutil.copyfileobj(r.raw, f)
         else :
            print ("Failed to download image")
            sys.exit(1)
     print ("file download complete")


     sys.exit(0)

if __name__ == '__main__':
    main()
