import cv2
import numpy as np
import os
from rembg import remove

os.chdir(os.path.dirname(os.path.abspath(__file__)))

img = cv2.imread('input\sample.jpg')
img = cv2.resize(img, (1000, 500))

classNames=[]
classFile = 'coco.names'
with open(classFile, 'rt') as f:
    classNames = f.read().rstrip('\n').split('\n')

configPath = 'ssd_mobilenet_v3_large_coco_2020_01_14.pbtxt'
weightsPath = 'frozen_inference_graph.pb'

net = cv2.dnn_DetectionModel(weightsPath, configPath)
net.setInputSize(320,320)
net.setInputScale(1.0/ 127.5)
net.setInputMean((127.5, 127.5, 127.5))
net.setInputSwapRB(True)

classIds, confs, bbox = net.detect(img, confThreshold=0.5)

i = 1
for classIds, confidence, box in zip(classIds.flatten(), confs.flatten(), bbox):
    img_name = "output\\" + str(i) + ".jpg"
    cropped = (img[box[1]:box[1] + box[3], box[0]:box[2] + box[0]])
    cv2.imwrite(str(img_name), remove(cropped))
    # cv2.rectangle(img, box, color=(0,255,0))
    i += 1

cv2.imshow("Output",img)
cv2.waitKey(0)