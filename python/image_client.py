
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
    def _headers(self):
        headers = {
            'Accept': 'application/octet-stream',
            'Content-Type': "application/json",
        }
        return headers

    @classmethod
    def get_image(self, host, project, user, password, imageId):
        params = {
                'project':project,
                'userName':user,
                'password':password,
                'imageId':imageId
            }
        response = requests.get((host + '/exoimageservice/1.0.0/image'), headers=self._headers(), params=params, stream=True, verify=False)
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

     args = parser.parse_args()


     im=ImageInterface()
     r =im.get_image(args.exoimageHost, args.project, args.user, args.password, args.imageId)
     print ("Headers: %s" % r.headers)
     if r.status_code == 200:
        with open(args.file, 'wb') as f:
            r.raw.decode_content = True
            shutil.copyfileobj(r.raw, f)
     print ("file download complete")

     sys.exit(0)

if __name__ == '__main__':
    main()
