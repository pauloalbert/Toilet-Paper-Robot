#------------launch options------------------------------------------------------
from clint.textui import colored
import sys
camera=0
if '-h' in sys.argv or '--help' in sys.argv:
    print(colored.cyan('Usage: ')+'python3 vision_class.py [-s / --stream] [-l / --local] [-p / --port {camera port}]  '
          '[-nts / --networktables-server {networktable ip address}]')
    exit(0)

if '-s' in sys.argv or '--stream' in sys.argv:
    is_stream = True
else:
    is_stream=False

if '-l' in sys.argv or '--local' in sys.argv:
    is_local = True
else:
    is_local=False

if '-p' in sys.argv or '--port' in sys.argv:
    try:
        try:
            index=sys.argv.index('-p')
        except:
            index=sys.argv.index('--port')
        camera=int(sys.argv[index+1])
    except ValueError:
        print(colored.red('ERROR: Not A Valid Camera Port'))
        exit(11)
else:
    camera=0

if '-nts' in sys.argv or '--networktables-server' in sys.argv:
    try:
        index=sys.argv.index('-nts')
    except:
        index=sys.argv.index('--networktabless-server')
    try:
        nt_server=sys.argv[index+1]
    except:
        print('You Must Enter A Valid IP Address')
        exit(12)
else:
    nt_server="roboRIO-{team_number}-FRC.local".format(team_number=5987)
print('NetworkTables Server: '+colored.green(nt_server))

#------------getting the ip------------------------------------------------------
import netifaces as ni

ip=None
for interface in ni.interfaces():
    if ip is None:
        try:
            ip=ni.ifaddresses(interface)[ni.AF_INET][0]['addr']
            start=ip.split('.')[0]
            if start == '127':
                ip=None
        except KeyError:
            pass
    else:
        break
print('IP: '+colored.green(ip))
#-----------------------------Starting The Vision Class--------------------------------------------------------------

import cv2
import numpy as np
import math
from networktables import NetworkTables
class Vision:
    def __init__(self):
        """
        Summary: Start camera, read and analyze first frame.
        Parameters:
            * cam : The camera the code will use. Set to 0 if there's only one connected, check ports if more than one.
            Usually, a USB attached camera will have a bigger port than native ones.
            * frame : A numpy array. The frame the camera captured. All contours and hulls will be drawn on it.
            * hsv : The conversion of the values in frame from BGR to HSV, since our code operates on it.
            * mask : The pixels found within a preset range.
            * contours : A numpy array, converted to a list for easy of use. Stores the x and y values of all the contours.
            * hulls : A list of hulls, empty until the hull() function is called.
            * centers_x : A list of the x values of all centers, empty until the find_center() function is called.
            * centers_y : A list of the y values of all centers, empty until the find_center() function is called.
            * center : The average point of all centers of all contours.
            * font: The font we'll write the text on the frame with.
            * calibaration: A boolean that says whether we want to calibrate the distance function.
            * stream: The stream we may send to a server.
            * cal_fun: The dictionary of functions by which we can calibrate and filter contours. The first variable in
            the tuple is the string command, the second one is whether it needs to be average'd.
            * sees_target: A boolean that says whether the target was found.
        """
        self.cam = cv2.VideoCapture(camera)
        self.distance=0
        # self.cam.set(cv2.CAP_PROP_AUTO_EXPOSURE, 0)
        # self.cam.set(cv2.CAP_PROP_AUTOFOCUS, 0)
        # self.cam.set(cv2.CAP_PROP_SETTINGS, 1)
        # Reads the latest values of the files
        NetworkTables.initialize(server=nt_server)
        self.table = NetworkTables.getTable("SmartDashboard")
        file = open('Values.val','r')
        execution=file.read()
        exec(execution)
        file.close()
        _, self.frame = self.cam.read()
        self.show_frame=self.frame.copy()
        self.hsv = cv2.cvtColor(self.frame, cv2.COLOR_BGR2HSV)
        self.set_range()
        self.filter_hsv()
        _, contours, _ = cv2.findContours(self.mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        self.contours=list(contours)
        self.hulls = []
        self.centers_x = []
        self.centers_y = []
        self.center = (0, 0)
        self.font = cv2.FONT_HERSHEY_SIMPLEX
        self.stream=[]
        self.cal_fun = {
            'area': ("cv2.contourArea(c)", False),
            'extent': ("cv2.contourArea(c) / (cv2.minAreaRect(c)[1][0] * cv2.minAreaRect(c)[1][1])", False),
            'height': ("cv2.boundingRect(c)[3]", True), 'hull': ("cv2.contourArea(c) / cv2.contourArea(cv2.convexHull(c))", False),
            'circle': ('self.ellipse_area(c) / cv2.contourArea(cv2.convexHull(c))',True),
            'Aspect Ratio': ('cv2.boundingRect(c)[2] / cv2.boundingRect(c)[3]',True)
            }
        self.sees_target = False
        """
        Summary: Get SmartDashboard. 
        """
        # Sends all values to SmartDashboard
        self.set_item("Command", self.command_s)
        self.set_item("Draw contours", self.draw_contours_b)
        self.set_item("Draw hulls", self.draw_hulls_b)
        self.set_item("DiRode iterations", self.dirode_iterations_i)
        self.set_item("Find center", self.find_center_b)
        self.set_item("Method", self.find_by_s)
        self.set_item("Focal length", self.focal_l_f)
        self.set_item("Real height", self.real_height_f)
        self.set_item("Sees target", self.sees_target)
        self.set_item("Raspberry PI IP", ip)

    def set_item(self, key, value):
        """
        Summary: Add a value to SmartDashboard.

        Parameters:
            * key : The name the value will be stored under and displayed.
            * value : The information the key will hold.
            * value_type : The type of the value it recieved (string, integer, boolean, etc.).
        """
        value_type = type(value)
        if value_type is str:
            self.table.putString(key, value)
        elif value_type is int or value_type is float:
            self.table.putNumber(key, value)
        elif value_type is bool:
            self.table.putBoolean(key, value)

    def get_item(self, key, default_value):
        """
        Summary: Get a value from SmartDashboard.

        Parameters:
            * key : The name the value is stored under.
            * default_value : The value returned if key holds none.
        """
        try:
            res = self.table.getString(key, default_value)
        except:
            try:
                res = self.table.getNumber(key, default_value)
            except:
                res = self.table.getBoolean(key, default_value)
        return res

    def set_range(self):
        # Retrieves the range written in "Ace" which was written there by Range Finder 3.0
        file = open("Ace.acpf", 'r')
        exec(file.read())
        file.close()

    def filter_hsv(self):
        self.hsv = cv2.cvtColor(self.frame, cv2.COLOR_BGR2HSV)
        # toilet paper
        mask_white = cv2.inRange(self.hsv, self.lower_white_range, self.upper_white_range)
        # light reflector
        mask_green = cv2.inRange(self.hsv, self.lower_green_range, self.upper_green_range)
        self.mask = cv2.bitwise_or(mask_white, mask_green)
        self.dirode()

    def ellipse_area(self,c):
        _,(w,h),_=cv2.minAreaRect(c)
        r1=w/2
        r2=h/2
        ellipse_area=r1*r2*math.pi
        return ellipse_area

    def draw_contours(self):
        # Draws contours on the frame, if asked so on SmartDashboard
        if len(self.contours) > 0 and self.get_item("Draw contours", self.draw_contours_b):
            # Works with indexes instead of the contours themselves
            for x in range(0, len(self.contours)):
                # Draws all contours in blue
                cv2.drawContours(self.show_frame, self.contours[x], -1, (255, 0, 0), 3)
                # Draws a green rectangle around the target.
                #cv2.rectangle(self.frame.copy(), (cv2.boundingRect(self.contours[x])[0], cv2.boundingRect(self.contours[x])[1]), (cv2.boundingRect(self.contours[x])[0]+cv2.boundingRect(self.contours[x])[2], cv2.boundingRect(self.contours[x])[1]+cv2.boundingRect(self.contours[x])[3]),(0,255,0),2)
                # Draws hulls on the frame, if asked so on SmartDashboard
                if len(self.hulls) > 0 and self.get_item("Draw hulls", self.draw_hulls_b):
                    # Finds all defects in the outline compared to the hull
                    defects = cv2.convexityDefects(self.contours[x], self.hulls[x])
                    for i in range(defects.shape[0]):
                        s, e, f, _ = defects[i, 0]
                        start = tuple(self.contours[x][s][0])
                        end = tuple(self.contours[x][e][0])
                        far = tuple(self.contours[x][f][0])
                        cv2.line(self.show_frame, start, end, [0, 255, 0], 2)
                        cv2.circle(self.show_frame, far, 5, [0, 0, 255], -1)

    def dirode(self):
        # Dialates and erodes the mask to reduce static and make the image clearer
        # The kernel both functions will use
        kernel = [
            [0,1,0],
            [1,1,1],
            [0,1,0]
        ]
        kernel=np.array(kernel,dtype=np.uint8)
        self.mask=cv2.dilate(self.mask, kernel, iterations = self.get_item("DiRode iterations", self.dirode_iterations_i))
        self.mask=cv2.erode(self.mask, kernel, iterations = self.get_item("DiRode iterations", self.dirode_iterations_i))

    def find_center(self):
        # Finds the average of all centers of all contours
        self.centers_x.clear()
        self.centers_y.clear()
        if self.get_item("Find center", self.find_center_b) and len(self.contours) > 0:
            for c in self.contours:
                # Uses the center of the minimum enclosing circle
                (x, y), _ = cv2.minEnclosingCircle(c)
                # Adds both x and y values to the lists
                self.centers_x.append(x)
                self.centers_y.append(y)
            # Finds the average center of all contours and draws it on the frame
            self.center = (int((sum(self.centers_x) / len(self.centers_x))), (int(sum(self.centers_y) / len(self.centers_y))))
            cv2.putText(self.show_frame, "o {}".format(self.center), self.center, self.font, 0.5, 255)

    def get_contours(self):
        # Executes a command line from SmartDashboard
        command = self.get_item("Command", self.command_s)
        # Splits the command into separate instructions
        functions = command.split(";")
        self.hulls.clear()
        if len(functions) > 0:
            for fun in functions:
                # Separates the instruction into method and margin
                fun = fun.split(",")
                if fun[0] is not '' and len(self.contours) > 0:
                    # Creates a list of all appropriate contours
                    possible_fit = []
                    for c in self.contours:
                        if cv2.contourArea(c)>0:
                            if float(fun[2]) > float(eval(self.cal_fun[fun[0]][0])) > float(fun[1]):
                                possible_fit.append(c)
                                if fun[0] == 'hull':
                                    self.hulls.append(cv2.convexHull(c, returnPoints=False))
                    # Updates the contour list
                    self.contours = possible_fit

    def get_angle(self):
        # Returns the angle of the center of contours from the camera based on the focal length and trigonometry
        self.angle = math.atan((self.center[0]-self.frame.shape[1]/2)/self.get_item("Focal length", self.focal_l_f))*(180/math.pi)
        cv2.putText(self.show_frame, "Angle: {}".format(self.angle), (5, 15), self.font, 0.5, 255)

    def get_distance(self):
        # Returns the distance of the target from camera based on trigonometry, focal length and its known real height
        alpha=-math.atan((self.center[1]-self.frame.shape[1]/2)/self.get_item("Focal length", self.focal_l_f))
        try:
            self.distance=self.get_item("Real height", self.real_height_f)/math.tan(alpha)
        except ZeroDivisionError:
            pass
        cv2.putText(self.show_frame, "distance: " + str(self.distance), (50, 150), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)
        cv2.putText(self.show_frame, "alpha: " + str(alpha*(180/math.pi)), (50, 100), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)

#-----------Setting Global Variables For Thread-work----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

global vision
global stop
stop=False
try:
    vision=Vision()
except AttributeError:
    print(colored.red("ERROR: Camera Port Has No Camera "))
    exit(13)

#-------Getting The Stream Ready------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

from flask import Flask, render_template, Response

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/video_feed')
def video_feed():
    return Response(gen(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

def gen():
    global stop
    global vision
    while not stop:
        jpg=cv2.imencode('.jpg',vision.show_frame)[1].tostring()
        yield (b'--frame\r\n'b'Content-Type: image/jpeg\r\n\r\n' + jpg + b'\r\n')
        key=cv2.waitKey(1)

#--------------Setting Up The Thread Functions-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

def get_frame():
    global stop
    global vision
    while not stop:
        _,vision.frame=vision.cam.read()
        # cv2.line(vision.frame,(0,int(vision.frame.shape[0]/2)),(int(vision.frame.shape[1]),int(vision.frame.shape[0]/2)),(0,0,0),int(1),int(1))
        vision.show_frame=vision.frame.copy()
        key=cv2.waitKey(1)

def analyse():
    global stop
    global vision
    while not stop:
        vision.filter_hsv()
        _, contours, _ = cv2.findContours(vision.mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        vision.contours=list(contours)
        vision.get_contours()
        if (len(vision.contours)) > 0:
            vision.sees_target = True
            vision.set_item("Sees target", vision.sees_target)
            vision.draw_contours()
            vision.find_center()
            vision.get_angle()
            vision.get_distance()


def show():
    global stop
    global vision
    if is_stream:
        app.run(host=ip, debug=False)
    if is_local:
        while not stop:
            cv2.imshow('Frame',vision.show_frame)
            cv2.imshow('Mask', vision.mask)
            vision.key=cv2.waitKey(1)
            if vision.key is ord('q'):
                cv2.destroyAllWindows()
                stop=True

#---------------Starting The Threads--------------------------------------------------------------------------------------------------

import threading
threading._start_new_thread(get_frame,())
threading._start_new_thread(analyse,())
show()
if not is_local and not is_stream:
    print('Press '+colored.cyan("Enter")+' To End It All')
    _=input()
stop=True
