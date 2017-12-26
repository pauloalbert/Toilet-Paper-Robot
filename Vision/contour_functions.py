import math
import cv2


def area(c):
    return cv2.contourArea(c)


def extent(c):
    return cv2.contourArea(c) / (cv2.minAreaRect(c)[1][0] * cv2.minAreaRect(c)[1][1])


def height(c):
    return cv2.boundingRect(c)[3]


def hull(c):
    return cv2.contourArea(c) / cv2.contourArea(cv2.convexHull(c))


def circle(c):
    return ellipse_area(c) / cv2.contourArea(cv2.convexHull(c))


def aspect_ratio(c):
    return cv2.boundingRect(c)[2] / cv2.boundingRect(c)[3]


def ellipse_area(c):
    _, (w, h), _ = cv2.minAreaRect(c)
    r1 = w / 2
    r2 = h / 2
    e_area = r1 * r2 * math.pi
    return e_area
