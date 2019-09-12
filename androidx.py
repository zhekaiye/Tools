#encodeing:utf-8
import csv
import os
import shutil

def searchFile(path):
    if os.path.isfile(path):
        replaceWithAndroidX(path)
    else:
        fileList = os.listdir(path)
        for fileName in fileList:
            subFile = path + '/' + fileName
            searchFile(subFile)

def replaceWithAndroidX(path):
    srcFile = open(path, 'r')
    dstFile = open(path + ".tmp", 'w')
    count = 0
    result = ""
    needWrite = False
    for line in srcFile.readlines():
        #count = count + 1
        lineMatch = False
        for key in android_dict.keys():
            if key in line:
                # print(path + " ==> ")
                # print("    " + line.rstrip('\n'))
                # print("    line number=%d" % count)
                needWrite = True
                lineMatch = True
                result = result + line.replace(key, android_dict[key], 3)
                break
        if lineMatch == False:
            result += line

    if needWrite:
        dstFile.write(result)
        srcFile.close()
        os.unlink(path)
        shutil.move(path + ".tmp", path)
        dstFile.close()
    else:
        dstFile.close()
        os.unlink(path + ".tmp")
        srcFile.close()

csvFile = open('androidx.csv', 'r')
reader = csv.reader(csvFile)

android_dict = {}

for line in reader:
    android_dict[line[0]] = line[1]

searchFile('LastTwoFeat/security-v5/stock_option_sdk');

csvFile.close()