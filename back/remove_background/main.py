import cv2
import os
from rembg import remove

os.chdir(os.path.dirname(os.path.abspath(__file__)))

input = cv2.imread("input\dog.jpg")
output = cv2.imwrite("output\dog.jpg", remove(input))
