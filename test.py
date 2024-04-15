from socket import socket, AF_INET, SOCK_STREAM
from ossapi import Ossapi
Ossapi
serversocket = socket(AF_INET, SOCK_STREAM)
serversocket.bind(("localhost", 3727))
serversocket.listen(1)
connection, _ = serversocket.accept()

data = connection.recv(8192).decode()
connection.send(b"HTTP/1.0 200 OK\n")
connection.send(b"Content-Type: text/html\n")
connection.send(b"\n")
connection.send(b"""
        <html><body>
        <h2>Code received.</h2>
        You may now close this tab safely.
        </body></html>""")
connection.close()
serversocket.close()

code = data.split("code=")[1].split(" ")[0]
print(code)