import sys

camera=0

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
        print('Not A Valid Camera Port')
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

if '-h' in sys.argv or '--help' in sys.argv:
    print('Usage: python3 vision_class.py [-s / --stream] [-l / --local] [-p / --port {camera port}]  '
          '[-nts / --networktables-server {networktable ip address}]')
    exit(0)
