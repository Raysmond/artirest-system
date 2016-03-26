#!/usr/bin/python

import paramiko
import threading
import time
import sys

ips = ['10.131.245.176', '10.131.245.196', '10.131.245.181']
user = 'root'
password = ''

def execute_cmds(ip, user, passwd, cmd):
    try:
        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        ssh.connect(ip,22,user,passwd,timeout=5)
        for m in cmd:
            print m
            stdin, stdout, stderr = ssh.exec_command(m)
#           stdin.write("Y")
            out = stdout.readlines()
            for o in out:
                print o,
        print '%s\tOK\n'%(ip)
        ssh.close()
    except :
        print '%s\tError\n'%(ip)


if __name__=='__main__':
    cmd = [
        'docker load -i /root/raysmond/artirest.tar'
    ]

    for ip in ips:
        print "Copy images to %s"%(ip)
        threading.Thread(target=execute_cmds, args=(ip,user,password,cmd)).start()
